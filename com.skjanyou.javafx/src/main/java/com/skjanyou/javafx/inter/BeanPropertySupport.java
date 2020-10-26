package com.skjanyou.javafx.inter;

import javafx.beans.property.Property;

public interface BeanPropertySupport {
	public<R> Property<R> getPropertyByKey( String key );
}
