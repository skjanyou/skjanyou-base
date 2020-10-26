package com.skjanyou.javafx.inter.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.Set;

import com.skjanyou.javafx.anno.FxAnnotation.FxController;
import com.skjanyou.javafx.anno.FxAnnotation.ResponsiveBean;
import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.core.BeanProperty;
import com.skjanyou.javafx.core.BeanPropertyHelper;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.FxControllerFactoryProperty;
import com.skjanyou.javafx.inter.FxEventDispatcher;
import com.skjanyou.javafx.inter.FxFXMLLoader;
import com.sun.javafx.fxml.BeanAdapter;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;

public class DefaultFxControllerFactory implements FxControllerFactory,FxControllerFactoryProperty {
	private FxEventDispatcher eventDispatcher;
	private FxFXMLLoader fxmlLoader;
	private Class<?> controllerClass;
	private FxController fxControllerAnno;
	private Object proxyController;
	private Parent parent;
	private LoadResult loadResult;
	
	public DefaultFxControllerFactory( Class<?> controllerClass ) {
		this.controllerClass = controllerClass;
		this.fxControllerAnno = controllerClass.getAnnotation(FxController.class);
		if( fxControllerAnno == null ) {
			throw new RuntimeException("Controller类上面必须携带FxController注解");
		}
	}
	
	@Override
	public FxEventDispatcher getFxEventDispatcher() {
		return this.eventDispatcher = this.eventDispatcher == null ? new DefaultFxEventDispatcher(this.proxyController,this.controllerClass) : this.eventDispatcher;
	}


	@Override
	public FxFXMLLoader getFxFXMLLoader() {
		return this.fxmlLoader = this.fxmlLoader == null ? new DefaultFxFXMLLoader( this.controllerClass ) : this.fxmlLoader;
	}

	@Override
	public LoadResult createController() {
		if( this.loadResult == null ) {
			this.loadResult = initControllerBean();
			this.parent = loadResult.getParent();
			this.proxyController = loadResult.getController();
			EventHandler<Event> handler = getFxEventDispatcher().getEventHandler();
			this.parent.addEventFilter(Event.ANY, handler);
			
			
			initFxml( this.proxyController,this.parent );
			initEventHandler( this.proxyController,this.parent );
			initResponsiveBean( this.proxyController,this.parent );
		} 
		return this.loadResult;
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
		Field[] fields = this.controllerClass.getDeclaredFields();
		for (Field field : fields) {
			ResponsiveBean respBean = field.getAnnotation(ResponsiveBean.class);
			if( respBean != null ) {
				String[] binds = respBean.value();
				Class<?> clazz = field.getType();
				

				BeanProperty beanProperty = new BeanPropertyHelper(clazz).builder();
				try {
					field.setAccessible(true);
					field.set(controller, beanProperty.getBean());
					for (String selector : binds) {
						Set<Node> set = parent.lookupAll(selector);
						for( Node node : set ) {
							System.out.println(node);
							BeanAdapter beanAdapter = new BeanAdapter(node);
							
							ObservableValue value = beanAdapter.getPropertyModel("text");
							Property p = (Property) value;
							System.out.println(p);
							p.addListener(new InvalidationListener() {
								
								@Override
								public void invalidated(Observable observable) {
									System.out.println("11");
								}
							});
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
