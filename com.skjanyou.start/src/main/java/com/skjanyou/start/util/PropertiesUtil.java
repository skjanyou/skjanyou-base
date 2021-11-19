package com.skjanyou.start.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.skjanyou.util.CommUtil;

public class PropertiesUtil {

	public static Properties read( URL url ) throws IOException{
		Properties propertis = new Properties();
		propertis.load(url.openStream());
		return propertis;
	}

	public static Properties combineByUrl( List<URL> urlList ) {
		Properties result = new Properties();

		return result;
	}

	public static Properties getResources( String file ) {
		Properties properties = null;
		String resources = file;
		if( file.startsWith("classpath:") ){
			resources = file.substring(10);
			properties = getInnerResources(resources, Thread.currentThread().getContextClassLoader());
		}else{
			properties = getOuterResources(resources,Thread.currentThread().getContextClassLoader());
		}

		return properties;
	}

	public static Properties getOuterResources( String outerpath,ClassLoader classloader ) {
		Properties properties = new Properties();
		List<Properties> propList = findOuterSubProperties(outerpath, classloader);
		propList.stream().forEach(p->{
			properties.putAll(p);
		});		
		return properties;
	}

	// 查询包含的子配置文件
	private static List<Properties> findOuterSubProperties( String outerpath,ClassLoader classloader ){
		List<Properties> resultList = new LinkedList<>();
		// 读取当前文件的配置
		InputStream is;
		try {
			is = new FileInputStream(outerpath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("找不到文件：" + outerpath, e);
		}
		Properties properties = new Properties();
		try {
			properties.load(new InputStreamReader(is,"UTF-8"));
			resultList.add(properties);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			CommUtil.close(is);
		}

		// 读取子文件的配置
		try {
			is = new FileInputStream(outerpath);
		} catch (FileNotFoundException e) {
		}
		Reader reader = new InputStreamReader(is);
		BufferedReader bf = new BufferedReader(reader);
		String line = null;
		try {
			while( ( line = bf.readLine() ) != null ) {
				if( !line.contains("=") ) {
					continue;
				}
				String[] arr = line.split("=");
				String key = arr[0].trim();
				if( "include".equalsIgnoreCase(key) ) {
					String path = arr[1];
					try {
						List<Properties> subProperties = findOuterSubProperties(path,classloader);
						resultList.addAll(subProperties);		
					} catch( RuntimeException re ) {
						re.printStackTrace();
						continue;
					}
				}
			}
		} catch(Exception e) {
			throw new RuntimeException("读取文件出错",e);
		} finally {
			CommUtil.close(bf);
			CommUtil.close(reader);
			CommUtil.close(is);
		}

		return resultList;		
	} 	


	public static Properties getInnerResources( String innerpath,ClassLoader classloader ){
		Properties properties = new Properties();

		List<Properties> propList = findInnerSubProperties(innerpath, classloader);
		propList.stream().forEach(p->{
			properties.putAll(p);
		});

		return properties;
	}

	// 查询包含的子配置文件
	private static List<Properties> findInnerSubProperties( String innerpath,ClassLoader classloader ){
		if( innerpath.startsWith("/")){ innerpath = innerpath.substring(1); }
		List<Properties> resultList = new LinkedList<>();
		// 读取当前文件的配置
		InputStream is = classloader.getResourceAsStream(innerpath);
		if( is == null ) {
			throw new RuntimeException("找不到文件：" + innerpath);
		}
		Properties properties = new Properties();
		try {
			properties.load(new InputStreamReader(is,"UTF-8"));
			resultList.add(properties);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			CommUtil.close(is);
		}

		// 读取子文件的配置
		is = classloader.getResourceAsStream(innerpath);
		Reader reader = new InputStreamReader(is);
		BufferedReader bf = new BufferedReader(reader);
		String line = null;
		try {
			while( ( line = bf.readLine() ) != null ) {
				if( !line.contains("=") ) {
					continue;
				}
				String[] arr = line.split("=");
				String key = arr[0].trim();
				if( "include".equalsIgnoreCase(key) ) {
					String path = arr[1];
					try {
						List<Properties> subProperties = findInnerSubProperties(path,classloader);
						resultList.addAll(subProperties);		
					} catch( RuntimeException re ) {
						re.printStackTrace();
						continue;
					}
				}
			}
		} catch(Exception e) {
			throw new RuntimeException("读取文件出错",e);
		} finally {
			CommUtil.close(bf);
			CommUtil.close(reader);
			CommUtil.close(is);
		}

		return resultList;		
	} 
}
