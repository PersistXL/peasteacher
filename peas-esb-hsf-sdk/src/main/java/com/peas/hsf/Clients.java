package com.peas.hsf;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.peas.hsf.http.Callback;
import com.peas.hsf.http.Client;
import com.peas.hsf.http.Http;
import com.peas.hsf.http.security.PeasHostnameVerifier;
import com.peas.hsf.http.security.SSLClientContext;
import com.peas.hsf.model.Form;
import com.peas.hsf.tool.ResponseChecker;
import com.peas.hsf.tool.StreamUtil;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Http客户端
 *
 * @author dyh 2015年2月3日
 * @see
 * @since 1.0
 */
@Log4j
public final class Clients {

    /**
     * 构造器私有化
     */
    private Clients() {
    }

    /**
     * 创建客户端
     *
     * @param baseAddress 基础地址
     * @param sslcontext  ssl容器
     * @return 客户端
     */
    public static Client builder(URI baseAddress, SSLClientContext sslcontext) {
        return new ClientImpl(baseAddress, sslcontext);
    }

    /**
     * 创建客户端
     *
     * @param baseAddress 基础地址
     * @return 客户端
     */
    public static Client builder(URI baseAddress) {
        return new ClientImpl(baseAddress);
    }

    public static Client builder() {
        return new ClientImpl(URI.create(""));
    }

    public abstract static class AbstractClient implements Client {
        /**
         * 默认的访问类型
         */
        private static final String DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_JSON;

        private String mediaType;

        private String baseAddress;

        private javax.ws.rs.client.Client client;

        private boolean needCheck = true;

        protected boolean needResponse = false;

        @Getter
        private SSLClientContext context;

        private Map<String, Object> headers = Maps.newConcurrentMap();

        private Map<String, Object> cookies = Maps.newConcurrentMap();

        private Consumer<Map<String, NewCookie>> cookieConsumer;

        /**
         * 默认处理方式为抛出异常
         */
        private Consumer<Throwable> throwableConsumer = throwable -> Throwables.propagate(throwable);

        /**
         * @param baseAddress
         */
        private AbstractClient(URI baseAddress) {
            String s = Objects.toString(baseAddress.toString());
            if (s.endsWith("/")) {
                s = s.substring(0, s.length() - 1);
            }
            this.baseAddress = s;
            client = ClientBuilder.newClient();
            client.register(MultiPartFeature.class);
            log.debug("客户端创建成功");
        }

        private AbstractClient(URI baseAddress, SSLClientContext context) {
            String s = Objects.toString(baseAddress.toString());
            if (s.endsWith("/")) {
                s = s.substring(0, s.length() - 1);
            }
            this.baseAddress = s;
            this.context = context;
            client = ClientBuilder.newBuilder().sslContext(context.getContext()).hostnameVerifier(new PeasHostnameVerifier()).build();
            client.register(MultiPartFeature.class);
            log.info(String.format("客户端创建成功，认证方式为: [ %s ]", context.isOneWayAuthority() ? "服务器单方认证" : "服务器客户端双方互相认证"));
        }

        protected Object dealResponse(Response response) {
            if (needResponse) {
                return response;
            }
            int status = response.getStatus();
            log.debug(String.format("response status is : %s", status));
            log.debug(String.format("response description is : %s", Status.fromStatusCode(status).getReasonPhrase()));
            if (cookieConsumer != null) {
                cookieConsumer.accept(response.getCookies());
            }
            if (status == Status.NO_CONTENT.getStatusCode()) {
                checkResponseStatus(status, null);
                log.debug("response content-type is empty");
                log.debug("response return entity is processed for : null");
                log.debug("response return entity is processed value is : null");
                return "";
            }
            String contentType = response.getHeaderString(Http.Headers.CONTENT_TYPE);
            InputStream inputStream = (InputStream) response.getEntity();
            byte[] bytes = StreamUtil.read(inputStream);
            log.debug(String.format("response content-type is : %s ", contentType));
            String ct = Objects.toString(contentType, "").toLowerCase();
            Object result;
            if (ct.matches("application/.*(json|xml).*|(text/.+)") || Strings.isNullOrEmpty(ct)) {
                result = new String(bytes, Charset.forName("utf-8"));
            } else {
                result = bytes;
            }
            log.debug(String.format("response return entity is processed for : %s", result.getClass().getName()));
            log.debug(String.format("response return entity is processed value is : %s", result));
            response.close();
            checkResponseStatus(status, result);
            return result;
        }

        @Override
        public void register(Class<?> clz) {
            this.client.register(clz);
        }

        @Override
        public Client auth(String username, String password) {
            this.client.register(HttpAuthenticationFeature.basic(username, password));
            return this;
        }

        @Override
        public Client byJson() {
            return setMediaType(MediaType.APPLICATION_JSON);
        }

        @Override
        public Client byFile() {
            return setMediaType(MediaType.MULTIPART_FORM_DATA);
        }

        @Override
        public Client byXml() {
            return setMediaType(MediaType.APPLICATION_XML);
        }

        @Override
        public Client byText() {
            return setMediaType(MediaType.TEXT_PLAIN);
        }

        @Override
        public Client byOctet() {
            return setMediaType(MediaType.APPLICATION_OCTET_STREAM);
        }

        @Override
        public Client byForm() {
            return setMediaType(MediaType.MULTIPART_FORM_DATA);
        }

