package com.skjanyou.javafx.inter.impl;

import com.skjanyou.javafx.anno.FxAnnotation.FxController;
import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.FxControllerFactoryProperty;
import com.skjanyou.javafx.inter.FxEventDispatcher;
import com.skjanyou.javafx.inter.FxFXMLLoader;

import javafx.event.Event;
import javafx.scene.Parent;

public class DefaultFxControllerFactory implements FxControllerFactory,FxControllerFactoryProperty {
	private FxEventDispatcher eventDispatcher;
	private FxFXMLLoader fxmlLoader;
	private Class<?> controllerClass;
	private FxController fxControllerAnno;
	private Object proxyController;
	private Parent parent;
	
	public DefaultFxControllerFactory( Class<?> controllerClass ) {
		this.controllerClass = controllerClass;
		this.fxControllerAnno = controllerClass.getAnnotation(FxController.class);
		if( fxControllerAnno == null ) {
			throw new RuntimeException("Controller类上面必须携带FxController注解");
		}
	}
	
	@Override
	public FxEventDispatcher getFxEventDispatcher() {
		return this.eventDispatcher = this.eventDispatcher == null ? new DefaultFxEventDispatcher() : this.eventDispatcher;
	}


	@Override
	public FxFXMLLoader getFxFXMLLoader() {
		return this.fxmlLoader = this.fxmlLoader == null ? new DefaultFxFXMLLoader( this.controllerClass ) : this.fxmlLoader;
	}

	@SuppressWarnings("unchecked")
	@Override
	public<R> R createController() {
		R result = null;
		if( this.proxyController == null ) {
			LoadResult loadResult = initControllerBean();
			this.parent = loadResult.getParent();
			this.proxyController = loadResult.getController();
			initFxml( this.proxyController,this.parent );
			initEventHandler( this.proxyController,this.parent );
			initResponsiveBean( this.proxyController,this.parent );
		} else {
			result = (R) proxyController;
		}
		return result;
	}

	protected LoadResult initControllerBean() {
		FxFXMLLoader fxmlLoader = getFxFXMLLoader();
		return fxmlLoader.loader();
	}
	
	protected void initFxml(Object controller,Parent parent) {
		
	}
	
	protected void initEventHandler(Object controller,Parent parent) {
		FxEventDispatcher eventDispatcher = getFxEventDispatcher();
		parent.addEventFilter(Event.ANY, eventDispatcher.getEventHandler());
	}
	
	protected void initResponsiveBean(Object controller,Parent parent) {
		
	}


}
