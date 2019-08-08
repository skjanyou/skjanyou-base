package com.skjanyou.util.proxy.process;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.skjanyou.util.proxy.annotation.Proxy;
import com.skjanyou.util.proxy.fun.ProxyHandle;

public class ProxyProcess {
	
	@SuppressWarnings("unchecked")
	public static<T> T create(Class<T> clazz,Class<? extends Proxy> proxy){
		Enhancer enhancer = new Enhancer();
        //设置创建子类的类
        enhancer.setSuperclass(clazz);
        MethodInterceptor callback = new cglibProxy(clazz,proxy);
        enhancer.setCallback(callback);
        return (T) enhancer.create();
	}
	
	private static class cglibProxy implements MethodInterceptor {
		private Class<? extends Proxy> proxy;
		private Class<?> target;
		
		public cglibProxy(Class<?> target,Class<? extends Proxy> proxy){
			this.proxy = proxy;
			this.target = target;
		}
		
		@Override
		public Object intercept(Object obj, Method method, Object[] args,MethodProxy proxy) throws Throwable {
			List<Annotation> list = getAnnotations(target,method);
			
			// 注解前处理
			before(obj, method, args, list);
			//真正操作
			Object object = proxy.invokeSuper(obj, args);
			// 注解后处理
			after(obj, method, args, list);
			return object;
		}
		
		private List<Annotation> getAnnotations(Class<?> clazz,Method method){
			List<Annotation> result = new ArrayList<>();
			
			Annotation[] annotations1 = clazz.getAnnotations();
			Annotation[] annotations2 = method.getAnnotations();
			Annotation[] annotations = new Annotation[annotations1.length + annotations2.length];
			
			System.arraycopy(annotations1, 0, annotations, 0, annotations1.length);
			System.arraycopy(annotations2, 0, annotations, annotations1.length, annotations2.length);
			
			for (Annotation annotation : annotations) {
				
				//判断是否有注解的子注解
				if(annotation.annotationType() == this.proxy){
					result.add(annotation);
				}
			}
			return result;
		}
		
		private void before(Object obj, Method method, Object[] args,List<Annotation> list) throws Throwable{
			for (Annotation annotation : list) {
				// 获取代理注解
				Proxy proxyClazz = (Proxy) annotation;
				ProxyHandle handle = proxyClazz.value().newInstance();
				//前处理
				handle.before(args);
			}
		}
		
		private void after(Object obj, Method method, Object[] args,List<Annotation> list)throws Throwable{
			for (Annotation annotation : list) {
				// 获取代理注解
				Proxy proxyClazz = (Proxy) annotation;
				ProxyHandle handle = proxyClazz.value().newInstance();
				// 后处理
				handle.after(args);
			}
		}
	}
	
	
	
	
}
