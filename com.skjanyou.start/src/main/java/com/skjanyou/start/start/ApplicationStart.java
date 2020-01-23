package com.skjanyou.start.start;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.ApplicationConst;
import com.skjanyou.start.config.ConfigManager;
import com.skjanyou.start.config.ConfigManagerFactory;
import com.skjanyou.start.plugin.PluginManager;
import com.skjanyou.start.plugin.bean.Plugin;
import com.skjanyou.start.util.BeanUtil;
import com.skjanyou.start.util.JarUtil;
import com.skjanyou.util.ClassUtil;
import com.skjanyou.util.CommUtil;
import com.skjanyou.util.ScanUtil;

public final class ApplicationStart {
	private static String pluginPattern = "plugin/\\S+.plugin.xml$";
	private static ClassLoader loader = null;
	private static List<Class<?>> classList = null;
	private ApplicationStart(){}
	public static void start( Class<?> clazz ){
		Configure configure = clazz.getAnnotation(Configure.class);
		if( configure == null ){ return ; }
		
		Class<? extends ConfigManagerFactory> cmFactoryClazz = configure.configManagerFactory();
		ConfigManagerFactory cmFactory = BeanUtil.getBean(cmFactoryClazz);
		ConfigManager manager = cmFactory.create();
		// 1.解析应用配置
		String lib_path = manager.getString(ApplicationConst.LIB_PATH);
		// 2.扫描jar目录,将jar添加至classpath
		Collection<URL> urlList = null;
		if( lib_path != null ){
			System.out.println("开始扫描Jar路径");
			String[] lib_path_arr = lib_path.split(",");
			urlList = JarUtil.getAllJarFileURL(lib_path_arr);
			System.out.println("开始扫描Jar路径完成,总共找到Jar文件" + urlList.size() + "个");
		}
		// 创建ClassLoader
		if( urlList == null ) { urlList = new ArrayList<>(); }
		loader = new URLClassLoader( urlList.toArray( new URL[]{} ) );
		Thread.currentThread().setContextClassLoader(loader);
		// 3.扫描classpath目录,获取所有的*.plugin.xml文件
		List<URL> list = null;
		try {
			list = ScanUtil.findResourcesByPattern("", "plugin/\\S+.plugin.xml$", loader);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("所有插件配置文件失败!",e);
		}
		if( list != null ){
			for (URL url : list) {
				InputStream is = null;
				try {
					is = url.openStream();
					SAXReader sr = new SAXReader();
					Document document = sr.read(is);
					Element root = document.getRootElement();
					String id = root.attributeValue("id");	// 插件ID
					String displayName = root.attributeValue("displayName"); //插件名称
					String activatorString = root.attributeValue("activator");
					Class activator = null;
					try{
						activator = loader.loadClass( activatorString ); //启动类
					} catch ( ClassNotFoundException e){
						activator = ApplicationStart.class.getClassLoader().loadClass( activatorString );
					}
						int order = Integer.valueOf(root.attributeValue("order"));				//排序
					Boolean enable = Boolean.valueOf(root.attributeValue("enable"));	//是否启动
					Boolean failOnInitError = Boolean.valueOf(root.attributeValue("failOnInitError"));						//报错时是否终止启动
					System.out.println("加载插件:[id:" + id + ",displayName:" + displayName + "]");
					
					Plugin plugin = new Plugin();
					plugin.setId(id);plugin.setActivator(activator);plugin.setDisplayName(displayName);
					plugin.setEnable(enable);plugin.setFailOnInitError(failOnInitError);plugin.setOrder(order);
					
					PluginManager.registPlugin(plugin);
				} catch (IOException | DocumentException | ClassNotFoundException e) {
					e.printStackTrace();
				} finally {
					CommUtil.close(is);
				}
			}
		}
		
		// 3.加载所有的类  TODO
		List<Class<?>> allClassList = ClassUtil.getClasses("",loader);
		System.out.println(allClassList);
		PluginManager.loadAllPlugins();
		
	}


	
}
