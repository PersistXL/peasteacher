package com.peas.hsf.hk2;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Created by duanyihui on 2016/12/5.
 */
public class HK2Binding extends AbstractBinder
{

    private String packageName;

    private HK2Binding(String packageName)
    {
        this.packageName = packageName;
    }

    @Override
    protected void configure()
    {
        DIRuntimeInitializer.initHK2(this, this.packageName);
    }

    public static HK2Binding build(String packageName)
    {
        return new HK2Binding(packageName);
    }
}
