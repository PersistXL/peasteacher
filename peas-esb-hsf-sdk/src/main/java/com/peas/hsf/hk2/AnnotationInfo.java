package com.peas.hsf.hk2;

import javax.inject.Named;
import javax.inject.Singleton;

import lombok.Data;

/**
 * Created by duanyihui on 2016/12/7.
 */
@Data
public class AnnotationInfo<T>
{

    private Class<T> superClass;

    private Class<? extends T> implClass;

    private Singleton singletonScope;

    private Named named;
}
