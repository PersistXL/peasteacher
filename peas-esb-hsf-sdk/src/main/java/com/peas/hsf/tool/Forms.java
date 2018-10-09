package com.peas.hsf.tool;

import com.google.common.collect.Lists;
import com.peas.hsf.model.DataMultiPartForm;
import com.peas.hsf.model.UnEncodedForm;

import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * Created by duan on 2015/8/31.
 */
public class Forms
{
    /**
     * 创建@FormParam对应的Entry
     *
     * @return
     */
    public static UnEncodedForm newUnEncodedForm()
    {
        return new UnEncodedForm();
    }

    /**
     * 创建@FormDataParam对应的Entry
     *
     * @return
     */
    public static DataMultiPartForm newDataMultiPartForm()
    {
        return new DataMultiPartForm();
    }


    private static final List<String> efs = Lists.newArrayList("class");

    public static UnEncodedForm from(Object... bean)
    {
        UnEncodedForm form = Forms.newUnEncodedForm();
        from(form, bean);
        return form;
    }

    private static void from(UnEncodedForm form, Object... bean)
    {
        try
        {
            for (Object o : bean)
            {
                Class<?> clz = o.getClass();
                BeanInfo beanInfo = Introspector.getBeanInfo(clz);
                PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor property : pds)
                {
                    String name = property.getName();
                    if (efs.contains(name))
                    {
                        continue;
                    }
                    BeanParam beanParam = clz.getDeclaredField(name).getAnnotation(BeanParam.class);
                    if (beanParam == null)
                    {
                        FormParam formBean = clz.getDeclaredField(name).getAnnotation(FormParam.class);
                        if (formBean == null)
                        {
                            continue;
                        }
                        String value = formBean.value();
                        Object propertyValue = property.getReadMethod().invoke(o);
                        if (propertyValue != null)
                        {
                            form.add(value, property.getReadMethod().invoke(o));
                        }
                    }
                    else
                    {
                        Object value = property.getReadMethod().invoke(o);
                        if (value != null)
                        {
                            from(form, value);
                        }
                    }

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

