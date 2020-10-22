package com.skjanyou.javafx.core;

import java.beans.PropertyChangeSupport;

public class BeanProperty {
	private Object bean;
	private PropertyChangeSupport propertyChangeSupport;
	public Object getBean() {
		return bean;
	}
	public void setBean(Object bean) {
		this.bean = bean;
	}
	public PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}
	public void setPropertyChangeSupport(PropertyChangeSupport propertyChangeSupport) {
		this.propertyChangeSupport = propertyChangeSupport;
	}
	
}
