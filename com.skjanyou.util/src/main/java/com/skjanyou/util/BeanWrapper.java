package com.skjanyou.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BeanWrapper {
	protected static final String CGLIB = "$$";
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
	
	public Object getByField( String fieldName ) throws NoSuchFieldException,IllegalArgumentException, IllegalAccessException {
		return this.wrapper.getByField(fieldName);
	}
	
	public BeanWrapper setByField( String fieldName,Object value ) throws NoSuchFieldException,IllegalArgumentException, IllegalAccessException {
		this.wrapper.setByField(fieldName, value);
		return this;
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
		protected Object targetBean;
		protected Class<?> targetClass;
		protected boolean isCglib;
		protected final String CGLIB_PREFIX = "$cglib_prop_"; 
		private List<String> fields;
		private List<Field> fieldList;
		private Map<String,Method> getter;
		private Map<String,Method> setter;
		
		public BeanWrapper0( Object beanObject ){
			if( beanObject == null ){
				throw new NullPointerException("Bean不能为空");
			}
			this.targetBean = beanObject;
			this.targetClass = getBeanClass();
			if( this.targetClass.getName().contains(CGLIB) ) {
				this.isCglib = true;
			}
			
			this.getter = new HashMap<>();
			this.setter = new HashMap<>();
			this.fields = new ArrayList<>();
			this.fieldList = new ArrayList<>();
			this.resolveSetter();
			this.resolveGetter();
			this.resolveField();
		}
		
		protected Class<?> getBeanClass(){
			return this.targetBean.getClass();
		}
		

		private void resolveSetter(){
			Method[] methods = getClassMethods();
			for (Method method : methods) {
				String name = method.getName();
				if (name.startsWith("set") && name.length() > 3) {
					if (method.getParameterCount() == 1) {
						name = name.substring(3).toLowerCase();
						setter.put(name, method);
					}
				}else if( name.startsWith("is") && name.length() > 2 ){
					if( method.getParameterCount() == 1 ){
						name = name.substring(2).toLowerCase();
						setter.put(name, method);
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
		
		private void resolveField() {
			List<Field> fieldList = new ArrayList<>();
			Class<?> resolveClass = this.targetClass;
			List<Field> subField = null;
			do {
				subField = getClassFieldsByClass(resolveClass);
				fieldList.addAll(subField);
			} while( ( null != this.targetClass.getSuperclass() ) && ( resolveClass != this.targetClass.getSuperclass() ) && (resolveClass = this.targetClass.getSuperclass()) != Object.class );
			
			for (Field field : subField) {
				this.fieldList.add(field);
				this.fields.add(field.getName());
			}
		}

		public Object getTargetBean() {
			return targetBean;
		}

		public Class<?> getTargetClass() {
			return targetClass;
		}

		private Method[] getClassMethods(){
			List<Method> methodList = new ArrayList<>();
			Class<?> resolveClass = this.targetClass;
			List<Method> subMethod = null;
			do {
				subMethod = getClassMethodsByClass(resolveClass);
				methodList.addAll(subMethod);
			} while( ( null != this.targetClass.getSuperclass() ) && ( resolveClass != this.targetClass.getSuperclass() ) && (resolveClass = this.targetClass.getSuperclass()) != Object.class );
			
			return methodList.toArray(new Method[] {});
		}
		
		private List<Method> getClassMethodsByClass( Class<?> clazz ){
			Method[] methods = clazz.getDeclaredMethods();
			List<Method> methodList = new ArrayList<>();
			for (Method method : methods) {
				methodList.add(method);
			}
			return methodList;
		}
		
		private List<Field> getClassFieldsByClass( Class<?> clazz ) {
			List<Field> fieldList = new ArrayList<>();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				fieldList.add(field);
			}
			return fieldList;
		}
		
		public Object get( String fieldName ) throws InvocationTargetException,NoSuchMethodException{
			String lowerCase = fieldName.toLowerCase();
			Method method = getter.get(lowerCase);
			if( method == null ){
				throw new NoSuchMethodException("找不到" + fieldName + "对应的get方法");
			}
			
			Object result = null;
			try {
				method.setAccessible(true);
				result = method.invoke(this.targetBean);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				throw new InvocationTargetException(e);
			}
			
			return result;
		}

		public BeanWrapper set( String fieldName,Object value ) throws InvocationTargetException,NoSuchMethodException{
			String lowerCase = fieldName.toLowerCase();
			Method method = setter.get(lowerCase);
			if( method == null ){
				throw new NoSuchMethodException("找不到" + fieldName + "对应的set方法");
			}
			
			try {
				method.setAccessible(true);
				method.invoke(this.targetBean,value);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				throw new InvocationTargetException(e);
			}
			
			return this;
		}	
		
		@Override
		public Object getByField(String fieldName) throws NoSuchFieldException,IllegalArgumentException, IllegalAccessException {
			String lowerCase = fieldName.toLowerCase();
			if( this.isCglib ){
				lowerCase = this.CGLIB_PREFIX + lowerCase;
			}
			
			int index = fields.indexOf(lowerCase);
			if( index == -1 ) {
				throw new NoSuchFieldException(lowerCase);
			}
			
			Field field = fieldList.get(index);
			if( field == null ) {
				throw new NoSuchFieldException(lowerCase);
			}
			Object result = null;
			try {
				field.setAccessible(true);
				result = field.get(this.targetBean);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			return result;
		}
		
		@Override
		public BeanWrapper setByField(String fieldName, Object value) throws NoSuchFieldException,IllegalArgumentException, IllegalAccessException {
			String lowerCase = fieldName.toLowerCase();
			if( this.isCglib ){
				lowerCase = this.CGLIB_PREFIX + lowerCase;
			}			
			int index = fields.indexOf(lowerCase);
			if( index == -1 ) {
				throw new NoSuchFieldException(lowerCase);
			}
			
			Field field = fieldList.get(index);
			if( field == null ) {
				throw new NoSuchFieldException(lowerCase);
			}
			field.setAccessible(true);
			field.set(this.targetBean, value);
			
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
		private Object object;
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
			this.beanObject.put(fieldName, value);
			return this;
		}

		@Override
		public Object getByField(String fieldName) throws NoSuchFieldException,NoSuchFieldException{
			Object result = null;
			try {
				result = this.get(fieldName);
			} catch (Exception e) {
				throw new NoSuchFieldException(fieldName);
			}
			return result;
		}
		
		@Override
		public BeanWrapper setByField(String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException{
			try {
				this.set(fieldName, value);
			} catch (Exception e) {
				throw new IllegalArgumentException(fieldName);
			}
			
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
