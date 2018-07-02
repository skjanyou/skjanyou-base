package com.skjanyou.util;

import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.common.base.Predicate;

public class ScanUtil {
	public static final String ROOT = ".";
	public static final String ALL = ".*";
	public static final String NONE = "";
		
	/**
	 * 扫描目录下面的资源
	 * @param forpackage	在哪个目录下面查找, 根目录为 "."
	 * @param resource		查找资源,支持匹配符
	 * @param include		包括资源资源
	 * @param exclude		排除资源
	 * @return
	 */
	public static Set<String> scanResources(String forpackage,String resource,String include,String exclude){
		Set<String> result = null;
		Predicate<String> filter = new FilterBuilder().include(include)
				.exclude(exclude);
		Reflections reflections = new Reflections(new ConfigurationBuilder()
		.filterInputsBy(filter)
		.setScanners(new ResourcesScanner())
		.setUrls(ClasspathHelper.forPackage(forpackage)));

		result = reflections.getResources(Pattern.compile(resource));
		return result;
	}
}
