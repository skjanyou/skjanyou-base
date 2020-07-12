package com.skjanyou.start.start;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.skjanyou.annotation.api.Application.Bean;
import com.skjanyou.annotation.api.Application.Component;
import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.beancontainer.factory.BeandefinitionFactory;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.plugin.PluginDefineAnnotationClassManager;
import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.ApplicationConst;
import com.skjanyou.start.config.ConfigManager;
import com.skjanyou.start.core.SkjanyouClassLoader;
import com.skjanyou.start.process.PluginProcess;
import com.skjanyou.start.provider.ClassLoaderProvider;
import com.skjanyou.start.provider.ConfigureProvider;
import com.skjanyou.start.util.InstanceUtil;
import com.skjanyou.start.util.JarUtil;
import com.skjanyou.start.util.ServiceLoaderUtil;
import com.skjanyou.util.ClassUtil;
import com.skjanyou.util.StringUtil;

public abstract class SkjanyouApplicationStart {
	private static SkjanyouApplicationStart start = new SkjanyouApplicationStart() {};
	protected static Logger logger = LogUtil.getLogger(SkjanyouApplicationStart.class);
	private Class<?> configClass;
	private ClassLoaderProvider classLoaderProvider;
	private ConfigureProvider configureProvider;
	private Beandefinition beandefinition;
	private PluginProcess pluginProcess;
	private Set<Class<?>> classSet;
	private List<String> pluginScanPath = new LinkedList<>(Arrays.asList(new String[]{ "com.skjanyou" }));
	SkjanyouApplicationStart(){
		this.classLoaderProvider = ServiceLoaderUtil.load(ClassLoaderProvider.class);
		this.configureProvider = ServiceLoaderUtil.load(ConfigureProvider.class);
		this.beandefinition = ServiceLoaderUtil.load(BeandefinitionFactory.class).create();
		this.pluginProcess = ServiceLoaderUtil.load(PluginProcess.class);
	}
	
	
	
	public static void start( Class<?> configClass,String[] args ){
		start.configClass = configClass;
		// 1.检查注解
		Configure configure = configClass.getAnnotation(Configure.class);
		if( configure == null ){
			logger.error("启动类",configClass.getName(),"没有配置@Configure注解,应用程序无法启动!");
			return;
		}
		// 2.配置参数,扫描路径,配置类等
		ConfigManager manager = start.configureProvider.getConfigure(start.configClass);
		// 3.创建ClassLoader
		ClassLoader srcClassLoader = Thread.currentThread().getContextClassLoader();
		SkjanyouClassLoader classLoader = start.classLoaderProvider.getClassLoader();
		classLoader.addClassLoader(srcClassLoader);
		Thread.currentThread().setContextClassLoader(classLoader);
		// 4.将前面步骤创建对象放置到Bean容器
		start.beandefinition.setBean(Beandefinition.class.getName(), start.beandefinition);
		start.beandefinition.setBean(ConfigManager.class.getName(), manager);
		start.beandefinition.setBean(SkjanyouClassLoader.class.getName(), classLoader);
		// 5.扫描Jar包
		start.findJarFile(manager, classLoader);
		// 6.初始化Plugin
		start.pluginProcess.findPlugin(manager, classLoader, start.pluginScanPath);
		// 7.加载所有的类
		start.loadAllClasses(classLoader);
		// 8.初始化Plugin
		start.pluginProcess.initPlugin(manager, classLoader);
		// 9.初始化Bean
		start.initBean( start.beandefinition );
		// 10.填充依赖
		start.fillDependency( start.beandefinition );
		// 11.启动所有的Plugin
		start.pluginProcess.startPlugin();
		// 12.绑定shutdown钩子
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				start.pluginProcess.shutdownPlugin();
				start.onExit();
			}
		});
	}
	
	protected void onExit(){}
	
	/** 搜索jar文件 **/
	protected void findJarFile( ConfigManager manager,SkjanyouClassLoader classLoader ){
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
		classLoader.addClassUrls(urlList);
	}
	
	/** 加载所有的类 **/
	protected void loadAllClasses( SkjanyouClassLoader classLoader ){
		List<Class<?>> pluginClass = new ArrayList<>();
		for( String scanPath : pluginScanPath){
			pluginClass.addAll(ClassUtil.getClasses(scanPath, classLoader));
		}
		classSet = new HashSet<>(pluginClass);
	}
	
	/** 创建所有的Bean 
	 * @param beandefinition 
	 * **/
	protected void initBean( Beandefinition beandefinition  ){
		String className = null;
		Class<? extends Annotation> registAnnoClass = null;
		for (Class<?> targetClass : classSet) {
			// 接口,跳过处理
			if(targetClass.isEnum()){ continue; }
			if(targetClass.isAnnotation()){ continue; }
			
			Bean beanAnno = targetClass.getAnnotation(Bean.class);
			if( beanAnno != null ){
				className = beanAnno.value();
				if( StringUtil.isBlank(className) ){
					// 如果没有提供bean的别名,就使用全限定名来作为查询key
					className = targetClass.getName();
				}
				Object bean = InstanceUtil.newInstance(targetClass);
				beandefinition.setBean(className, bean);
			}
			registAnnoClass = PluginDefineAnnotationClassManager.getRegistAnnotationClass(PluginDefineAnnotationClassManager.classAnnotationClass(),targetClass);
			if( registAnnoClass != null ){
				PluginDefineAnnotationClassManager.classProcess(registAnnoClass, targetClass, beandefinition);
			}
			
		}		
	}	
	
	/** 填充依赖bean 
	 * @param beandefinition 
	 * **/
	protected void fillDependency(Beandefinition beandefinition ){
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
							obj = beandefinition.getBeanByInterfaceClass(field.getType());
						}else{
							obj = beandefinition.getBean(beanName);
						}
						// 通过反射赋值
						field.setAccessible(true);
						Object bean = null;
						if( !ClassUtil.isStatic(field) ){
							bean = InstanceUtil.newInstance(cla);
							beandefinition.setBean(cla.getName(), bean);
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
		
}
