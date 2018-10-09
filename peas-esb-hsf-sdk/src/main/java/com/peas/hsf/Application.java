package com.peas.hsf;

import com.peas.hsf.filter.HsfRestfulWebFilter;
import com.peas.hsf.provider.HsfJacksonJsonProvider;
import com.peas.hsf.provider.JacksonConfigurator;
import com.peas.hsf.provider.ServiceExceptionMapper;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

/**
 * 资源注册初始化
 *
 * @author dyh 2015年2月3日
 * @see
 * @since 1.0
 */
public abstract class Application extends ResourceConfig {
    /**
     * Spring 配置文件路径Key
     */
    public static final String SPRING_CONFIG_LOCATION = "contextConfigLocation";

    /**
     * 构造器
     */
    public Application() {
        property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
        register(JacksonConfigurator.class);
        register(HsfJacksonJsonProvider.class);
        register(ServiceExceptionMapper.class);
        register(MultiPartFeature.class);
        register(HsfRestfulWebFilter.class);
        registerClass();
    }

    protected abstract void registerClass();

}
