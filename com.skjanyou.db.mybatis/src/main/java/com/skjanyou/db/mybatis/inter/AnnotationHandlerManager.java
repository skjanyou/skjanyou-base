package com.skjanyou.db.mybatis.inter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.skjanyou.db.mybatis.anno.DDL.Delete;
import com.skjanyou.db.mybatis.anno.DDL.Insert;
import com.skjanyou.db.mybatis.anno.DDL.Select;
import com.skjanyou.db.mybatis.anno.DDL.Update;
import com.skjanyou.db.mybatis.bean.Invocation;
import com.skjanyou.db.mybatis.core.BeanAnnotation;
import com.skjanyou.db.mybatis.inter.impl.DefaultAnnotationDispatchHandler;

public class AnnotationHandlerManager {
	private static AnnotationHandlerManager $this = new AnnotationHandlerManager();
	private static Map<Class<? extends Annotation>,AnnotationHandler<?>> map = new HashMap<>();
	
	static {
		$this.on(Select.class, new DefaultAnnotationDispatchHandler.SelectAnnotationHandler());
		$this.on(Insert.class, new DefaultAnnotationDispatchHandler.InsertAnnotationHander());
		$this.on(Update.class, new DefaultAnnotationDispatchHandler.UpdateAnnotationHander());
		$this.on(Delete.class, new DefaultAnnotationDispatchHandler.DeleteAnnotationHandler());
		
		AnnotationHandler<Annotation> baseHandler = new BeanAnnotation.BeanBaseHandler();
		$this.on(BeanAnnotation.SelectOne.class, baseHandler);
		$this.on(BeanAnnotation.SelectFirst.class, baseHandler);
		$this.on(BeanAnnotation.Insert.class, baseHandler);
		$this.on(BeanAnnotation.Query.class, baseHandler);
		$this.on(BeanAnnotation.QueryPage.class, baseHandler);
		$this.on(BeanAnnotation.Update.class, baseHandler);
	}
	
	private AnnotationHandlerManager(){}
	
	public static AnnotationHandlerManager get(){ return $this; }

	public AnnotationHandlerManager on ( Class<? extends Annotation> clazz,AnnotationHandler<?> handler ) {
		if( map.containsKey(clazz) ){
			throw new RuntimeException("注解" + clazz.getName() + "已存在");
		}
		map.put(clazz, handler);
		return this;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object process( Class<?> mapperClass, Object proxy, Method method,Object[] args ) throws Throwable{
		Annotation[] annos = method.getAnnotations();
		for (Annotation annotation : annos) {
			Class<?> cls = annotation.annotationType();
			AnnotationHandler<?> handler = map.get(cls);
			if( handler != null ){
				return handler.handler(new Invocation(mapperClass,proxy,method,args,annotation));
			}
		}
		
		return method.invoke(proxy, args);
	}
}
