package com.skjanyou.mvc.handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skjanyou.mvc.anno.Mvc.Controller;
import com.skjanyou.mvc.anno.Mvc.Dao;
import com.skjanyou.mvc.anno.Mvc.Mapping;
import com.skjanyou.mvc.anno.Mvc.Service;
import com.skjanyou.server.inter.Request;
import com.skjanyou.server.inter.Response;
import com.skjanyou.server.inter.ServerHandler;
import com.skjanyou.util.ClassUtil;

public class MvcHandler implements ServerHandler {
	// url映射
	private Map<String,Method> mappings = new HashMap<>();
	// 扫描的class
	private String scanPath = null;
	// bean容器
	private Map<String,Object> beans = new HashMap<>();
	
	public MvcHandler( String scanPath ) {
		this.scanPath = scanPath;
	}
	
	
	@Override
	public boolean handler(Request request, Response response) {
		String url = request.requestLine().url(); 
		System.out.println(url);
		Method method = mappings.get(url);
		
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
			
			// 包含Controller注解
			Controller controller = clazz.getDeclaredAnnotation(Controller.class);
			if( controller != null ){
				String namespace = controller.value();
				
				Method[] methods = clazz.getDeclaredMethods();
				Mapping mapping = null;
				for ( Method md : methods ) {
					mapping = md.getDeclaredAnnotation(Mapping.class);
					if( mapping != null ){
						String m = mapping.value();
						String key = (namespace + "/" + m).replaceAll("/+*","/");
						mappings.put(key, md);
					}
				}
				
			}
			
			// 包含Service
			Service service = clazz.getDeclaredAnnotation(Service.class);
			if( service != null ){
				
			}
			
			// 包含Dao
			Dao dao = clazz.getDeclaredAnnotation(Dao.class);
			if( dao != null ){
				
			}
		}
	}

	private void autowired() throws Exception{
		
	}
}
