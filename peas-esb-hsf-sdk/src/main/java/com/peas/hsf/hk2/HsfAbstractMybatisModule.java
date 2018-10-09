package com.peas.hsf.hk2;

import static com.google.inject.matcher.Matchers.*;
import static com.google.inject.name.Names.named;
import static com.google.inject.util.Providers.guicify;

import java.lang.reflect.Method;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionManager;
import org.mybatis.guice.mappers.MapperProvider;
import org.mybatis.guice.session.SqlSessionManagerProvider;
import org.mybatis.guice.transactional.Transactional;
import org.mybatis.guice.transactional.TransactionalMethodInterceptor;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Scopes;
import com.google.inject.matcher.AbstractMatcher;

/**
 * Created by duanyihui on 2017/3/21.
 */
public abstract class HsfAbstractMybatisModule extends AbstractModule
{

    protected static final AbstractMatcher<Method> DECLARED_BY_OBJECT = new AbstractMatcher<Method>()
    {
        @Override
        public boolean matches(Method method)
        {
            return method.getDeclaringClass() == Object.class;
        }
    };

    protected static final AbstractMatcher<Method> SYNTHETIC = new AbstractMatcher<Method>()
    {
        @Override
        public boolean matches(Method method)
        {
            return method.isSynthetic();
        }
    };

    private ClassLoader resourcesClassLoader = getDefaultClassLoader();

    private ClassLoader driverClassLoader = getDefaultClassLoader();

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void configure()
    {
        try
        {
            // sql session manager
            bind(SqlSessionManager.class).toProvider(SqlSessionManagerProvider.class).in(Scopes.SINGLETON);
            bind(SqlSession.class).to(SqlSessionManager.class).in(Scopes.SINGLETON);

            internalConfigure();

            bindTransactionInterceptors();

            bind(ClassLoader.class).annotatedWith(named("JDBC.driverClassLoader")).toInstance(driverClassLoader);
        }
        finally
        {
            resourcesClassLoader = getDefaultClassLoader();
            driverClassLoader = getDefaultClassLoader();
        }
    }

    /**
     * bind transactional interceptors
     */
    protected void bindTransactionInterceptors()
    {
        // transactional interceptor
        TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
        requestInjection(interceptor);
        bindInterceptor(any(),
                not(SYNTHETIC).and(not(DECLARED_BY_OBJECT)).and(annotatedWith(Transactional.class)),
                interceptor);
        // Intercept classes annotated with Transactional, but avoid "double"
        // interception when a mathod is also annotated inside an annotated
        // class.
        bindInterceptor(annotatedWith(Transactional.class),
                not(SYNTHETIC).and(not(DECLARED_BY_OBJECT)).and(not(annotatedWith(Transactional.class))),
                interceptor);
    }

    /**
     *
     * @param mapperType
     */
    protected final <T> void bindMapper(Class<T> mapperType)
    {
        bind(mapperType).toProvider(guicify(new MapperProvider<T>(mapperType))).in(Scopes.SINGLETON);
    }

    /**
     *
     * @return
     * @since 3.3
     */
    public void useResourceClassLoader(ClassLoader resourceClassLoader)
    {
        this.resourcesClassLoader = resourceClassLoader;
    }

    /**
     *
     * @return
     * @since 3.3
     */
    protected final ClassLoader getResourceClassLoader()
    {
        return resourcesClassLoader;
    }

    /**
     *
     * @return
     * @since 3.3
     */
    public void useJdbcDriverClassLoader(ClassLoader driverClassLoader)
    {
        this.driverClassLoader = driverClassLoader;
    }

    /**
     *
     * @return
     * @since 3.3
     */
    private ClassLoader getDefaultClassLoader()
    {
        return getClass().getClassLoader();
    }

    /**
     * Configures a {@link Binder} via the exposed methods.
     */
    abstract void internalConfigure();

    /**
     *
     */
    protected abstract void initialize();
}
