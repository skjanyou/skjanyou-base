package com.skjanyou.start.start;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.skjanyou.annotation.api.Application.Bean;
import com.skjanyou.annotation.api.Application.Component;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.SimpleLogUtil;
import com.skjanyou.plugin.PluginManager;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.Plugin;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.ApplicationConst;
import com.skjanyou.start.config.ComplexPluginConfig;
import com.skjanyou.start.config.ConfigManager;
import com.skjanyou.start.config.ConfigManagerFactory;
import com.skjanyou.start.exception.StartFailException;
import com.skjanyou.start.ioc.BeanContainer;
import com.skjanyou.start.util.InstanceUtil;
import com.skjanyou.start.util.JarUtil;
import com.skjanyou.util.ClassUtil;
import com.skjanyou.util.CommUtil;
import com.skjanyou.util.ResourcesUtil;
import com.skjanyou.util.ScanUtil;
import com.skjanyou.util.StringUtil;

public final class ApplicationStart {
	private ApplicationStart(){}
	
	private static Class<?> startClass;
	private static ClassLoader classLoader;
	// 配置中心
	private static ConfigManager manager;
	private static String pluginPackageName = "plugin/";
	private static String pluginPattern = "plugin/\\S+.plugin.xml$";
	// 所有的类
	private static Set<Class<?>> classSet;
	// logger
	private static Logger logger = SimpleLogUtil.getLogger(ApplicationStart.class);
	public static void start( Class<?> clazz ) {
		startClass = clazz;
		Configure configure = startClass.getAnnotation(Configure.class);
		if( configure == null ){ 
			logger.error("启动类" + clazz.getName() + "没有配置@Configure注解,应用程序无法启动!");
			return ; 
		}	
		// 1.读取配置
		initConfig();
		// 2.搜索jar文件,并将jar文件放置到classpath内,并初始化classloader
		findJarFile();
		// 3.设置当前运行上下文的classloader 
		Thread.currentThread().setContextClassLoader(classLoader);
		// 4.扫描classpath目录,获取所有的*.plugin.xml文件
		findPlugin();
		// 5.加载所有的类
		loadAllClasses();
		// 6.创建所有的Bean
		initBean();
		// 7.填充依赖bean
		fillDependency();
		// 8.初始化所有的插件bean
		initPlugin();
		// 9.启动所有的插件
		PluginManager.loadAllPlugins();
		// 10.绑定线程守护事件
		
	} 
	
	/** 1.读取配置  **/
	private static void initConfig(){
		Configure configure = startClass.getAnnotation(Configure.class);

		Class<? extends ConfigManagerFactory> cmFactoryClazz = configure.configManagerFactory();
		ConfigManagerFactory cmFactory = InstanceUtil.newInstance(cmFactoryClazz);
		BeanContainer.setBean(ConfigManagerFactory.class.getName(), cmFactory);
		manager = cmFactory.create();
	}	

	/** 搜索jar文件 **/
	private static void findJarFile(){
		String lib_path = manager.getString(ApplicationConst.LIB_PATH);
		// 2.扫描jar目录,将jar添加至classpath
		Collection<URL> urlList = null;
		if( lib_path != null ){
			logger.info("----------------开始扫描Jar路径----------------");
			String[] lib_path_arr = lib_path.split(",");
			urlList = JarUtil.getAllJarFileURL(lib_path_arr);
			logger.info("----------------扫描Jar路径完成,总共找到Jar文件" + urlList.size() + "个----------------");
		}
		// 创建ClassLoader
		if( urlList == null ) { urlList = new ArrayList<>(); }
		classLoader = new URLClassLoader( urlList.toArray( new URL[]{} ) );
	}
	
