package com.skjanyou.mvc.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.skjanyou.mvc.anno.Mvc.Autowired;
import com.skjanyou.mvc.anno.Mvc.Controller;
import com.skjanyou.mvc.anno.Mvc.Dao;
import com.skjanyou.mvc.anno.Mvc.Mapping;
import com.skjanyou.mvc.anno.Mvc.Service;
import com.skjanyou.mvc.bean.Context;
import com.skjanyou.mvc.core.MvcApplicateContext;
import com.skjanyou.server.api.exception.ServerException;
import com.skjanyou.server.api.inter.ServerHandler;
import com.skjanyou.server.simplehttpserver.http.HttpServerHandler;
import com.skjanyou.util.ClassUtil;

public abstract class MappingHandler extends HttpServerHandler{
	// url映射
	protected Map<String,Context> mappings = new HashMap<>();
	// 扫描的class
	private String scanPath = null;
	
	public MappingHandler( String scanPath ){
		this.scanPath = scanPath;
	}
	
	@Override
	public ServerHandler init() throws ServerException {
		this.initBean();
		this.autowired();
		return this;
	}
	
	// 创建bean
	protected void initBean() throws ServerException {
		List<Class<?>> list = ClassUtil.getClasses(this.scanPath);
		for (Class<?> clazz : list) {
			resolveClass(clazz);
		}
	}
	
	protected void autowired() throws ServerException {
		HashSet<?> beanSet = MvcApplicateContext.getAllBeans();
		Field[] fileds = null;
		for (Object obj : beanSet) {
			
			fileds = obj.getClass().getDeclaredFields();
			for (Field field : fileds) {
				field.setAccessible(true);
				Object filedObj = null;
				// 包含Service
				Service service = field.getDeclaredAnnotation(Service.class);
				if( service != null ){
					String beanName = service.value();
					filedObj = MvcApplicateContext.getBean(beanName);
					if( filedObj == null ){
						throw new ServerException(fileds + "待装配对象在容器中不存在!");
					}
				}
				
				// 包含Dao
				Dao dao = field.getDeclaredAnnotation(Dao.class);
				if( dao != null ){
					String beanName = dao.value();
					filedObj = MvcApplicateContext.getBean(beanName);
					if( filedObj == null ){
						throw new ServerException(field + "待装配对象在容器中不存在!");
					}					
				}	
				
				// 自动装配
				Autowired autowired = field.getDeclaredAnnotation(Autowired.class);
				if( autowired != null ){
					filedObj = MvcApplicateContext.getBeanByInterfaceClass(field.getType());
					if( filedObj == null ){
						throw new ServerException(field + "待装配对象在容器中不存在!");
					}						
				}
				
				if( filedObj != null ){
					try {
						field.set(obj, filedObj);
					} catch (Exception e) {
						throw new ServerException("对象[" + obj + "]的成员变量["+ field + "]装配属性失败",e);
					}
				}
			}
		}
	}
	
	// 处理类
	protected void resolveClass( Class<?> clazz ) throws ServerException {
		// 接口,跳过处理
		if(clazz.isInterface()){ return; }
		
		try{
			// 包含Service
			Service service = clazz.getDeclaredAnnotation(Service.class);
			if( service != null ){
				Object object = clazz.newInstance();
				String serviceName = service.value();
				MvcApplicateContext.putBean(serviceName, object);				
			}
			
			// 包含Dao
			Dao dao = clazz.getDeclaredAnnotation(Dao.class);
			if( dao != null ){
				Object object = clazz.newInstance();
				String daoName = dao.value();
				MvcApplicateContext.putBean(daoName, object);
			}			
			
			// 包含Controller注解
			Controller controller = clazz.getDeclaredAnnotation(Controller.class);
			if( controller != null ){
				Object object = clazz.newInstance();
				MvcApplicateContext.putBean(clazz, object);
				// 构建映射关系
				String namespace = controller.value();
				Method[] methods = clazz.getDeclaredMethods();
				Mapping mapping = null;
				Context context = null;
				for ( Method md : methods ) {
					mapping = md.getDeclaredAnnotation(Mapping.class);
					if( mapping != null ){
						String m = mapping.value();
						String key = ("/" + namespace + "/" + m).replaceAll("/+","/");
						context = new Context(object, clazz, md);
						mappings.put(key, context);
					}
				}
			}
		}catch( Exception e ){
			throw new ServerException("初始化Bean[" + clazz.getName() + "]失败",e);
		}
	}
}
