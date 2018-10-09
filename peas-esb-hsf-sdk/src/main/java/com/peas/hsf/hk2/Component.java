package com.peas.hsf.hk2;

import org.glassfish.hk2.api.ContractIndicator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by duanyihui on 2017/3/24.
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE})
@ContractIndicator
public @interface Component
{
}
