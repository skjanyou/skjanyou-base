package com.skjanyou.javafx.inter;

import java.beans.PropertyChangeListener;

public interface BeanPropertySupport {
	public BeanPropertySupport addPropertyChangeListener( PropertyChangeListener propertyChangeListener );
	public BeanPropertySupport removePropertyChangeListener( PropertyChangeListener propertyChangeListener );
}
