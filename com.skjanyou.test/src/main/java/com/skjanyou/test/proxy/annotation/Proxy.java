package com.skjanyou.test.proxy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.skjanyou.test.proxy.fun.ProxyHandle;



@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented	
public @interface Proxy {
	Class<? extends ProxyHandle> value() default ProxyHandle.class;
}
