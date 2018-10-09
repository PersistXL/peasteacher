package com.peas.hsf.hk2;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.glassfish.hk2.api.ContractIndicator;

/**
 * Created by duanyihui on 2016/12/7.
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE })
@ContractIndicator
public @interface Contract
{
}
