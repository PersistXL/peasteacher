package com.peas.hsf.hk2;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.ServiceLocatorProvider;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Created by duanyihui on 2017/3/17.
 */
public class HsfGuiceFeature implements Feature
{
    private Module[] modules;

    private HsfGuiceFeature()
    {
    }

    @Override
    public boolean configure(FeatureContext context)
    {
        ServiceLocator serviceLocator = ServiceLocatorProvider.getServiceLocator(context);
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        Injector injector = Guice.createInjector(modules);
        guiceBridge.bridgeGuiceInjector(injector);
        InstanceContext.addContext(injector);
        return true;
    }

    public void addModules(Module... modules)
    {
        this.modules = modules;
    }

    public static HsfGuiceFeature create(Module... modules)
    {
        HsfGuiceFeature feature = new HsfGuiceFeature();
        feature.addModules(modules);
        return feature;
    }
}
