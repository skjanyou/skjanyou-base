package com.skjanyou.beancontainer.factory.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.util.ClassUtil;
import com.skjanyou.util.CommUtil;

@SuppressWarnings("unchecked")
public class BeandefinitionImpl implements Beandefinition {
	// 一个bean可能有多个实例,通过设置beanName来区分
	private static Map<String, Map<String, Object>> nameBeanContainer = new HashMap<>();
	
	@Override
	public <T> T getBeanByInterfaceClass(Class<?> clazz) {
		if( !clazz.isInterface() ){ return null; }
		Collection<Map<String,Object>> objectList = nameBeanContainer.values();
		for (Map<String, Object> map : objectList) {
			Collection<Object> values = map.values();
			for (Object object : values) {
				if( clazz.isAssignableFrom(object.getClass()) ){
					return (T) object;
				}
			}
		}
		return null;
	}

	@Override
	public <T> T getBean(String beanName) {
		if (CommUtil.isNullOrEmpty(beanName)) {
			return null;
		}
		Map<String, Object> map = nameBeanContainer.get(beanName);
		if (CommUtil.isNullOrEmpty(map)) {
			return null;
		}
		if (map.size() > 1) {
			throw new RuntimeException(beanName + "存在多个实例,未明确指定");
		}
		for (String key : map.keySet()) {
			return (T) map.get(key);
		}
		return null;
	}

	@Override
	public synchronized void setBean(String beanName, Object bean) {
		Class<?> clazz = ClassUtil.getSourceClass(bean.getClass());
		String realBeanName = clazz.getName();
		if (nameBeanContainer.containsKey(beanName)) {
			Map<String, Object> map = nameBeanContainer.get(beanName);
			map.put(realBeanName, bean);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(realBeanName, bean);
		nameBeanContainer.put(beanName, map);
	}

	@Override
	public HashSet<?> getBeans() {
		HashSet<Object> beans = new HashSet<Object>();
		for (String key : nameBeanContainer.keySet()) {
			Map<String, Object> map = nameBeanContainer.get(key);
			if (CommUtil.isNullOrEmpty(map)) {
				continue;
			}
			for (String realKey : map.keySet()) {
				beans.add(map.get(realKey));
			}
		}
		return beans;
	}

	@Override
	public boolean contains(String beanName) {
		return nameBeanContainer.containsKey(beanName);
	}

}
