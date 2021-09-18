package com.skjanyou.test.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.impl.PropertiesConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(SkjanyouTestExtension.class)
@Configure(name = "Default 配置",scanPath = "com.skjanyou.dbsync",configManagerFactory = PropertiesConfig.class)
public @interface SkjanyouStartTest {
	String[] property() default { "skjanyou.configfile=classpath:Junit5.properties" };
	String[] args() default { "start" };
}
