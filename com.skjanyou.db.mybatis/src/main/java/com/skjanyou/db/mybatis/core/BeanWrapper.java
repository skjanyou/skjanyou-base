package com.skjanyou.db.mybatis.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BeanWrapper {
	private BeanWrapper wrapper = null;
	private BeanWrapper() {}
	public BeanWrapper( Object beanObject ){
		if( beanObject == null ){
			throw new NullPointerException("Bean不能为空");
		}
		if( beanObject instanceof Map ) {
			this.wrapper = new MapBeanWrapper(beanObject);
		}else {
			this.wrapper = new BeanWrapper0(beanObject);
		}
	}

	public List<String> getAllField(){
		return this.wrapper.getAllField();
	}
	
	public Object get( String fieldName ) throws InvocationTargetException,NoSuchMethodException{
		return this.wrapper.get(fieldName);
	}

	public BeanWrapper set( String fieldName,Object value ) throws InvocationTargetException,NoSuchMethodException{
		this.wrapper.set(fieldName, value);
		return this;
	}
	
	public Object getTargetBean() {
		return this.wrapper.getTargetBean();
	}

	public Class<?> getTargetClass() {
		return this.wrapper.getTargetClass();
	}
	
	
	private class BeanWrapper0 extends BeanWrapper {
		private Object targetBean;
		private Class<?> targetClass;
		private List<String> fields;
		private Map<String,Method> getter;
		private Map<String,Method> setter;
		
		public BeanWrapper0( Object beanObject ){
			if( beanObject == null ){
				throw new NullPointerException("Bean不能为空");
			}
			this.targetBean = beanObject;
			this.targetClass = beanObject.getClass();
			this.getter = new HashMap<>();
			this.setter = new HashMap<>();
			this.fields = new ArrayList<>();
			this.resolveSetter();
			this.resolveGetter();
		}

		private void resolveSetter(){
			Method[] methods = getClassMethods();
			for (Method method : methods) {
				String name = method.getName();
				if (name.startsWith("set") && name.length() > 3) {
					if (method.getParameterCount() == 1) {
						name = name.substring(3).toLowerCase();
						setter.put(name, method);
						fields.add(name);
					}
				}else if( name.startsWith("is") && name.length() > 2 ){
					if( method.getParameterCount() == 1 ){
						name = name.substring(2).toLowerCase();
						setter.put(name, method);
						fields.add(name);
					}
				}		
			}
		}
		private void resolveGetter(){
			Method[] methods = getClassMethods();
			for (Method method : methods) {
				String name = method.getName();
				if (name.startsWith("get") && name.length() > 3) {
					if (method.getParameterCount() == 0) {
						name = name.substring(3).toLowerCase();
						getter.put(name, method);
					}
				}else if( name.startsWith("is") && name.length() > 2 ){
					if( method.getParameterCount() == 0 ){
						name = name.substring(2).toLowerCase();
						getter.put(name, method);
					}
				}		
			}
		}

		public Object getTargetBean() {
			return targetBean;
		}

		public Class<?> getTargetClass() {
			return targetClass;
		}

		private Method[] getClassMethods(){
			Method[] methods = targetClass.getDeclaredMethods();
			for (Method method : methods) {
				method.setAccessible(true);
			}
			return methods;
		}
		
		public Object get( String fieldName ) throws InvocationTargetException,NoSuchMethodException{
			String lowerCase = fieldName.toLowerCase();
			Method m = getter.get(lowerCase);
			if( m == null ){
				throw new NoSuchMethodException("找不到" + fieldName + "对应的get方法");
			}
			
			Object result = null;
			try {
				result = m.invoke(this.targetBean);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				throw new InvocationTargetException(e);
			}
			
			return result;
		}

		public BeanWrapper set( String fieldName,Object value ) throws InvocationTargetException,NoSuchMethodException{
			String lowerCase = fieldName.toLowerCase();
			Method m = setter.get(lowerCase);
			if( m == null ){
				throw new NoSuchMethodException("找不到" + fieldName + "对应的set方法");
			}
			
			try {
				m.invoke(this.targetBean,value);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				throw new InvocationTargetException(e);
			}
			
			return this;
		}	
		
		public List<String> getAllField(){
			return this.fields;
		}
	}

	private class MapBeanWrapper extends BeanWrapper {
		private Map<String,Object> beanObject;
		private Map<String,Object> ignoreBeanObject;
		private List<String> fields;
		@SuppressWarnings("unchecked")
		public MapBeanWrapper ( Object beanObject ) {
			this.beanObject = (Map<String, Object>) beanObject;
			this.ignoreBeanObject = new HashMap<>();
			this.fields = new ArrayList<>();
			this.resolveMap();
		}
		
		private void resolveMap() {
			Iterator<String> it = this.beanObject.keySet().iterator();
			while( it.hasNext() ) {
				String oldKey = it.next();
				String newKey = oldKey.toLowerCase();
				this.ignoreBeanObject.put(newKey, this.beanObject.get(oldKey));
				this.fields.add(newKey);
			}
		}
		
		@Override
		public Object get(String fieldName) throws InvocationTargetException, NoSuchMethodException {
			String lowcase = fieldName.toLowerCase();
			return this.ignoreBeanObject.get(lowcase);
		}

		@Override
		public BeanWrapper set(String fieldName, Object value) throws InvocationTargetException, NoSuchMethodException {
			String lowcase = fieldName.toLowerCase();
			this.ignoreBeanObject.put(lowcase, value);
			return this;
		}

		@Override
		public Object getTargetBean() {
			return beanObject;
		}

		@Override
		public Class<?> getTargetClass() {
			return beanObject.getClass();
		}
		
		public List<String> getAllField(){
			return this.fields;
		}
		
	}
}
