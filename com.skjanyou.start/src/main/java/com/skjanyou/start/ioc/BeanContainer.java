package com.skjanyou.start.ioc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;



import com.skjanyou.util.ClassUtil;
import com.skjanyou.util.CommUtil;

public class BeanContainer {

	private static Set<Class<?>> clazzContainer = new ConcurrentSkipListSet<Class<?>>();

	// 一个bean可能有多个实例,通过设置beanName来区分
	private static Map<String, Map<String, Object>> beanContainer = new HashMap<String, Map<String, Object>>();


	public static <T> T getBean(Class<?> clazz) {
		String beanName = clazz.getName();
		if (CommUtil.isNullOrEmpty(beanName)) {
			return null;
		}
		return getBean(beanName);
	}

	public static Set<Class<?>> getClazzContainer() {
		return clazzContainer;
	}

	public static void wipeClazzFromContainer(Class<?> clazz) {
		clazzContainer.remove(clazz);
	}

	public static void setClazzContainer(Set<Class<?>> clazzContainer) {
		BeanContainer.clazzContainer = clazzContainer;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		if (CommUtil.isNullOrEmpty(beanName)) {
			return null;
		}
		Map<String, Object> map = beanContainer.get(beanName);
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

	public static synchronized void setBean(String beanName, Object bean) {
		Class<?> clazz = ClassUtil.getSourceClass(bean.getClass());
		String realBeanName = clazz.getName();
		if (beanContainer.containsKey(beanName)) {
			Map<String, Object> map = beanContainer.get(beanName);
			map.put(realBeanName, bean);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(realBeanName, bean);
		beanContainer.put(beanName, map);
	}

	public static boolean contains(String beanName) {
		return beanContainer.containsKey(beanName);
	}

	public static HashSet<?> getBeans() {
		HashSet<Object> beans = new HashSet<Object>();
		for (String key : beanContainer.keySet()) {
			Map<String, Object> map = beanContainer.get(key);
			if (CommUtil.isNullOrEmpty(map)) {
				continue;
			}
			for (String realKey : map.keySet()) {
				beans.add(map.get(realKey));
			}
		}
		return beans;
	}
	
}