        @Override
        public Client byFormUnEncoded() {
            return setMediaType(MediaType.APPLICATION_FORM_URLENCODED);
        }

        @Override
        public Client by(String by) {
            return setMediaType(by);
        }

        @Override
        public Client checkResponse() {
            needCheck = true;
            return this;
        }

        @Override
        public Client header(String head, Object value) {
            headers.put(head, value);
            return this;
        }

        @Override
        public Client cookie(String name, String value) {
            cookies.put(name, Objects.toString(value));
            return this;
        }

        @SuppressWarnings("unchecked")
        private void checkResponseStatus(int statusCode, Object result) {
            if (!needCheck || null == result) {
                return;
            }
            if (!Status.Family.familyOf(statusCode).equals(Status.Family.SUCCESSFUL)) {
                Preconditions.checkState(false, String.format("Response code:%s\t  ERROR-MESSAGE:\t %s", statusCode, Objects.toString(result)));
            }
            Preconditions.checkState(ResponseChecker.isOk(result), Objects.toString(result));
        }

        protected String getMediaType() {
            return Strings.isNullOrEmpty(mediaType) ? DEFAULT_MEDIA_TYPE : mediaType;
        }

        protected Client setMediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        protected Invocation.Builder newBuilder(String resource) {
            String link = formatLink(resource);
            WebTarget target = client.target(link);
            Invocation.Builder builder = target.request().header(Http.Headers.CONTENT_TYPE, getMediaType());
            cookies.forEach((n, v) -> builder.cookie(n, Objects.toString(v)));
            headers.forEach((key, value) -> builder.header(key, value));
            headers.clear();
            return builder;
        }

        protected void async(final Callback callback, final Callable<Object> call) {
            new Thread(() ->
            {
                try {
                    Object object = call.call();
                    if (!"EXCEPTION".equals(object)) {
                        callback.handle(object);
                    }
                } catch (Exception e1) {
                    handleThrowable(e1);
                }
            }).start();
        }

        private String formatLink(String resource) {
            resource = Objects.toString(resource);
            if (resource.startsWith("/")) {
                resource = resource.substring(1, resource.length());
            }
            String link = String.format("%s/%s", this.baseAddress, resource);
            if (link.trim().startsWith("/")) {
                link = link.substring(1, link.length());
            }
            if (link.endsWith("/")) {
                link = link.substring(0, link.length() - 1);
            }
            log.debug("request url: " + link);
            return link;
        }

        @Override
        public Client callCookie(Consumer<Map<String, NewCookie>> consumer) {
            this.cookieConsumer = consumer;
            return this;
        }

        @Override
        public Client cookie(Collection<NewCookie> cookie) {
            cookie.forEach(c -> this.cookie(c));
            return this;
        }

        @Override
        public Client cookie(NewCookie cookie) {
            cookie(cookie.getName(), cookie.getValue());
            return this;
        }

        @Override
        public Client onThrowable(Consumer<Throwable> consumer) {
            throwableConsumer = consumer;
            return this;
        }


        protected void handleThrowable(Throwable throwable) {
            if (throwableConsumer != null) {
                throwableConsumer.accept(throwable);
            }
        }
    }

    /**
     * 客户端实现
     *
     * @author dyh 2015年2月3日
     * @see
     * @since 1.0
     */
    public static class ClientImpl extends AbstractClient {
        public ClientImpl(URI baseAddress) {
            super(baseAddress);
        }

        public ClientImpl(URI baseAddress, SSLClientContext context) {
            super(baseAddress, context);
        }

        @Override
        public Object post(String resource, Object argument) {
            try {
                return dealResponse(newBuilder(resource).buildPost(Entity.entity(dealParam(argument), getMediaType())).invoke());
            } catch (Exception e) {
                handleThrowable(e);
            }
            return "EXCEPTION";
        }

        @Override
        public Object put(String resource, Object argument) {
            try {
                return dealResponse(newBuilder(resource).buildPut(Entity.entity(dealParam(argument), getMediaType())).invoke());
            } catch (Exception e) {
                handleThrowable(e);
            }
            return "EXCEPTION";
        }

        @Override
        public Object get(String resource) {
            try {
                return dealResponse(newBuilder(resource).buildGet().invoke());
            } catch (Exception e) {
                handleThrowable(e);
            }
            return "EXCEPTION";
        }

        @Override
        public Object delete(String resource) {
            try {
                return dealResponse(newBuilder(resource).buildDelete().invoke());
            } catch (Exception e) {
                handleThrowable(e);
            }
            return "EXCEPTION";
        }

        @Override
        public void asyncPost(final String resource, final Object argument, final Callback callback) {
            async(callback, () -> post(resource, argument));
        }

        @Override
        public void asyncPut(final String resource, final Object argument, final Callback callback) {
            async(callback, () -> put(resource, argument));
        }

        @Override
        public void asyncGet(final String resource, final Callback callback) {
            async(callback, () -> get(resource));
        }

        @Override
        public void asyncDelete(final String resource, final Callback callback) {
            async(callback, () -> delete(resource));
        }

        @Override
        public Client returnResponse() {
            needResponse = true;
            return this;
        }


        private Object dealParam(Object object) {
            if (object instanceof Form) {
                Form form = (Form) object;
                return form.getEntry();
            }
            return object;
        }
    }
}
