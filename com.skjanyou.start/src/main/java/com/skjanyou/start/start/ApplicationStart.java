package com.skjanyou.start.start;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.simple.SystemLogger;
import com.skjanyou.plugin.PluginManager;
import com.skjanyou.plugin.bean.Plugin;
import com.skjanyou.start.anno.Bean;
import com.skjanyou.start.anno.Component;
import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.ApplicationConst;
import com.skjanyou.start.config.ConfigManager;
import com.skjanyou.start.config.ConfigManagerFactory;
import com.skjanyou.start.ioc.BeanContainer;
import com.skjanyou.start.util.InstanceUtil;
import com.skjanyou.start.util.JarUtil;
import com.skjanyou.util.ClassUtil;
import com.skjanyou.util.CommUtil;
import com.skjanyou.util.ScanUtil;
import com.skjanyou.util.StringUtil;

public final class ApplicationStart {
	private static Logger logger = new SystemLogger().create(ApplicationStart.class);
	// 启动类
	private static Class<?> startClass = null;
	// 配置中心
	private static ConfigManager manager = null;
	private static String pluginPattern = "plugin/\\S+.plugin.xml$";
	private static ClassLoader loader = null;
	private static List<Class<?>> classList = null;
	private ApplicationStart(){}
	public static void start( Class<?> clazz ){
		startClass = clazz;
		Configure configure = startClass.getAnnotation(Configure.class);
		if( configure == null ){ return ; }
		// 1.解析应用配置
		initConfig();
		// 2.搜索jar文件,并将jar文件放置到classpath内,并初始化classloader
		findJarFile();
		// 3.设置当前运行上下文的classloader 
		Thread.currentThread().setContextClassLoader(loader);
		// 4.扫描classpath目录,获取所有的*.plugin.xml文件
		findPlugin();
		// 5.加载所有的类  TODO
		loadClasses();
		// 6.初始化bean
		initBean();
		// 7.填充依赖bean
		fillDependency();

		// 7.加载所有的插件
		PluginManager.loadAllPlugins();

		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				PluginManager.shutdownAllPlugins();
			}
		});

	}

	private static void initConfig(){
		Configure configure = startClass.getAnnotation(Configure.class);

		Class<? extends ConfigManagerFactory> cmFactoryClazz = configure.configManagerFactory();
		ConfigManagerFactory cmFactory = InstanceUtil.newInstance(cmFactoryClazz);
		BeanContainer.setBean(ConfigManagerFactory.class.getName(), cmFactory);
		manager = cmFactory.create();
	}

	private static void findJarFile(){
		String lib_path = manager.getString(ApplicationConst.LIB_PATH);
		// 2.扫描jar目录,将jar添加至classpath
		Collection<URL> urlList = null;
		if( lib_path != null ){
			logger.debug("开始扫描Jar路径");
			String[] lib_path_arr = lib_path.split(",");
			urlList = JarUtil.getAllJarFileURL(lib_path_arr);
			logger.debug("开始扫描Jar路径完成,总共找到Jar文件" + urlList.size() + "个");
		}
		// 创建ClassLoader
		if( urlList == null ) { urlList = new ArrayList<>(); }
		loader = new URLClassLoader( urlList.toArray( new URL[]{} ) );

	}

	private static void findPlugin(){
		List<URL> list = null;
		try {
			list = ScanUtil.findResourcesByPattern("", pluginPattern, loader);
		} catch (IOException e) {
			logger.error(e);
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
					String defaultConfig = root.attributeValue("defaultConfig");	//默认配置文件路径
					logger.debug("扫描到插件:[id:" + id + ",displayName:" + displayName + "]");
					
					Plugin plugin = new Plugin();
					plugin.setId(id);plugin.setActivator(activator);plugin.setDisplayName(displayName);
					plugin.setEnable(enable);plugin.setFailOnInitError(failOnInitError);plugin.setOrder(order);
					plugin.setDefaultConfig(defaultConfig);

					PluginManager.registPlugin(plugin);
				} catch (IOException | DocumentException | ClassNotFoundException e) {
					e.printStackTrace();
				} finally {
					CommUtil.close(is);
				}
			}
		}		
	}


	private static void loadClasses(){
		classList = ClassUtil.getClasses("",loader);
	}

	private static void initBean(){
		String className = null;
		for (Class<?> cla : classList) {
			// 接口,跳过处理
			if(cla.isInterface()){ continue; }
			if(cla.isEnum()){ continue; }
			if(cla.isAnnotation()){ continue; }
			
			Bean beanAnno = cla.getAnnotation(Bean.class);
			if( beanAnno != null ){
				className = beanAnno.value();
				if( StringUtil.isBlank(className) ){
					// 如果没有提供bean的别名,就使用全限定名来作为查询key
					className = cla.getName();
				}
				Object bean = InstanceUtil.newInstance(cla);
				BeanContainer.setBean(className, bean);
			}
			
			Component component = cla.getAnnotation(Component.class);
			if( component != null ){
				
			}
		}		
	}

	private static void fillDependency(){
		for (Class<?> cla : classList) {
			// 带有Component注解的类,扫描内部的属性,填充数据
			Component component = cla.getAnnotation(Component.class);
			if( component != null ){
				Field[] fields = cla.getDeclaredFields();
				Bean fieldBean = null;String beanName = null;
				for (Field field : fields) {
					fieldBean = field.getAnnotation(Bean.class);
					if( fieldBean != null ){
						beanName = fieldBean.value();
						Object obj = null;
						if( null == beanName || "".equals(beanName) ){
							obj = BeanContainer.getBeanByInterfaceClass(field.getType());
						}else{
							obj = BeanContainer.getBean(beanName);
						}
						// 通过反射赋值
						field.setAccessible(true);
						Object bean = InstanceUtil.newInstance(cla);
						try {
							field.set(bean, obj);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}

				}
			}
		}
	}

	private static void startupPlugin(){}

}
