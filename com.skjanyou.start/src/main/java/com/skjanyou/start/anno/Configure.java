package com.skjanyou.start.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.skjanyou.start.config.ConfigManagerFactory;
import com.skjanyou.start.config.impl.PropertiesConfig;


@Target( { ElementType.TYPE } )
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configure {
	String name();
	Class<? extends ConfigManagerFactory> configManagerFactory() default PropertiesConfig.class;
	String[] scanPath() default "";
}
