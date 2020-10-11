package com.skjanyou.javafx.core;

import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;

public class SkjanyouFXMLLoader extends FXMLLoader implements Callback<Class<?>, Object>{
	/** **/
	private SkjanyouController skjanyouController;
	/** **/
	private ControllerProxy controllerProxy;
	/** **/
	private DoubleEventDispatcher eventDispatcher;
	private Class<?> controllerClass;
	private Object proxyObject;
	
	
	public SkjanyouFXMLLoader( Class<?> controllerClass ) {
		this.controllerClass = controllerClass;
		this.controllerProxy = new ControllerProxy(controllerClass);
		this.proxyObject = controllerProxy.createProxy();
		this.skjanyouController = new SkjanyouController(proxyObject, controllerClass);
		
		this.setLocation(this.controllerProxy.getFXMLURL());
//		this.setRoot(proxyObject);
		this.eventDispatcher = new DoubleEventDispatcher(skjanyouController);
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
			return new ControllerProxy(controllerClass).createProxy();
		}
		return proxyObject;
	}

	public DoubleEventDispatcher getEventDispatcher() {
		return eventDispatcher;
	}

	public SkjanyouController getSkjanyouController() {
		return skjanyouController;
	}

	public Object getProxyObject() {
		return proxyObject;
	}
	
	
}
