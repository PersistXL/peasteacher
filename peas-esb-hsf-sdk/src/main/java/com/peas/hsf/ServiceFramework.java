package com.peas.hsf;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.peas.hsf.http.security.AuthenticationVerifier;
import com.peas.hsf.http.security.PeasHostnameVerifier;
import com.peas.hsf.http.security.SSLServerContext;
import com.peas.hsf.http.security.SecurityFilter;
import lombok.extern.log4j.Log4j;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

/**
 * 服务框架
 *
 * @author dyh
 */
@Log4j
public class ServiceFramework {

    private static final String defaultLog4jConfigurator = "com.peas.common.base.Log4jConfigurator";

    private static final ServiceFramework instance = new ServiceFramework();

    private static Properties properties;

    /**
     * 私有构造器
     */
    private ServiceFramework() {

    }

    public static ServiceFramework withProperties(Properties prop) {
        properties = prop;
        return instance;
    }

    public Server build() {
        final String baseUri = properties.getProperty("server.publish.address");
        final String spring = properties.getProperty("server.publish.spring");
        final String rPackages = properties.getProperty("server.publish.resources.package");
        final String log4j = properties.getProperty("server.publish.log4j");
        final String ssl = properties.getProperty("server.publish.ssl");
        Preconditions.checkState(!Strings.isNullOrEmpty(baseUri), "'server.publish.address' can not be null");
        Preconditions.checkState(!Strings.isNullOrEmpty(rPackages), "'server.publish.resources.package' can not be null");
        if (!Strings.isNullOrEmpty(log4j)) {
            try {
                Class<?> clz = Class.forName(defaultLog4jConfigurator);
                Method m = clz.getMethod("configure", String.class);
                m.invoke(null, log4j);
            } catch (Exception e) {
                log.warn("未找到Log4j适配器", e);
            }
        }
        final URI base = URI.create(baseUri);
        final Application application = new Application() {
            @Override
            protected void registerClass() {
                packages(rPackages);
            }
        };
        if (!Strings.isNullOrEmpty(spring) && !Strings.isNullOrEmpty(ssl)) {
            return build(base, application, spring, new SSLServerContext(ssl));
        }

        if (!Strings.isNullOrEmpty(spring)) {
            return build(base, application, spring);
        }

        if (!Strings.isNullOrEmpty(ssl)) {
            return build(base, application, new SSLServerContext(ssl));
        }
        return build(base, application);
    }

    /**
     * 创建服务
     *
     * @param baseUri     基础URL
     * @param application 发布的资源
     * @return 服务
     */
    public static Server build(URI baseUri, Application application) {
        return new Server(baseUri, application, null);
    }

    /**
     * 创建服务
     *
     * @param baseUri     基础URL
     * @param application 发布的资源
     * @param context     安全容器
     * @return 服务
     */
    public static Server build(URI baseUri, Application application, SSLServerContext context) {
        return new Server(baseUri, application, context);
    }

    /**
     * 创建服务
     *
     * @param baseUri            基础URL
     * @param application        发布的资源
     * @param springXmlClassPath Spring 文件路径
     * @return 服务
     */
    public static Server build(URI baseUri, Application application, String springXmlClassPath) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(Application.SPRING_CONFIG_LOCATION, springXmlClassPath);
        application.addProperties(map);
        return new Server(baseUri, application, null);
    }

    /**
     * 创建服务
     *
     * @param baseUri            基础URL
     * @param application        发布的资源
     * @param springXmlClassPath Spring 文件路径
     * @return 服务
     */
    public static Server build(URI baseUri, Application application, String springXmlClassPath,
                               SSLServerContext context) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(Application.SPRING_CONFIG_LOCATION, springXmlClassPath);
        application.addProperties(map);
        return new Server(baseUri, application, context);
    }

    /**
     * 服务类
     *
     * @author dyh
     */
    public static class Server {
        private URI baseUri;

        private Application application;

        private HttpServer server;

        private SSLServerContext context;

        public Server(URI baseUri, Application application, SSLServerContext context) {
            this.baseUri = baseUri;
            this.application = application;
            this.context = context;
            if (context == null) {
                server = GrizzlyHttpServerFactory.createHttpServer(baseUri, application, false);
            } else {
                application.register(PeasHostnameVerifier.class);
                server = GrizzlyHttpServerFactory.createHttpServer(baseUri, application, true, context.getContext(), false);
            }
        }

        public Server withAuthentication(final AuthenticationVerifier verifier) {
            this.application.register(new SecurityFilter(verifier));
            return this;
        }

        /**
         * 附带静态资源
         *
         * @param resourcePath    本地资源(相对)路径
         * @param resourceContext web访问的Context uri
         * @return
         */
        public Server withStaticContent(String resourcePath, String resourceContext) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(resourcePath) && !Strings.isNullOrEmpty(resourceContext),
                    "arguments can not be null or empty");
            ServerConfiguration sc = server.getServerConfiguration();
            StaticHttpHandler sh = new StaticHttpHandler(resourcePath);
            sh.setRequestURIEncoding(Charset.forName("utf-8"));
            sh.setFileCacheEnabled(true);
            sc.setSendFileEnabled(true);
            sc.addHttpHandler(sh, resourceContext);
            return this;
        }


        /**
         * WebSocket
         *
         * @return
         */
        public Server withWebSocket(String contextPath, String urlPattern, WebSocketApplication application) {
            WebSocketAddOn addOn = new WebSocketAddOn();
            addOn.setTimeoutInSeconds(60);
            server.getListeners().forEach((listener) -> listener.registerAddOn(addOn));
            WebSocketEngine.getEngine().register(contextPath, urlPattern, application);
            return this;
        }

        /**
         * 启动服务
         */
        public void start() {
            try {
                server.getServerConfiguration().setDefaultQueryEncoding(Charset.forName("utf-8"));
                server.start();
                log.info(String.format("******Grizzly Http Server base uri : %s ******", baseUri));
                log.info(String.format("******Grizzly Http Server application.wadl path is : %s ******", baseUri + "/application.wadl"));
                if (context != null) {
                    log.info(String.format("服务器认证方式为:[%s]", context.isOneWayAuthority() ? "服务器单方认证" : "服务器客户端双方互相认证"));
                }
                log.info("****** Grizzly Http Server is started ******");
            } catch (IOException e) {
                log.error("****** Grizzly http server start server failed! ******", e);
                return;
            }
        }

        /**
         * 停止服务
         */
        public void stop() {
            server.shutdownNow();
        }
    }
}
