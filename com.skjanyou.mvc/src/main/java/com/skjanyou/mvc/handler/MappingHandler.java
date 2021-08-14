package com.skjanyou.mvc.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.skjanyou.db.plugin.DBPlugin;
import com.skjanyou.db.pool.DataSource;
import com.skjanyou.mvc.anno.Mvc.Autowired;
import com.skjanyou.mvc.anno.Mvc.Controller;
import com.skjanyou.mvc.anno.Mvc.Dao;
import com.skjanyou.mvc.anno.Mvc.HandlerException;
import com.skjanyou.mvc.anno.Mvc.HandlerException.ExceptionHandler;
import com.skjanyou.mvc.anno.Mvc.Mapping;
import com.skjanyou.mvc.anno.Mvc.Service;
import com.skjanyou.mvc.anno.Mvc.Transactional;
import com.skjanyou.mvc.bean.Context;
import com.skjanyou.mvc.core.MvcApplicateContext;
import com.skjanyou.mvc.core.MvcContext;
import com.skjanyou.server.api.exception.ServerException;
import com.skjanyou.server.api.inter.ServerHandler;
import com.skjanyou.server.core.HttpServerHandler;
import com.skjanyou.util.ClassUtil;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
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
	
	/** 处理类 **/
	protected void resolveClass( Class<?> clazz) throws ServerException {
		// 接口,跳过处理
		if(clazz.isInterface()){ return; }
		
		try{
			// 包含Service
			Service service = clazz.getDeclaredAnnotation(Service.class);
			if( service != null ){
				Object object = null;
				Transactional tsl = clazz.getDeclaredAnnotation(Transactional.class);
				if( tsl == null ) {
					
					// 使用cglib代理
					Enhancer enhancer = new Enhancer();
					enhancer.setSuperclass(clazz);
					MethodInterceptor callback = new MethodInterceptor() {
						@Override
						public Object intercept(Object obj, Method paramMethod, Object[] args,
								MethodProxy proxy) throws Throwable {
							String name = paramMethod.getName();
							if( name.equals("getClass") || name.equals("equals") || name.equals("hashCode") ) {
								return proxy.invokeSuper(obj, args);
							}
							Object result = null;
							DataSource dataSource = DBPlugin.getDefaultDataSourceManager().getDataSource();
							try {
								MvcContext context = new MvcContext();
								context.putContext("CUR_DS", dataSource);
								MvcContext.set(context);
								dataSource.beginTransaction();
								// 开启事务
								result = proxy.invokeSuper(obj, args);
								// 提交事务
								dataSource.commit();
							} catch( Exception e ) {
								e.printStackTrace();
								// 回滚事务
								dataSource.rollback();
								throw e;
							}
							
							return result;
						}
					};
					enhancer.setCallback(callback);
					object = enhancer.create();
				}else if( tsl.level() == Transactional.Level.NONE ){
					object = clazz.newInstance();
				}
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
				// 方法上面的异常不获取
				ExceptionHandler<?> classHandler = null;
				List<Class<? extends Exception>> classList = null;
				HandlerException classHandlerException = clazz.getDeclaredAnnotation(HandlerException.class);
				if( classHandlerException != null ) {
					classHandler = classHandlerException.handler().newInstance();
					classList = Arrays.asList(classHandlerException.exception());
				}
				
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
						md.setAccessible(true);
						
						// 查找方法上面的异常捕获器
						ExceptionHandler<?> methodHandler = null;
						List<Class<? extends Exception>> methodList = null;
						HandlerException methodHandlerException = md.getDeclaredAnnotation(HandlerException.class);
						if( methodHandlerException != null ) {
							methodHandler = methodHandlerException.handler().newInstance();
							methodList = Arrays.asList(classHandlerException.exception());
						}
						
						context = new Context(object, clazz, md);
						if( methodHandler != null ) {
							context.setHandler(methodHandler);
							context.setExceptionList(classList);
						}else if( classHandler != null ){
							context.setHandler(classHandler);
							context.setExceptionList(methodList);
						}
						mappings.put(key, context);
					}
				}
			}
		}catch( Exception e ){
			throw new ServerException("初始化Bean[" + clazz.getName() + "]失败",e);
		}
	}
	
	protected Class<?> getTargetClass( Object obj ){
		Class<?> clazz = obj.getClass();
		if( !Proxy.isProxyClass(clazz) && !ClassUtil.isCglibProxyClassName(clazz.getName()) ) {
			return clazz;
		}else {
			Class<?> su = clazz.getSuperclass();
			if( su != Object.class) {
				return su;
			}
			try {
				Field h = clazz.getDeclaredField("CGLIB$CALLBACK_0");
				h.setAccessible(true);
				
				Object dynamicAdvisedInterceptor = h.get(obj);
				Field targetClassField = dynamicAdvisedInterceptor.getClass().getDeclaredField("targetClass");
				targetClassField.setAccessible(true);
				return (Class<?>) targetClassField.get(dynamicAdvisedInterceptor);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
	}
	
	// 自动注入
	protected void autowired() throws ServerException{
		HashSet<?> beanSet = MvcApplicateContext.getAllBeans();
		Field[] fileds = null;
		for (Object obj : beanSet) {
			Class<?> targetClass = getTargetClass(obj);
			fileds = targetClass.getDeclaredFields();
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
	
}
