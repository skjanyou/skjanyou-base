package com.skjanyou.db.mybatis.anno;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.skjanyou.db.mybatis.anno.DDL.SqlParameter;
import com.skjanyou.db.mybatis.bean.Invocation;
import com.skjanyou.db.mybatis.inter.AnnotationHandler;
import com.skjanyou.db.mybatis.inter.SqlExceptionProcess;
import com.skjanyou.db.mybatis.inter.SqlProcess;
import com.skjanyou.db.mybatis.util.BeanUtil;
import com.skjanyou.db.mybatis.core.SqlSession;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SelectMap {
	public String value() default "";
	public Class<? extends SqlProcess<SelectMap>> handler() default SelectMapProcess.class;
	public Class<? extends SqlExceptionProcess> exception() default SelectMapProcess.class;	
	public static class SelectMapProcess implements SqlProcess<SelectMap>,SqlExceptionProcess {

		@Override
		public Object process(Invocation<SelectMap> pi) {
			SelectMap selectMap = pi.getAnno();
			String sql = selectMap.value();
			Object[] args = pi.getArgs();
	        Method method = pi.getMethod();
			Map<String, Object> beanMap = new HashMap<>();
			// 获取参数个数
	        int len = args.length;
	        // 每个参数上面的注解数组
	        Annotation[][] dbAnnotation = method.getParameterAnnotations();
	        for( int index = 0; index < len; index ++ ) {
	        	Annotation[] parameterAnno = dbAnnotation[index];
	        	SqlParameter sqlParm = hasAnnotation(parameterAnno,SqlParameter.class);
	        	if( sqlParm != null ) {
	        		String key = sqlParm.value();
	        		Object value = args[index];
	        		beanMap.put(key, value);
	        	}
	        }
	        
	        
	        return SqlSession.executeSelectListSql(sql, beanMap, HashMap.class,pi.getDataSourceManager());
		}
		
		@SuppressWarnings("unchecked")
		private static<T> T hasAnnotation( Annotation[] annoArr, Class<? extends Annotation> clazz ){
			T result = null;
			
			for (Annotation annotation : annoArr) {
				if(  clazz.isAssignableFrom(annotation.getClass()) ) {
					result = (T) annotation;
					break;
				}
			}
			
			return result;
		}

		@Override
		public void handlerException(Throwable paramThrowable) {
			throw new RuntimeException(paramThrowable);
		}
		
	}
	
	public static class SelectMapHandler implements AnnotationHandler<SelectMap> {

		@Override
		public Object handler(Invocation<SelectMap> pi) {
			Object result = null;
			SelectMap selectMap = pi.getAnno();
			Class<? extends SqlProcess<SelectMap>> sqlProcessClass = selectMap.handler();
			Class<? extends SqlExceptionProcess> exceptionProcessClass = selectMap.exception();
			SqlProcess<SelectMap> sqlProcess = BeanUtil.getBean(sqlProcessClass);
			SqlExceptionProcess exceptionProcess = BeanUtil.getBean(exceptionProcessClass);
			
			try{
				result = sqlProcess.process(pi);
			}catch(Exception e){
				exceptionProcess.handlerException(e);
			}
			
			return result;
		}
		
	}
	
	
}
