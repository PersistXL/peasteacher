package com.peas.hsf.hk2;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jvnet.hk2.annotations.InhabitantAnnotation;

/**
 * Created by duanyihui on 2016/12/7.
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
@InhabitantAnnotation("default")
public @interface Service
{
}
