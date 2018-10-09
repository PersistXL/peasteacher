package com.peas.hsf.hk2;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.ibatis.io.Resources.getResourceAsReader;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.type.TypeHandler;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import com.peas.hsf.tool.AntPathUtil;

/**
 * Created by duanyihui on 2017/3/21.
 */
public abstract class HsfMybatisModule extends HsfAbstractMybatisModule
{
    private static final String DEFAULT_CONFIG_RESOURCE = "mybatis-config.xml";

    private static final String DEFAULT_ENVIRONMENT_ID = null;

    private String classPathResource = DEFAULT_CONFIG_RESOURCE;

    private String environmentId = DEFAULT_ENVIRONMENT_ID;

    private List<String> mapperLocation;

    private Properties properties = new Properties();

    /**
     * Set the MyBatis configuration class path resource.
     *
     * @param classPathResource the MyBatis configuration class path resource
     */
    protected final void setClassPathResource(String classPathResource)
    {
        checkArgument(classPathResource != null, "Parameter 'classPathResource' must be not null");
        this.classPathResource = classPathResource;
    }

    /**
     * Set the MyBatis configuration environment id.
     *
     * @param environmentId the MyBatis configuration environment id
     */
    protected final void setEnvironmentId(String environmentId)
    {
        this.environmentId = environmentId;
    }

    protected final void setMapperLocation(String mapperLocation)
    {
        if (mapperLocation.startsWith("classpath:") || mapperLocation.startsWith("classpath*:"))
        {
            mapperLocation = mapperLocation.replace("classpath:", "").replace("classpath*:", "");
        }
        this.mapperLocation = AntPathUtil.getAllMatch(mapperLocation);
    }

    /**
     * Add the variables will be used to replace placeholders in the MyBatis
     * configuration.
     *
     * @param properties the variables will be used to replace placeholders in
     *        the MyBatis configuration
     */
    protected final void addProperties(Properties properties)
    {
        if (properties != null)
        {
            this.properties.putAll(properties);
        }
    }

    private Configuration createConfiguration() throws IOException
    {
        Configuration configuration = null;
        try (Reader reader = getResourceAsReader(getResourceClassLoader(), classPathResource))
        {
            XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader, environmentId, properties);
            configuration = xmlConfigBuilder.parse();
            if (mapperLocation != null && !mapperLocation.isEmpty())
            {
                for (String mapper : mapperLocation)
                {
                    XMLMapperBuilder mapperParser = new XMLMapperBuilder(Resources.getResource(mapper).openStream(),
                            configuration, mapper, configuration.getSqlFragments());
                    mapperParser.parse();
                }
            }
        }
        return Preconditions.checkNotNull(configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void internalConfigure()
    {
        this.initialize();

        try
        {
            Configuration configuration = createConfiguration();
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(configuration);
            bind(SqlSessionFactory.class).toInstance(sessionFactory);

            // bind mappers
            Collection<Class<?>> mapperClasses = configuration.getMapperRegistry().getMappers();
            for (Class<?> mapperType : mapperClasses)
            {
                bindMapper(mapperType);
            }

            // request injection for type handlers
            Collection<TypeHandler<?>> allTypeHandlers = configuration.getTypeHandlerRegistry().getTypeHandlers();
            for (TypeHandler<?> handler : allTypeHandlers)
            {
                requestInjection(handler);
            }

            // request injection for interceptors
            Collection<Interceptor> interceptors = configuration.getInterceptors();
            for (Interceptor interceptor : interceptors)
            {
                requestInjection(interceptor);
            }

            // request injection for object factory.
            requestInjection(configuration.getObjectFactory());

            // request injection for object wrapper factory.
            requestInjection(configuration.getObjectWrapperFactory());
        }
        catch (Exception e)
        {
            addError("Impossible to read classpath resource '%s', see nested exceptions: %s",
                    classPathResource,
                    e.getMessage());
        }
        finally
        {

        }
    }
}
