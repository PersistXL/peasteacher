package com.peas.hsf.hk2;

import com.google.common.collect.Sets;
import com.google.inject.Binder;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder;
import org.reflections.Reflections;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by duanyihui on 2016/12/6.
 */
public class DIRuntimeInitializer
{

    public static void initHK2(AbstractBinder binder, String pack)
    {
        Set<AnnotationInfo> infos = getAnnotationInfos(pack);
        infos.forEach(info ->
        {
            ServiceBindingBuilder<?> builder = binder.bind(info.getImplClass()).to(info.getSuperClass());
            if (info.getSingletonScope() != null)
            {
                builder.in(info.getSingletonScope());
            }
            if (info.getNamed() != null)
            {
                builder.named(info.getNamed().value());
            }
        });
    }

    public static void initGuice(Binder binder, String packageName)
    {
        Set<AnnotationInfo> infos = getAnnotationInfos(packageName);
        infos.forEach(info ->
        {
            AnnotatedBindingBuilder ab = binder.bind(info.getSuperClass());
            if (info.getImplClass() != null)
            {
                if (info.getNamed() != null)
                {
                    ab.annotatedWith(Names.named(info.getNamed().value())).to(info.getImplClass());
                }
                else
                {
                    ab.to(info.getImplClass());
                }
            }
            else
            {
                if (info.getNamed() != null)
                {
                    ab.annotatedWith(Names.named(info.getNamed().value()));
                }
            }
            if (info.getSingletonScope() != null)
            {
                ab.in(info.getSingletonScope().annotationType());
            }
        });
    }

    private static Set<AnnotationInfo> getAnnotationInfos(String pack)
    {
        Set<AnnotationInfo> entities = Sets.newHashSet();
        Reflections reflections = new Reflections(pack);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> contracts = reflections.getTypesAnnotatedWith(Contract.class);
        for (Class<?> service : services)
        {
            for (Class<?> contract : contracts)
            {
                if (contract.isAssignableFrom(service) && contract.isInterface())
                {
                    AnnotationInfo info = new AnnotationInfo();
                    info.setImplClass(service);
                    info.setSuperClass(contract);
                    Singleton singleton = service.getAnnotation(Singleton.class);
                    info.setSingletonScope(singleton);
                    Named named = service.getAnnotation(Named.class);
                    info.setNamed(named);
                    entities.add(info);
                    break;
                }
            }
        }

        Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);
        components.forEach(component ->
        {
            AnnotationInfo info = new AnnotationInfo();
            info.setImplClass(null);
            info.setSuperClass(component);
            Singleton singleton = component.getAnnotation(Singleton.class);
            info.setSingletonScope(singleton);
            Named named = component.getAnnotation(Named.class);
            info.setNamed(named);
            entities.add(info);
        });
        return entities;
    }
}
