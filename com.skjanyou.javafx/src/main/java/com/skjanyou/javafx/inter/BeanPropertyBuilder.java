package com.skjanyou.javafx.inter;

import java.lang.reflect.Method;

import com.skjanyou.javafx.core.BeanProperty;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;

public abstract class BeanPropertyBuilder {
	protected static final String BIND_METHOD_NAME = "$bind";
	protected static final String UNBIND_METHOD_NAME = "$unbind";
	
	protected Class<?> clazz;
	public BeanPropertyBuilder( Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public static void bind( Object propertyBean,ObservableValue<?> rawObservable ) {
		Class<?> targetClass = propertyBean.getClass();
		try {
			Method method = targetClass.getMethod(BIND_METHOD_NAME, ObservableValue.class);
			
			method.invoke(propertyBean, new Object[] { rawObservable });
			
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("找不到方法" + BIND_METHOD_NAME,e);
		} catch ( Exception e) {
			throw new RuntimeException("绑定数据出错",e);
		}
	}
	
	
	public static void unBind( Object propertyBean ) {
		Class<?> targetClass = propertyBean.getClass();
		try {
			Method method = targetClass.getMethod(UNBIND_METHOD_NAME);
			
			method.invoke(propertyBean, new Object[] {  });
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("找不到方法" + UNBIND_METHOD_NAME,e);
		} catch ( Exception e) {
			throw new RuntimeException("解绑定数据出错",e);
		}
	}
	
	/**
	 * 通过Key去搜索类里面的property属性进行绑定
	 * @param propertyBean
	 * @param property
	 */
	public static void bind( Object propertyBean,String propertyName, Property<?> property ) {
		Class<?> targetClass = propertyBean.getClass();
		try {
			Method method = targetClass.getMethod(BIND_METHOD_NAME, String.class,Property.class);
			method.invoke(propertyBean, new Object[] { propertyName,property });
			
		} catch (SecurityException e) {
			throw new RuntimeException("找不到方法" + BIND_METHOD_NAME,e);
		} catch ( Exception e) {
			throw new RuntimeException("绑定数据出错",e);
		}
	}

	public abstract BeanProperty builder();
	
}
