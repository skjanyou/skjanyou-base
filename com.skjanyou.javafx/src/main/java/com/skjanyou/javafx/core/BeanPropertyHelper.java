package com.skjanyou.javafx.core;

import java.util.HashMap;
import java.util.Map;

import com.skjanyou.javafx.inter.BeanPropertyBuilder;

public class BeanPropertyHelper{
	private BeanPropertyBuilder beanPropertyBuilder;
	private static Map<Object,BeanProperty> objectMapper = new HashMap<>();
	
	public BeanPropertyHelper(Class<?> clazz) {
		this.beanPropertyBuilder = new DefaultBeanPropertyBuilder(clazz);
	}

	public BeanProperty builder() {
		BeanProperty beanProperty = beanPropertyBuilder.builder();
		objectMapper.put(beanProperty.getBean(), beanProperty);
		return beanProperty;
	} 

}
