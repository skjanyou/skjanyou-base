package com.skjanyou.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourceUtil {
	public static List<URL> findResources( String packageName,ClassLoader loader ) throws IOException {
		List<URL> resultUrl = new ArrayList<>();
		String subPack = packageName.replace(".", "/");
		Enumeration<URL> urls = loader.getResources(subPack);
		while( urls.hasMoreElements() ){
			URL url = urls.nextElement();
			if( !isJarUrl(url) ){
				resultUrl.addAll(findFileResources(url));
			}else{
				resultUrl.addAll(findJarResources(url,subPack,loader));
			}
		}
		return resultUrl;
	}
	public static List<URL> findResources( String packageName ) throws IOException {
		return findResources(packageName, ResourceUtil.class.getClassLoader());
	}
	
	private static Collection<? extends URL> findJarResources(URL jarUrl,String subPack, ClassLoader loader) throws IOException{
		List<URL> jarUrlList = new ArrayList<>();
		
		//获取连接
		URLConnection connection = jarUrl.openConnection();
		if( connection instanceof JarURLConnection ){
			JarURLConnection jarCon = (JarURLConnection)connection;
			JarFile jarFile = jarCon.getJarFile();
			Enumeration<JarEntry> jarEntrys = jarFile.entries();
			while( jarEntrys.hasMoreElements() ){
				JarEntry subJarEntry = jarEntrys.nextElement();
				String entryName = subJarEntry.getName();
				// 判断是否为目录
				if( entryName.endsWith("/") ){ continue; }
				// 判断包路径是否匹配
				if( !entryName.startsWith(subPack) ){ continue; }
				// 满足条件,添加到结果集中
				URL subJarUrl = loader.getResource(entryName);
				jarUrlList.add(subJarUrl);
			}
			
		}
		
		jarUrlList.add(jarUrl);
		return jarUrlList;
	}

	private static Collection<? extends URL> findFileResources(URL fileUrl) throws IOException {
		List<URL> fileUrlList = new ArrayList<>();
		String filePath = fileUrl.getFile();
		File file = new File(filePath);
		if( file.isFile() ){
			fileUrlList.add(fileUrl);
		}else if( file.isDirectory() ){
			File[] subFiles = file.listFiles();
			if( subFiles != null ){
				for (File subFile : subFiles) {
					fileUrlList.addAll(findFileResources( subFile.toURI().toURL()));
				}
			}
		}
		return null;
	}

	private static boolean isJarUrl(URL url) {
		String protocol = url.getProtocol();
		return ("jar".equals(protocol) || "zip".equals(protocol) || "wsjar".equals(protocol) || "code-source".equals(protocol)) && url.getPath().contains("!/");
	}
}
