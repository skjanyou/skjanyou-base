package com.skjanyou.javafx.inter;

import com.skjanyou.javafx.core.BeanProperty;

public abstract class BeanPropertyBuilder {
	protected Class<?> clazz;
	public BeanPropertyBuilder( Class<?> clazz) {
		this.clazz = clazz;
	}
	public abstract BeanProperty builder();
}
