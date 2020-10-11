package com.skjanyou.javafx.core;

import java.lang.reflect.Method;
import java.net.URL;

import com.skjanyou.javafx.anno.FxAnnotation.FxController;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ControllerProxy implements MethodInterceptor {
	private Class<?> controllerClass;
	private FxController fxControllerAnno;
	public ControllerProxy( Class<?> controllerClass ) {
		this.controllerClass = controllerClass;
		this.fxControllerAnno = controllerClass.getAnnotation(FxController.class);
		if( fxControllerAnno == null ) {
			throw new RuntimeException("Controller类上面必须携带FxController注解");
		}
	} 
	
	@SuppressWarnings("unchecked")
	public<T> T createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(controllerClass);
        enhancer.setCallback(this);
        T result = (T) enhancer.create();
        initFxml( result );
		initEventHandler();
		initResponsiveBean();
		
		
		return result;
	} 
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		proxy.invokeSuper(obj, args);
		

		return null;
	}
	
	@SuppressWarnings("restriction")
	protected void initFxml( Object controller ) {
		String id = fxControllerAnno.id();
		String fxml = fxControllerAnno.fxml();
		String css = fxControllerAnno.css();
		String skin = fxControllerAnno.skin();
		String[] styles = fxControllerAnno.styles();

	}
	
	protected void initEventHandler() {
		
	}
	
	protected void initResponsiveBean() {
		
	}

	public URL getFXMLURL() {
		return Thread.currentThread().getContextClassLoader().getResource(fxControllerAnno.fxml());
	}
	
}
