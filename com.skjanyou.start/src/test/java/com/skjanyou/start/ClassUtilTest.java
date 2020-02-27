package com.skjanyou.start;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.skjanyou.start.core.SkjanyouClassLoader;

public class ClassUtilTest {

	public static void main(String[] args) throws Exception {
		File jarFile = new File("D:\\workspace\\GIT\\skjanyou-demo\\com.skjanyou.applicationcase\\target\\com.skjanyou.applicationcase-0.0.1-SNAPSHOT-jar-with-dependencies.jar");
		SkjanyouClassLoader classLoader = new SkjanyouClassLoader(new URL[]{ jarFile.toURI().toURL() });
		
//		List<Class<?>> list = ClassUtil.getClasses("", classLoader);
//		Class<?> cl = classLoader.loadClass("com.skjanyou.applicationcase.start.ApplicationCaseStart");
//		System.out.println(cl);
		
		jarFun( jarFile,"",classLoader );
	}
	
	public static void jarFun( File jarFile,String packageName,SkjanyouClassLoader loader ) throws Exception{
		// 第一个class类的集合
		List<Class<?>> classes = new ArrayList<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageDirName = packageName.replace('.', '/');
		// 定义一个JarFile
		JarFile jar;
		try {
			// 获取jar
			jar = new JarFile(jarFile);
			// 从此jar包 得到一个枚举类
			Enumeration<JarEntry> entries = jar.entries();
			// 同样的进行循环迭代
			while (entries.hasMoreElements()) {
				// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				// 如果是以/开头的
				if (name.charAt(0) == '/') {
					// 获取后面的字符串
					name = name.substring(1);
				}
				// 如果前半部分和定义的包名相同
				if (name.startsWith(packageDirName)) {
					int idx = name.lastIndexOf('/');
					// 如果以"/"结尾 是一个包
					if (idx != -1) {
						// 获取包名 把"/"替换成"."
						packageName = name.substring(0, idx).replace('/', '.');
					}
					// 如果可以迭代下去 并且是一个包
					if ((idx != -1) || recursive) {
						// 如果是一个.class文件 而且不是目录
						if (name.endsWith(".class") && !entry.isDirectory()) {
							// 去掉后面的".class" 获取真正的类名
							String className = name.substring(packageName.length() + 1, name.length() - 6);
							try {
								// 添加到classes
								classes.add(loader.loadClass(packageName + '.' + className));
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
