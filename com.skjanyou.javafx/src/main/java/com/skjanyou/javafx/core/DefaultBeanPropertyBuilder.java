package com.skjanyou.javafx.core;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.Type;

import com.skjanyou.javafx.inter.BeanPropertyBuilder;
import com.skjanyou.plugin.util.InstanceUtil;
import com.skjanyou.util.BeanWrapper;
import com.skjanyou.util.FieldUtil;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class DefaultBeanPropertyBuilder extends BeanPropertyBuilder implements MethodInterceptor {
	private static final String PROPERTY_SUFFIX = "$property";

	public DefaultBeanPropertyBuilder(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public BeanProperty builder() {
		BeanGenerator gen = new BeanGenerator();
		
		// 添加绑定、解绑方法
		InterfaceMaker im = new InterfaceMaker();
		im.add(new Signature(BIND_METHOD_NAME, Type.VOID_TYPE,new Type[] { Type.getType(String.class),Type.getType(Property.class) }), null);
		im.add(new Signature(UNBIND_METHOD_NAME, Type.VOID_TYPE,new Type[] { Type.getType(String.class) }), null);
		Class<?> bindClass = im.create(); 
		
		gen.setSuperclass(this.clazz);
		
		// 添加成员变量对应的PropertyBean属性
		List<Field> fieldList = FieldUtil.getAllBeanField(this.clazz);
		Map<String,Class<?>> fieldPropertyMap = new HashMap<>();
		for (Field field : fieldList) {
			String propertyName = field.getName() + PROPERTY_SUFFIX;
			Class<? extends Property<?>> propertyClass = null;
			Class<?> fieldType = FieldUtil.getFieldType(field);

			if( fieldType == String.class ) {
				propertyClass = SimpleStringProperty.class;
			}else if( fieldType == Double.class ) {
				propertyClass = SimpleFloatProperty.class;
			}else if( fieldType == Float.class) {
				propertyClass = SimpleFloatProperty.class;
			}else if( fieldType == Integer.class) {
				propertyClass = SimpleIntegerProperty.class;
			}else if( fieldType == Boolean.class ){
				propertyClass = SimpleBooleanProperty.class;
			}else if( fieldType == Array.class){
				// 这个不能这样绑定
//				propertyClass = (Class<? extends Property<?>>) SimpleListProperty.class;
			}else {
				continue;
			}
			fieldPropertyMap.put(field.getName(),propertyClass);
			gen.addProperty(propertyName, propertyClass);
		}
		
 
		   
		Class<?> genClass = (Class<?>) gen.createClass();

		BeanProperty beanProperty = new BeanProperty();
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(genClass);
		enhancer.setInterfaces(new Class[] {bindClass});
		enhancer.setCallback(this);
		Object bean = enhancer.create();

		BeanWrapper beanWrapper = new BeanWrapper(bean);

		Iterator<Entry<String,Class<?>>> it = fieldPropertyMap.entrySet().iterator();
		while( it.hasNext() ) {
			Entry<String,Class<?>> entry = it.next();
			String fieldName = entry.getKey();
			Class<?> value = entry.getValue();

			Property<?> property = (Property<?>) InstanceUtil.newInstance(value);
			property.addListener(new ChangeListener<Object>() {
				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
					try {
						Field field = DefaultBeanPropertyBuilder.this.clazz.getDeclaredField(fieldName);
						field.setAccessible(true);
						field.set(bean, newValue);
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
							| SecurityException e) {
						e.printStackTrace();
					}
				}

			});

			try {
				beanWrapper.setByField(fieldName + PROPERTY_SUFFIX, property);
			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}



		beanProperty.setBean(bean);

		return beanProperty;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object result = null;

		BeanWrapper beanWrapper = new BeanWrapper(obj);
		String methodName = method.getName();
		String prefix = methodName.substring(0,3).toLowerCase();
		String field = methodName.substring(3).toLowerCase();
		if( prefix.equals("set") && !field.endsWith(PROPERTY_SUFFIX) ) {
			Property<Object> property = (Property<Object>) beanWrapper.getByField(field + PROPERTY_SUFFIX);
			property.setValue(args[0]);		
			result = proxy.invokeSuper(obj, args);
		}else if( BIND_METHOD_NAME.equals(methodName) ){
			String key = (String) args[0];
			Property property = (Property) args[1];
			// 数据绑定
			Property<?> bidireactionalProperty = (Property<?>) beanWrapper.getByField(key + PROPERTY_SUFFIX);
			property.bindBidirectional(bidireactionalProperty);
		}else if( UNBIND_METHOD_NAME.equals(methodName) ){
			String key = (String) args[0];
			// 数据解绑定
			Property<?> property = (Property<?>) beanWrapper.getByField(key + PROPERTY_SUFFIX);
			property.unbind();
		}else {
			result = proxy.invokeSuper(obj, args);
		}

		return result;
	}

}
