package com.skjanyou.javafx.core;

import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;

public class SkjanyouFXMLLoader extends FXMLLoader implements Callback<Class<?>, Object>{
	/** **/
	private ControllerHelper skjanyouController;
	/** **/
	private ControllerMethodInterceptor controllerProxy;
	/** **/
	private EventProxyDispatcher eventDispatcher;
	private Class<?> controllerClass;
	private Object proxyObject;
	
	
	public SkjanyouFXMLLoader( Class<?> controllerClass ) {
		this.controllerClass = controllerClass;
		this.controllerProxy = new ControllerMethodInterceptor(controllerClass);
		this.proxyObject = controllerProxy.createProxy();
		this.skjanyouController = new ControllerHelper(proxyObject, controllerClass);
		
		this.setLocation(this.controllerProxy.getFXMLURL());
//		this.setRoot(proxyObject);
		this.eventDispatcher = new EventProxyDispatcher(skjanyouController);
		this.setControllerFactory(this);
	}
	
	@Override
	public <T> T load() throws IOException {
		T result = super.load();
		if( result != null ) {
			this.skjanyouController.root = (Parent) result;
		}
		return result;
	}
	
	@Override
	public <T> T load(InputStream inputStream) throws IOException {
		T result = super.load(inputStream);
		if( result != null ) {
			this.skjanyouController.root = (Parent) result;
		}
		return result;
	}

	
	
	@Override
	public Object call(Class<?> controllerClass) {
		System.err.println(controllerClass);
		if( this.controllerClass != controllerClass ) { 
			return new ControllerMethodInterceptor(controllerClass).createProxy();
		}
		return proxyObject;
	}

	public EventProxyDispatcher getEventDispatcher() {
		return eventDispatcher;
	}

	public ControllerHelper getSkjanyouController() {
		return skjanyouController;
	}

	public Object getProxyObject() {
		return proxyObject;
	}
	
	
}