	/** 查找所有的插件 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void findPlugin(){
		logger.info("----------------开始扫描插件----------------");
		List<URL> list = null;
		try {
			list = ScanUtil.findResourcesByPattern(pluginPackageName, pluginPattern, classLoader);
		} catch (IOException e) {
			logger.error(e);
			throw new RuntimeException("所有插件配置文件失败!",e);
		}
		if( list != null ){
			for (URL url : list) {
				InputStream is = null;
				Plugin plugin = null;
				try {
					is = url.openStream();
//					Plugin p = XmlParseUtil.xml2Bean(is, Plugin.class);   TODO
					SAXReader sr = new SAXReader();
					Document document = sr.read(is);
					Element root = document.getRootElement();
					String id = root.attributeValue("id");	// 插件ID
					String displayName = root.attributeValue("displayName"); //插件名称
					String activatorString = root.attributeValue("activator");
					Class activator = null;
					try{
						activator = classLoader.loadClass( activatorString ); //启动类
					} catch ( ClassNotFoundException e){
						activator = Class.forName( activatorString );
					}
					int order = Integer.valueOf(root.attributeValue("order"));				//排序
					Boolean enable = Boolean.valueOf(root.attributeValue("enable"));	//是否启动
					Boolean failOnInitError = Boolean.valueOf(root.attributeValue("failOnInitError"));						//报错时是否终止启动
					String defaultConfig = root.attributeValue("defaultConfig");	//默认配置文件路径
					String classScanPath = root.attributeValue("classScanPath");
					logger.info("扫描到插件:{id:" + id + ",displayName:" + displayName + "}");
					
					plugin = new Plugin();
					plugin.setId(id);plugin.setActivator(activator);plugin.setDisplayName(displayName);
					plugin.setEnable(enable);plugin.setFailOnInitError(failOnInitError);plugin.setOrder(order);
					plugin.setDefaultConfig(defaultConfig);plugin.setClassScanPath(classScanPath);

					PluginManager.registPlugin(plugin);
				} catch (IOException | DocumentException | ClassNotFoundException e) {
					if( plugin != null && plugin.getFailOnInitError() ){
						throw new StartFailException("",e);
					}else{
						logger.error("加载插件失败",e);
					}
				} finally {
					CommUtil.close(is);
				}
			}
		}	
		logger.info("----------------扫描插件完成----------------");
	}	
	
	/** 加载所有的类 **/
	private static void loadAllClasses(){
		List<Class<?>> pluginClass = ClassUtil.getClasses("", classLoader);
		classSet = new HashSet<>(pluginClass);
	}
	
	/** 创建所有的Bean **/
	private static void initBean(  ){
		String className = null;
		for (Class<?> cla : classSet) {
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
			
		}		
	}	
	
	/** 填充依赖bean **/
	private static void fillDependency( ){
		for (Class<?> cla : classSet) {
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
						Object bean = null;
						if( !ClassUtil.isStatic(field) ){
							bean = InstanceUtil.newInstance(cla);
							BeanContainer.setBean(cla.getName(), bean);
						}
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
	
	/** 初始化插件 **/
	private static void initPlugin(){
		logger.info("----------------开始加载插件----------------");
		List<Plugin> pluginList = PluginManager.getPluginList();
		PluginSupport pluginSupport = null;
		List<Class<?>> classList = null;
		PluginConfig properties = null;
		for (Plugin plugin : pluginList) {
			// 启动开启的插件
			if( plugin.getEnable() ){
				logger.info("开始加载插件:{ id:" + plugin.getId() + ",displayName:" + plugin.getDisplayName() + "}");
				pluginSupport = InstanceUtil.newInstance(plugin.getActivator());
				classList = scanPluginClassList(plugin.getClassScanPath());
				properties = new ComplexPluginConfig( manager, ResourcesUtil.getInnerResources(plugin.getDefaultConfig(), classLoader) );
				PluginManager.initPlugin(pluginSupport, classList, properties);
				logger.info("加载插件:{ id:" + plugin.getId() + ",displayName:" + plugin.getDisplayName() + "}完成");
			}else{
				logger.info("插件:{id:",plugin.getId(),",displayName:",plugin.getDisplayName(),"}因未启用,不会进行加载。");
			}
		}
		logger.info("----------------加载插件完成----------------");
	}
	
	/** 从列表中获取满足扫描规则的类  **/
	private static List<Class<?>> scanPluginClassList( String classScanPath ){
		List<Class<?>> resultClassList = new ArrayList<>();
		for (Class<?> clazz : classSet) {
			if( clazz.getName().startsWith(classScanPath) ){
				resultClassList.add(clazz);
			}
		}
		return resultClassList;
	}
}
