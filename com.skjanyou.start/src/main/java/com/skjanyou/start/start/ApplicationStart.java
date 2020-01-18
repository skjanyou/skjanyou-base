package com.skjanyou.start.start;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.ApplicationConst;
import com.skjanyou.start.config.ConfigManager;
import com.skjanyou.start.config.ConfigManagerFactory;
import com.skjanyou.start.util.BeanUtil;

public class ApplicationStart {
	private static String suffix = ".jar";
	private static ClassLoader loader = null;
	private static List<Class<?>> classList = null;
	
	public static void start( Class<?> clazz ){
		Configure configure = clazz.getAnnotation(Configure.class);
		if( configure == null ){ return ; }
		
		Class<? extends ConfigManagerFactory> cmFactoryClazz = configure.configManagerFactory();
		ConfigManagerFactory cmFactory = BeanUtil.getBean(cmFactoryClazz);
		ConfigManager manager = cmFactory.create();
		// 1.解析配置文件
		String lib_path = manager.getString(ApplicationConst.LIB_PATH);
		// 2.扫描jar目录
		List<URL> urlList = new ArrayList<>();
		if( lib_path != null && lib_path.length() != 0 ){
			String[] libPathArr = lib_path.split(",");
			File f = null;
			// 添加至ClassPath
			for( String libPathFile : libPathArr ){
				f = new File(libPathFile);
				if( f.exists() && f.isDirectory() ){
					File[] jarFiles = f.listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							return name.endsWith( suffix );
						}
					});
					// 加载URL
					for (File file : jarFiles) {
						urlList.add( converURL( file ) );
					}
				}else if( f.exists() && f.isFile() && f.getName().endsWith( suffix ) ){
					urlList.add( converURL(f) );
				}
			}
		}
		// 创建ClassLoader
		loader = new URLClassLoader( urlList.toArray( new URL[]{} ));
		
		// 3.加载所有的类  TODO
		
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
	
}
