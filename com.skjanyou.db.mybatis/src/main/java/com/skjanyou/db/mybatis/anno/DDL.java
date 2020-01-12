package com.skjanyou.db.mybatis.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.skjanyou.db.mybatis.inter.SqlExceptionProcess;
import com.skjanyou.db.mybatis.inter.SqlProcess;
import com.skjanyou.db.mybatis.inter.impl.DefaultSqlProcess.BatchInsertSqlProcess;
import com.skjanyou.db.mybatis.inter.impl.DefaultSqlProcess.DeleteSqlProcess;
import com.skjanyou.db.mybatis.inter.impl.DefaultSqlProcess.InsertSqlProcess;
import com.skjanyou.db.mybatis.inter.impl.DefaultSqlProcess.SelectSqlProcess;
import com.skjanyou.db.mybatis.inter.impl.DefaultSqlProcess.UpdateSqlProcess;

public interface DDL {
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Delete {
		public String value() default "";
		public Class<? extends SqlProcess<Delete>> handler() default DeleteSqlProcess.class;
		public Class<? extends SqlExceptionProcess> exception() default DeleteSqlProcess.class;
	}
	
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Update {
		public String value() ;
		public Class<? extends SqlProcess<Update>> handler() default UpdateSqlProcess.class;
		public Class<? extends SqlExceptionProcess> exception() default UpdateSqlProcess.class;
	}
	
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Select {
		public String value() ;
		public Class<? extends SqlProcess<Select>> handler() default SelectSqlProcess.class;
		public Class<? extends SqlExceptionProcess> exception() default SelectSqlProcess.class;
	}
	
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Insert {
		public String value() ;
		public Class<? extends SqlProcess<Insert>> handler() default InsertSqlProcess.class;
		public Class<? extends SqlExceptionProcess> exception() default InsertSqlProcess.class;
	}	
	
	@Documented
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface BatchInsert {
		public String value() ;
		public Class<? extends SqlProcess<BatchInsert>> handler() default BatchInsertSqlProcess.class;
		public Class<? extends SqlExceptionProcess> exception() default BatchInsertSqlProcess.class;
	}	
	
	@Documented
	@Inherited
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface SqlParameter {
		public String value() ;
	}		
}
