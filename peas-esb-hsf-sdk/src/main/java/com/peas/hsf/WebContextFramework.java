package com.peas.hsf;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.peas.hsf.filter.HsfWebFilter;
import com.peas.hsf.http.HsfErrorPageGenerator;
import com.peas.hsf.http.security.SSLServerContext;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.http.server.filecache.FileCache;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.DispatcherType;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.EnumSet;

/**
 * Created by duan on 2015/8/27.
 */
public class WebContextFramework {

    private static String webContext;


    public static Server build(String host, String context) {
        return new Server(host, context, null);
    }

    public static String getWebContextName() {
        return webContext;
    }


    public static class Server {

        private WebappContext webappContext;

        private HttpServer server;

        private SSLServerContext sslServerContext;

        private String host;

        private String context;

        public Server(String host, String context, SSLServerContext sslServerContext) {
            this.host = host;
            this.context = context;
            webContext = context.replaceAll("/", "");
            this.sslServerContext = sslServerContext;
            init();
        }

        private void init() {
            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(host), false);
            if (sslServerContext != null) {
                server.getListeners().forEach((listener) -> listener.setSSLEngineConfig(sslServerContext.getContext()));
            }
            ServerConfiguration serverConfiguration = server.getServerConfiguration();
            serverConfiguration.setDefaultQueryEncoding(Charset.forName("utf-8"));
            serverConfiguration.setDefaultErrorPageGenerator(new HsfErrorPageGenerator());
            webappContext = new WebappContext("RESTFUL-WEB", context);
            webappContext.addFilter("request", HsfWebFilter.class).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), "/*");
        }

        public Server addResource(String apiPattern, String... packageName) {
            ServletContainer servletContainer = new ServletContainer(new Application() {
                @Override
                protected void registerClass() {
                    packages(packageName);
                }
            });
            ServletRegistration servletRegistration = webappContext.addServlet("restful-api1", servletContainer);
            servletRegistration.addMapping(apiPattern);
            return this;
        }

        public Server addResource(String apiPattern, Application application) {
            ServletContainer servletContainer = new ServletContainer(application);
            ServletRegistration servletRegistration = webappContext.addServlet("restful-api2", servletContainer);
            servletRegistration.addMapping(apiPattern);
            return this;
        }

        public Server withHandler(Handler handler) {
            handler.handle(server, webappContext);
            return this;
        }

        public Server withSpringSupport(String springConfig) {
            webappContext.addContextInitParameter("contextConfigLocation", springConfig);
            webappContext.addListener("org.springframework.web.context.ContextLoaderListener");
            return this;
        }

        public Server withSpringSupport(String springConfig, Class<?> springListener) {
            webappContext.addContextInitParameter("contextConfigLocation", springConfig);
            webappContext.addListener(springListener.getName());
            return this;
        }

        public Server withSpringServletSupport(String springConfig) {
            ServletRegistration servletRegistration = webappContext.addServlet("spring", "org.springframework.web.servlet.DispatcherServlet");
            servletRegistration.addMapping(context);
            servletRegistration.setInitParameter("contextConfigLocation", springConfig);
            servletRegistration.setInitParameter("detectAllHandlerExceptionResolvers", "false");
            servletRegistration.setLoadOnStartup(1);
            servletRegistration.setAsyncSupported(true);
            return this;
        }

        public Server withWebSocket(String urlPattern, WebSocketApplication webSocket) {
            WebSocketAddOn addOn = new WebSocketAddOn();
            addOn.setTimeoutInSeconds(60);
            server.getListeners().forEach((listener) -> listener.registerAddOn(addOn));
            WebSocketEngine.getEngine().register("", urlPattern, webSocket);
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
            sh.setFileCacheEnabled(false);
            sc.setSendFileEnabled(false);
            sc.addHttpHandler(sh, resourceContext);
            return this;
        }

        public void start() {
            webappContext.deploy(server);
            try {
                server.getListeners().forEach((listener) -> {
                    FileCache fileCache = listener.getFileCache();
                    fileCache.setEnabled(false);
                    fileCache.setFileSendEnabled(false);
                });
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface Handler {
        void handle(HttpServer server, WebappContext webappContext);
    }
}
