package com.skjanyou.start.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SkjanyouClassLoader extends URLClassLoader {
	private List<ClassLoader> otherClassLoader = new ArrayList<>();
	
	public SkjanyouClassLoader(URL[] url,List<ClassLoader> otherClassLoader) {
		super(url);
		this.otherClassLoader = otherClassLoader;
	}
	
	public SkjanyouClassLoader(URL[] url, ClassLoader classLoader,URLStreamHandlerFactory uhf) {
		super(url, classLoader, uhf);
	}

	public SkjanyouClassLoader(URL[] url, ClassLoader classLoader) {
		super(url, classLoader);
	}

	public SkjanyouClassLoader(URL[] url) {
		super(url);
	}

	public SkjanyouClassLoader addClassLoader( ClassLoader...classLoaders ){
		this.otherClassLoader.addAll( Arrays.asList(classLoaders) );
		return this;
	}
	
	public SkjanyouClassLoader addClassPath( String classPath ){
		File classPathFile = new File( classPath );
		File[] files = classPathFile.listFiles();
		URL fileUrl = null;
		for(File file : files){
			if( file.isDirectory() ){
				addClassPath( file.getAbsolutePath() );
			}else if( file.isFile() ){
				try {
					fileUrl = file.toURI().toURL();
					super.addURL(fileUrl);
				} catch ( MalformedURLException e ){
					
				}
			}
		}
		
		return this;
	}
	
	public SkjanyouClassLoader addClassUrl( URL url ){
		super.addURL(url);
		return this;
	}
	
	public SkjanyouClassLoader addClassUrls( Collection<URL> urlList ){
		for (URL url : urlList) {
			super.addURL(url);
		}
		return this;
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		// 在传入的加载器中加载类
		Class<?> findClass = null;
		for( ClassLoader classLoader : otherClassLoader ){
			try { 
				findClass = classLoader.loadClass(name);
			} catch ( ClassNotFoundException e ) {
			}
			if( findClass != null ){
				return findClass;
			}
		}
		
		// 使用父加载器加载类
		return super.loadClass(name);
	}
}
