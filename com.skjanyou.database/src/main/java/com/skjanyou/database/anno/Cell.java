package com.skjanyou.database.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 与数据库中的字段进行映射的字段
 * @author skjanyou
 * 时间 : 2018年7月2日
 * 作用 :
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cell {
	/**
	 * 单元格名字
	 * @return
	 */
	String alias() default "";
	
	String type() default "";
}
