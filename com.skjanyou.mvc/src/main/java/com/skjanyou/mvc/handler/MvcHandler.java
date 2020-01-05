package com.skjanyou.mvc.handler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.skjanyou.mvc.anno.Mvc.Autowired;
import com.skjanyou.mvc.anno.Mvc.Controller;
import com.skjanyou.mvc.anno.Mvc.Dao;
import com.skjanyou.mvc.anno.Mvc.Mapping;
import com.skjanyou.mvc.anno.Mvc.Service;
import com.skjanyou.mvc.bean.Context;
import com.skjanyou.mvc.core.MvcApplicateContext;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.api.inter.ServerHandler;
import com.skjanyou.util.ClassUtil;

public class MvcHandler implements ServerHandler {
	// url映射
	private Map<String,Context> mappings = new HashMap<>();
	// 扫描的class
	private String scanPath = null;
	
	public MvcHandler( String scanPath ) {
		this.scanPath = scanPath;
	}
	
	
	@Override
	public boolean handler(Request request, Response response) {
		String url = request.requestLine().url(); 
		Map<String,Object> params = request.requestLine().queryParams();
		try {
			url = URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		System.out.println(url);
		Context context = mappings.get(url);
		if( context != null ){
			Method method = context.getTargetMethod();
			Object object = context.getTargetObj();
			
			
			try {
				method.invoke(object);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}			
		}
		
		
		return false;
	}	
	
	@Override
	public ServerHandler init() throws Exception {
		initBean();
		autowired();
		return this;
	}

	
	private void initBean() throws Exception{
		List<Class<?>> list = ClassUtil.getClasses(this.scanPath);
		for ( Class<?> clazz : list ) {
			// 接口,跳过处理
			if(clazz.isInterface()){ continue; }
			
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

		}
	}

	private void autowired() throws Exception{
		Map<String,Object> beanMap = MvcApplicateContext.getAllBeans();
		Iterator<Entry<String, Object>> it = beanMap.entrySet().iterator();
		Field[] fileds = null;
		while(it.hasNext()){
			Entry<String, Object> entry = it.next();
			
			Object obj = entry.getValue();
			
			fileds = obj.getClass().getDeclaredFields();
			for (Field field : fileds) {
				field.setAccessible(true);
				Object filedObj = null;
				// 包含Service
				Service service = field.getDeclaredAnnotation(Service.class);
				if( service != null ){
					String beanName = service.value();
					filedObj = beanMap.get(beanName);
					if( filedObj == null ){
						throw new RuntimeException(fileds + "待装配对象在容器中不存在!");
					}
				}
				
				// 包含Dao
				Dao dao = field.getDeclaredAnnotation(Dao.class);
				if( dao != null ){
					String beanName = dao.value();
					filedObj = beanMap.get(beanName);
					if( filedObj == null ){
						throw new RuntimeException(fileds + "待装配对象在容器中不存在!");
					}					
				}	
				
				// 自动装配
				Autowired autowired = field.getDeclaredAnnotation(Autowired.class);
				if( autowired != null ){
					String beanName = field.getName();
					filedObj = beanMap.get(beanName);
					if( filedObj == null ){
						throw new RuntimeException(fileds + "待装配对象在容器中不存在!");
					}						
				}
				
				if( filedObj != null ){
					field.set(obj, filedObj);
				}
			}
			
		}
	}
}
