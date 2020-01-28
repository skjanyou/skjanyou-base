package com.skjanyou.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ResourcesUtil {
	private static final String ENCODE = "UTF-8";
	
	/**
	 * 从classpath或者jar内读取资源文件输入流
	 * @param path 路径
	 * @return 文件输入流
	 */
	private static InputStream getResourcesInnerInputStream(String path){
		InputStream is = ResourcesUtil.class.getClassLoader().getResourceAsStream(path);
		return is;
	}
	
	/**
	 * 从外部文件中获取资源文件输入流
	 * @param path
	 * @return	文件输入流
	 */
	private static InputStream getResourcesOuterInputStream(String path){
		InputStream is = null;
		try {
			is = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
		}
		return is;
	}
	
	/**
	 * 获取资源文件
	 */
	public static Properties getResources(String path){
		InputStream is = getResourcesOuterInputStream(path);
		if(is == null){
			is = getResourcesInnerInputStream(path);
		}
		Properties properties = new Properties();
		try{
			properties.load(new InputStreamReader(is,ENCODE));
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			CommUtil.close(is);
		}
		return properties;
	}
	
	/**
	 * 通过资源文件来创建bean类
	 * @param clazz	要创建的类
	 * @param path	文件目录
	 * @return 	类实例
	 */
	public static<T> T getInstanceByResources(Class<T> clazz,String path){
		return null;
	}

	/**
	 * 通过资源文件来给对象填充数据
	 * @param T	类实例
	 * @param path	文件目录
	 * @return 	类实例
	 */
	public static<T> T getInstanceByResources(T t,String path){
		Properties properties = ResourcesUtil.getResources(path);
		return null;
	}
	
	/**
	 * 
	 */
	public static Properties getInnerResources( String innerpath,ClassLoader classloader ){
		InputStream is = classloader.getResourceAsStream(innerpath);
		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			CommUtil.close(is);
		}
		return properties;
	}
	
}
