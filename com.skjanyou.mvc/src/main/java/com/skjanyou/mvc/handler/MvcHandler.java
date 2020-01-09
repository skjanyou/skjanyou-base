package com.skjanyou.mvc.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
import com.skjanyou.server.api.constant.StatusCode;
import com.skjanyou.server.api.exception.ServerException;
import com.skjanyou.server.api.inter.ServerHandler;
import com.skjanyou.server.simplehttpserver.http.HttpHeaders;
import com.skjanyou.server.simplehttpserver.http.HttpProtocolLv1;
import com.skjanyou.server.simplehttpserver.http.HttpRequest;
import com.skjanyou.server.simplehttpserver.http.HttpResponse;
import com.skjanyou.server.simplehttpserver.http.HttpServerHandler;
import com.skjanyou.server.simplehttpserver.http.HttpResponse.HttpResponseBody;
import com.skjanyou.server.simplehttpserver.http.HttpResponse.HttpResponseLine;
import com.skjanyou.util.ClassUtil;

public class MvcHandler extends HttpServerHandler {
	// url映射
	private Map<String,Context> mappings = new HashMap<>();
	// 扫描的class
	private String scanPath = null;
	
	public MvcHandler( String scanPath ) {
		this.scanPath = scanPath;
	}
	
	
	@Override
	public void handler(HttpRequest request, HttpResponse response) throws ServerException{
		// 响应行
		HttpResponseLine responseLine = (HttpResponseLine) response.responseLine();
		// 响应体
		HttpResponseBody responseBody = (HttpResponseBody) response.responseBody();
		// 响应头
		HttpHeaders httpHeaders = (HttpHeaders) response.headers();
		// 请求url
		String url = request.requestLine().url(); 
		// 查询参数
		Map<String,Object> params = request.requestLine().queryParams();

		responseLine.setProtocol(new HttpProtocolLv1());
		
		Context context = mappings.get(url);
		if( context != null ){
			Method method = context.getTargetMethod();
			Object object = context.getTargetObj();
			Object result = null;
			try {
				method.setAccessible(true);
				result = method.invoke(object);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServerException("方法调用失败" + method,e);
			}
			responseLine.setStatusCode(StatusCode.Ok);
			responseBody.setBodyContent(result.toString());
			httpHeaders.put("Content-Type", "json");
		}else{
			responseLine.setStatusCode(StatusCode.Not_Found);
		}
		
	}	
	
	@Override
	public ServerHandler init() throws ServerException {
		initBean();
		autowired();
		return this;
	}

	
	private void initBean() throws ServerException{
		List<Class<?>> list = ClassUtil.getClasses(this.scanPath);
		for ( Class<?> clazz : list ) {
			// 接口,跳过处理
			if(clazz.isInterface()){ continue; }
			
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

	private void autowired() throws ServerException{
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
						throw new ServerException(fileds + "待装配对象在容器中不存在!");
					}
				}
				
				// 包含Dao
				Dao dao = field.getDeclaredAnnotation(Dao.class);
				if( dao != null ){
					String beanName = dao.value();
					filedObj = beanMap.get(beanName);
					if( filedObj == null ){
						throw new ServerException(fileds + "待装配对象在容器中不存在!");
					}					
				}	
				
				// 自动装配
				Autowired autowired = field.getDeclaredAnnotation(Autowired.class);
				if( autowired != null ){
					String beanName = field.getName();
					filedObj = beanMap.get(beanName);
					if( filedObj == null ){
						throw new ServerException(fileds + "待装配对象在容器中不存在!");
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
