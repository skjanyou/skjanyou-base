package com.skjanyou.beancontainer.factory;

import java.util.HashSet;

public interface Beandefinition {
	public<T> T getBeanByInterfaceClass(Class<?> clazz);
	public<T> T getBean(String beanName);
	public void setBean(String beanName, Object bean);
	public HashSet<?> getBeans() ;
	public boolean contains(String beanName);
}
