package com.skjanyou.start.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

public class JarUtil {
	public static String suffix = ".jar";

	/** 获取一个目录下面所有的Jar文件 **/
	public static Collection<JarFile> getAllJarFile( String... libPathArr ){
		Collection<JarFile> result = new ArrayList<>();
		for (String scanPath : libPathArr) {
			File scanFile = new File( scanPath );
			JarFile jarFile = null;
			if( scanFile.isDirectory() ){
				File[] files = scanFile.listFiles();
				for (File file : files) {
					if( file.isDirectory() ){
						result.addAll(getAllJarFile(file.getAbsolutePath()));
					}else{
						if( file.getName().endsWith(suffix) ){
							try {
								jarFile = new JarFile(file);
								result.add(jarFile);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}else{
				if( scanFile.getName().endsWith(suffix) ){
					try {
						jarFile = new JarFile(scanFile);
						result.add(jarFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return result;
	}

	/** 扫描Jar并将Jar转化为URL **/
	public static Collection<URL> getAllJarFileURL ( String... libPathArr ){
		Collection<URL> urlList = new ArrayList<>();
		File f = null;
		// 添加至ClassPath
		for( String libPathFile : libPathArr ){
			f = new File(libPathFile);
			if( f.exists() && f.isDirectory() ){
				File[] files = f.listFiles();
				for (File file : files) {
					if( file.isDirectory() ){
						urlList.addAll(getAllJarFileURL(file.getAbsolutePath()));
					}else{
						System.out.println("扫描到Jar文件:[" + file.getAbsolutePath() +  "]");
						urlList.add( converURL(file) );
					}
				}
			}else if( f.exists() && f.isFile() && f.getName().endsWith( JarUtil.suffix ) ){
				System.out.println("扫描到Jar文件:[" + f.getAbsolutePath() +  "]");
				urlList.add( converURL(f) );
			}
		}
		return urlList;
	}
	
	/** 通过正则匹配扫描Jar包内的文件 **/
	public static ZipEntry scanJarFile( JarFile jarFile, String resourcePattern ){
		ZipEntry resultEntry = null;
		Pattern pattern = Pattern.compile(resourcePattern);
		Enumeration<JarEntry> en = jarFile.entries();
		while( en.hasMoreElements() ){
			JarEntry jarEntry = en.nextElement();
			String name = jarEntry.getName();
			//System.out.println(name);
			Matcher match = pattern.matcher(name);
			if( match.find() ){
				resultEntry = jarEntry;
				break;
			}
		}
		return resultEntry;
	}

	private static URL converURL(File file) {
		URL resultURL = null;
		try {
			resultURL = file.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return resultURL;
	}

	public static void main(String[] args) throws IOException {
		JarFile jarFile = new JarFile( new File("D:\\temp\\com.skjanyou.db.mybatis-0.0.1-SNAPSHOT.jar") );
		ZipEntry ze = scanJarFile( jarFile,"plugin/\\S+.plugin.xml$");
		System.out.println(ze);
	}
}
