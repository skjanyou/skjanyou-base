package com.skjanyou.start.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;

import com.skjanyou.util.CommUtil;
import com.skjanyou.util.StringUtil;

public class ServiceLoaderUtil {
	private static final String JAVA_SPI_PATTERN = "META-INF/services/";
	private static final String SKJANYOU_SPI_PATTERN = "spi/";
	private static final String CHARSET = "utf-8";
	
	public static<T> T load ( Class<T> clazz ) {
		return load( clazz,Thread.currentThread().getContextClassLoader() );
	}
	
	public static <T> T load( Class<T> clazz, ClassLoader loader ){
		T result = null;
		Class<T> resultClass = loadClass( clazz,loader );
		try {
			result = resultClass.newInstance();
		} catch( Exception e ){
			throw new RuntimeException(e);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static<T> Class<T> loadClass( Class<T> targetClass, ClassLoader loader ) {
		Class<T> resultClass = null;
		ClassLoader classLoader = loader;
		if( classLoader == null ){ classLoader = Thread.currentThread().getContextClassLoader(); }
		String name = targetClass.getName();
		InputStream localInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader localBufferedReader = null;
		
		URL url = null;
		try {
			// 1.Skjanyou Spi
			Enumeration<URL> e = classLoader.getResources(SKJANYOU_SPI_PATTERN + name);
			if( e.hasMoreElements() ) {
				url = e.nextElement();
			}
			if( url == null ){
				// 2.兼容Java Spi
				e = classLoader.getResources(JAVA_SPI_PATTERN + name);
				if( e.hasMoreElements() ) {
					url = e.nextElement();
				}
			}
			
		} catch ( IOException e ) {
			throw new RuntimeException(e);
		} 
		
		if( url == null ){
			throw new RuntimeException("找不到Spi配置文件");
		}
		
		try {
			localInputStream = url.openStream();
			inputStreamReader = new InputStreamReader(localInputStream,CHARSET);
			localBufferedReader = new BufferedReader(inputStreamReader);
			String subClassName = localBufferedReader.readLine();
			if( StringUtil.isBlank(subClassName) ){
				throw new RuntimeException("Spi文件有误" + subClassName);
			}
			
			resultClass = (Class<T>) classLoader.loadClass(subClassName);
		} catch ( Exception e ) {
			throw new RuntimeException(e);
		} finally {
			CommUtil.close(localBufferedReader);
			CommUtil.close(inputStreamReader);
			CommUtil.close(localInputStream);
		}
		
		
		return resultClass;
	}
}
