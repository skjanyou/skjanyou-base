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
			String name = beanObject.getClass().getName();
					
			if( beanObject.getClass().getName().indexOf(CGLIB) != -1 ) {
				this.wrapper = new CglibBeanWrapper(beanObject);
			}else {
				this.wrapper = new BeanWrapper0(beanObject);
			}
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
		
		private void resolveField() {
			Iterator<String> it = fields.iterator();
			while( it.hasNext() ) {
				String fieldName = it.next();
				try {
					Field field = this.targetClass.getDeclaredField(fieldName);
					if( field != null ) {
						this.fieldList.add(field);
					}else {
						it.remove();
					}
				} catch (NoSuchFieldException | SecurityException e) {
					it.remove();
					continue;
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
		
		@Override
		public Object getByField(String fieldName) throws NoSuchFieldException,IllegalArgumentException, IllegalAccessException {
			String lowerCase = fieldName.toLowerCase();
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
				result = field.get(this.targetBean);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			return result;
		}
		
		@Override
		public BeanWrapper setByField(String fieldName, Object value) throws NoSuchFieldException,IllegalArgumentException, IllegalAccessException {
			String lowerCase = fieldName.toLowerCase();
			int index = fields.indexOf(lowerCase);
			if( index == -1 ) {
				throw new NoSuchFieldException(lowerCase);
			}
			
			Field field = fieldList.get(index);
			if( field == null ) {
				throw new NoSuchFieldException(lowerCase);
			}
			field.set(this.targetBean, value);
			
			return this;
		}
		
		public List<String> getAllField(){
			return this.fields;
		}
	}

	private class CglibBeanWrapper extends BeanWrapper0 {
		private Class<?> cglibClass;
		private List<String> cglibField;
		private List<Field> cglibFieldList;
		private final static String CGLIB_PREFIX = "$cglib_prop_";
		
		public CglibBeanWrapper(Object beanObject) {
			super(beanObject);
			this.cglibClass = beanObject.getClass();
			this.cglibField = new ArrayList<>();
			this.cglibFieldList = new ArrayList<>();
			this.resolveCglibAttr();
		}
		
		/**
		 *	代理类,先获取原类,再后面添加代理类的属性获取
		 */
		@Override
		protected Class<?> getBeanClass() {
			Class<?> resultClass = super.getBeanClass();
			while( resultClass.getSuperclass() != null ) {
				resultClass = resultClass.getSuperclass();
				if( resultClass.getName().indexOf(CGLIB) == -1 ) {
					break;
				}
			}
			
			return resultClass;
		}
		
		protected void resolveCglibAttr() {
			Field[] fields = this.cglibClass.getSuperclass().getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				this.cglibField.add(fieldName);
				this.cglibFieldList.add(field);
			}
		}
		
		@Override
		public BeanWrapper setByField(String fieldName, Object value) throws NoSuchFieldException,IllegalArgumentException, IllegalAccessException {
			try {
				super.setByField(fieldName, value);
			} catch( NoSuchFieldException | IllegalArgumentException | IllegalAccessException e ) {
				String lowerCase = CGLIB_PREFIX + fieldName.toLowerCase();
				int index = this.cglibField.indexOf(lowerCase);
				if( index == -1 ) {
					throw new IllegalAccessException(lowerCase);
				}
				
				Field field = this.cglibFieldList.get(index);
				if( field == null ) {
					throw new IllegalAccessException(lowerCase);
				}
				field.setAccessible(true);
				field.set(this.targetBean, value);
			}
			
			
			return this;
		}
		
		@Override
		public Object getByField(String fieldName) throws NoSuchFieldException,IllegalArgumentException, IllegalAccessException {
			Object result = null;
			try {
				result = super.getByField(fieldName);
			} catch ( NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				String lowerCase = CGLIB_PREFIX + fieldName.toLowerCase();
				int index = this.cglibField.indexOf(lowerCase);
				if( index == -1 ) {
					throw new IllegalAccessException(lowerCase);
				}
				
				Field field = this.cglibFieldList.get(index);
				if( field == null ) {
					throw new IllegalAccessException(lowerCase);
				}
				field.setAccessible(true);
				result = field.get(this.targetBean);
			}
			
			return result;
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
