package com.skjanyou.javafx.core;

import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;

import com.skjanyou.javafx.inter.BeanPropertyBuilder;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class DefaultBeanPropertyBuilder extends BeanPropertyBuilder implements MethodInterceptor {
	private BeanProperty beanProperty = null;
	public DefaultBeanPropertyBuilder(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public BeanProperty builder() {
		BeanProperty beanProperty = new BeanProperty();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.clazz);
        enhancer.setCallback(this);
        Object bean = enhancer.create();
        beanProperty.setBean(bean);
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(bean);
        beanProperty.setPropertyChangeSupport(propertyChangeSupport);
        
        this.beanProperty = beanProperty;
		return beanProperty;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object result = null;
		String methodName = method.getName();
		String prefix = methodName.substring(0,3).toLowerCase();
		String field = methodName.substring(3).toLowerCase();
		if( prefix.equals("set") ) {
			String getMethodName = methodName.replace("set", "get");
			Method getMethod = clazz.getMethod(getMethodName);
			Object oldValue = getMethod.invoke(obj);
			result = proxy.invokeSuper(obj, args);
			Object newValue = args[0];
			this.beanProperty.getPropertyChangeSupport().firePropertyChange(field, oldValue, newValue);
		}else {
			proxy.invokeSuper(obj, args);
		}
		
		return result;
	}

}
