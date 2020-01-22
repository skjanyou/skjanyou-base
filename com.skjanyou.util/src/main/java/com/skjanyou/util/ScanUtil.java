package com.skjanyou.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanUtil {
	
	public static List<URL> findResourcesByPattern( String packageName,String resourcePattern ,ClassLoader loader ) throws IOException {
		List<URL> result = findResources(packageName, loader);
		Pattern pattern = Pattern.compile(resourcePattern);
		Matcher match = null;
		Iterator<URL> it = result.iterator();
		while( it.hasNext() ){
			URL url = it.next();
			match = pattern.matcher(url.getFile());
			if( !match.find() ){
				it.remove();
			}
		}
		
		return result;
	}
	
	public static List<URL> findResources( String packageName,ClassLoader loader ) throws IOException{
		List<URL> resultUrl = new ArrayList<>();
		String subPack = packageName.replace(".","/");
		Enumeration<URL> urls = loader.getResources(subPack);
		while( urls.hasMoreElements() ){
			URL url = urls.nextElement();
			if( !isJarUrl( url ) ){
				resultUrl.addAll( fildFileResources(url) );
			}else{
				resultUrl.addAll( findJarResources(url,packageName,loader) );
			}
		}
		
		
		return resultUrl;
	}
	
	public static List<URL> findResources( String packageName ) throws IOException {
		return findResources(packageName, ScanUtil.class.getClassLoader());
	}

	private static Collection<? extends URL> findJarResources(URL jarUrl,String packageName, ClassLoader loader) throws IOException {
		List<URL> jarUrlList = new ArrayList<>();
		
		URLConnection connection = null;
		try {
			connection = jarUrl.openConnection();
			if( connection instanceof JarURLConnection ){
				JarURLConnection jarCon = (JarURLConnection) connection;
				JarFile jarFile = jarCon.getJarFile();
				Enumeration<JarEntry> jarEntrys = jarFile.entries();
				while( jarEntrys.hasMoreElements() ){
					JarEntry subJarEntry = jarEntrys.nextElement();
					String entryName = subJarEntry.getName();
					// 判断是否为目录
					if( entryName.endsWith("/") ){ continue ; }
					// 判断包路径是否匹配
					if( !entryName.startsWith(packageName.replace(".", "/"))){ continue; }
					URL subJarUrl = loader.getResource(entryName);
					jarUrlList.add(subJarUrl);
				}
			}
			
			jarUrlList.add(jarUrl);
		} finally {
			
		}
		
		return jarUrlList;
	}
	
	

	private static Collection<? extends URL> fildFileResources(URL fileUrl) throws MalformedURLException {
		List<URL> fileUrlList = new ArrayList<>();
		String filePath = fileUrl.getFile();
		File file = new File(filePath);
		if( file.isFile() ){
			fileUrlList.add(fileUrl);
		}else if( file.isDirectory() ){
			File[] subFiles = file.listFiles();
			if( subFiles != null ){
				for( File subFile : subFiles ){
					fileUrlList.addAll(fildFileResources(subFile.toURI().toURL()));
				}
			}
		}
		return fileUrlList;
	}

	private static boolean isJarUrl(URL url) {
		String protocol = url.getProtocol();
		return ("jar".equals(protocol)) || ("zip".equals(protocol)) || ("wsjar".equals(protocol)) || ("code-source".equals(protocol));
	}
}
