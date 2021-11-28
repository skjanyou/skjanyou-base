package com.skjanyou.start.start;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.skjanyou.annotation.api.Application.Autowired;
import com.skjanyou.annotation.api.Application.Bean;
import com.skjanyou.annotation.api.Application.Component;
import com.skjanyou.annotation.api.Util.Value;
import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.beancontainer.factory.BeandefinitionFactory;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.plugin.PluginDefineAnnotationClassManager;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.ApplicationConst;
import com.skjanyou.start.config.ConfigManager;
import com.skjanyou.start.core.CommandManager;
import com.skjanyou.start.core.CommandManager.Cmd;
import com.skjanyou.start.core.CommandManager.CommandProcess;
import com.skjanyou.start.core.SkjanyouClassLoader;
import com.skjanyou.start.exception.StartFailException;
import com.skjanyou.start.process.PluginProcess;
import com.skjanyou.start.process.ProgressLoader;
import com.skjanyou.start.provider.ClassLoaderProvider;
import com.skjanyou.start.provider.ConfigureProvider;
import com.skjanyou.start.util.InstanceUtil;
import com.skjanyou.start.util.JarUtil;
import com.skjanyou.start.util.ServiceLoaderUtil;
import com.skjanyou.util.ClassUtil;
import com.skjanyou.util.StreamUtil;
import com.skjanyou.util.StringUtil;

public abstract class SkjanyouApplicationStart {
	protected static Logger logger = LogUtil.getLogger(SkjanyouApplicationStart.class);
	private Class<?> configClass;
	private ProgressLoader progressLoader = null;
	private ClassLoaderProvider classLoaderProvider;
	private ConfigureProvider configureProvider;
	private Beandefinition beandefinition;
	private PluginProcess pluginProcess;
	private Set<Class<?>> classSet;
	private List<String> pluginScanPath = new LinkedList<>(Arrays.asList(new String[]{ "com.skjanyou" }));
	// Banner
	private static String BANNER = "classpath:banner.txt";
	public SkjanyouApplicationStart(){
		this.classLoaderProvider = ServiceLoaderUtil.load(ClassLoaderProvider.class);
		this.configureProvider = ServiceLoaderUtil.load(ConfigureProvider.class);
		this.beandefinition = ServiceLoaderUtil.load(BeandefinitionFactory.class).create();
		this.pluginProcess = ServiceLoaderUtil.load(PluginProcess.class);
		// 注册命令,应用启动
		CommandManager.regist(new Cmd("start", new CommandProcess() {
			@Override
			public void process() {
				versionInfo();
				start();
			}
		}));
		// 注册命令,应用停止
		CommandManager.regist(new Cmd("stop", new CommandProcess() {
			@Override
			public void process() {
				versionInfo();
			}
		}));		
		// 注册命令,帮助
		CommandManager.regist(new Cmd("help", new CommandProcess() {
			@Override
			public void process() {
				versionInfo();
				help();
			}
		}));	
		// 注册命令,应用默认配置文件生成
		CommandManager.regist(new Cmd("default-config", new CommandProcess() {
			@Override
			public void process() {
				versionInfo();
				defaultConfig();
			}
		}));			
	}
	
	protected void versionInfo() {
		logger.info("开始启动Skjanyou应用");
		logger.info( 
				"\n" + StreamUtil.readerFile2String(BANNER, "UTF-8") + "\n" 
				);
	}
	
	
	public static void start( Class<?> configClass,String[] args ){
		start(configClass,new ProgressLoader() {
			@Override
			public void progress(int total, int current, String message) {
				
			}

			@Override
			public void done() {
				
			}}
		,args );
	} 
	
	public static void start( Class<?> configClass,ProgressLoader progressLoader,String[] args ) {
		SkjanyouApplicationStart start = new SkjanyouApplicationStart(){};
		start.startup(configClass, progressLoader, args);
	}
	
	public void startup( Class<?> configClass,ProgressLoader progressLoader,String[] args  ){
		this.configClass = configClass;
		this.progressLoader = progressLoader == null ? new ProgressLoader() {
			@Override
			public void progress(int total, int current, String message) {}
			@Override
			public void done() {}
		} : progressLoader;
		CommandManager.processCommand(args[0]);
	}
	
	protected void defaultConfig() {
		// 1.检查注解
		Configure configure = configClass.getAnnotation(Configure.class);
		if( configure == null ){
			logger.error("启动类",configClass.getName(),"没有配置@Configure注解,应用程序无法启动!");
			return;
		}
		this.pluginScanPath.addAll(Arrays.asList(configure.scanPath()));
		// 2.配置参数,扫描路径,配置类等
		ConfigManager manager = this.configureProvider.getConfigure(this.configClass);
		// 3.创建ClassLoader
		ClassLoader srcClassLoader = Thread.currentThread().getContextClassLoader();
		SkjanyouClassLoader classLoader = this.classLoaderProvider.getClassLoader();
		classLoader.addClassLoader(srcClassLoader);
		Thread.currentThread().setContextClassLoader(classLoader);
		// 4.将前面步骤创建对象放置到Bean容器
		this.beandefinition.setBean(Beandefinition.class.getName(), this.beandefinition);
		this.beandefinition.setBean(ConfigManager.class.getName(), manager);
		this.beandefinition.setBean(SkjanyouClassLoader.class.getName(), classLoader);
		// 5.扫描Jar包
		this.findJarFile(manager, classLoader);
		// 6.初始化Plugin
		this.pluginProcess.findPlugin(manager, classLoader, this.pluginScanPath);
		// 7.生成配置文件
		this.pluginProcess.generateDefaultConfigList(new File("skjanyou.default.properties"));
		
	}
	
	protected void help(){
		// 1.检查注解
		Configure configure = configClass.getAnnotation(Configure.class);
		if( configure == null ){
			logger.error("启动类",configClass.getName(),"没有配置@Configure注解,应用程序无法启动!");
			return;
		}
		this.pluginScanPath.addAll(Arrays.asList(configure.scanPath()));
		// 2.配置参数,扫描路径,配置类等
		ConfigManager manager = this.configureProvider.getConfigure(this.configClass);
		// 3.创建ClassLoader
		ClassLoader srcClassLoader = Thread.currentThread().getContextClassLoader();
		SkjanyouClassLoader classLoader = this.classLoaderProvider.getClassLoader();
		classLoader.addClassLoader(srcClassLoader);
		Thread.currentThread().setContextClassLoader(classLoader);
		// 4.将前面步骤创建对象放置到Bean容器
		this.beandefinition.setBean(Beandefinition.class.getName(), this.beandefinition);
		this.beandefinition.setBean(ConfigManager.class.getName(), manager);
		this.beandefinition.setBean(SkjanyouClassLoader.class.getName(), classLoader);
		// 5.扫描Jar包
		this.findJarFile(manager, classLoader);
		// 6.初始化Plugin
		this.pluginProcess.findPlugin(manager, classLoader, this.pluginScanPath);
		// 7.初始化Plugin,并打印信息
		this.pluginProcess.initPlugin(manager, classLoader);		
		
		List<List<String>> resultList = new ArrayList<>();
		List<String> header = new ArrayList<>();
		header.add("插件名");header.add("插件键值");header.add("插件默认值");header.add("插件当前值");
		resultList.add(header);
		
		List<String> body = new ArrayList<>();
		header.add("插件名");header.add("插件键值");header.add("插件默认值");header.add("插件当前值");
		resultList.add(body);
		
		List<String> body2 = new ArrayList<>();
		header.add("插件名");header.add("插件键值");header.add("插件默认值");header.add("插件当前值");
		resultList.add(body2);
		
		List<String> body3 = new ArrayList<>();
		header.add("插件名");header.add("插件键值");header.add("插件默认值");header.add("插件当前值");
		resultList.add(body3);
		
		
		
	}
	
	protected void start(){
		// 1.检查注解
		Configure configure = configClass.getAnnotation(Configure.class);
		if( configure == null ){
			logger.error("启动类",configClass.getName(),"没有配置@Configure注解,应用程序无法启动!");
			return;
		}
		this.pluginScanPath.addAll(Arrays.asList(configure.scanPath()));
		progressLoader.progress(11, 1, "配置参数");
		// 2.配置参数,扫描路径,配置类等
		ConfigManager manager = this.configureProvider.getConfigure(this.configClass);
		progressLoader.progress(11, 2, "创建类加载器");
		// 3.创建ClassLoader
		ClassLoader srcClassLoader = Thread.currentThread().getContextClassLoader();
		SkjanyouClassLoader classLoader = this.classLoaderProvider.getClassLoader();
		classLoader.addClassLoader(srcClassLoader);
		Thread.currentThread().setContextClassLoader(classLoader);
		progressLoader.progress(11, 3, "配置Bean容器");
		// 4.将前面步骤创建对象放置到Bean容器
		this.beandefinition.setBean(Beandefinition.class.getName(), this.beandefinition);
		this.beandefinition.setBean(ConfigManager.class.getName(), manager);
		this.beandefinition.setBean(SkjanyouClassLoader.class.getName(), classLoader);
		progressLoader.progress(11, 4, "扫描Jar包");
		// 5.扫描Jar包
		this.findJarFile(manager, classLoader);
		progressLoader.progress(11, 5, "扫描插件");
		// 6.初始化Plugin
		this.pluginProcess.findPlugin(manager, classLoader, this.pluginScanPath);
		progressLoader.progress(11, 6, "加载类");
		// 7.加载所有的类
		this.loadAllClasses(classLoader);
		progressLoader.progress(11, 7, "初始化插件");
		// 8.初始化Plugin
		this.pluginProcess.initPlugin(manager, classLoader);
		progressLoader.progress(11, 8, "初始化Bean");
		// 9.初始化Bean
		this.initBean( this.beandefinition );
		// 10.填充依赖
		this.fillDependency( this.beandefinition );
		progressLoader.progress(11, 9, "启动描插件");
		// 11.启动所有的Plugin
		this.pluginProcess.startPlugin();
		progressLoader.progress(11, 10, "绑定shutdown钩子");
		// 12.绑定shutdown钩子
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				SkjanyouApplicationStart.this.pluginProcess.shutdownPlugin();
				SkjanyouApplicationStart.this.onExit();
			}
		});
		progressLoader.progress(11, 11, "应用启动完成");
		progressLoader.done();
	}
	
	protected void onExit(){
	}
	
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
			
			// 检查Component注解
			Component componentAnno = targetClass.getAnnotation(Component.class);
			if( componentAnno != null ){
				// 将该类实例化并且放入容器
				Object bean = InstanceUtil.newInstance(targetClass);
				// 开始对该类进行配置
				// 插件配置类
				PluginConfig config = this.pluginProcess.getSystemAllPluginConfig();
				Field[] fields = targetClass.getDeclaredFields();
				for (Field field : fields) {
					Value valAnno = field.getAnnotation(Value.class);
					if( valAnno != null ) {
						field.setAccessible(true);
						String keyName = valAnno.value();
						String value = config.getProperty(keyName);
						if( value == null ) {
							field.setAccessible(false);
							throw new StartFailException(String.format("类%s的成员变量%s无法匹配到配置参数%s。", targetClass.getName(),field.getName(),keyName));
						}
						
						try {
							field.set(bean, value);
						} catch (IllegalArgumentException e) {
							throw new StartFailException(String.format("类%s的成员变量%s无法匹配到配置参数%s。", targetClass.getName(),field.getName(),keyName),e);
						} catch (IllegalAccessException e) {
							throw new StartFailException(String.format("类%s的成员变量%s无法匹配到配置参数%s。", targetClass.getName(),field.getName(),keyName),e);
						}finally {
							field.setAccessible(false);
						}
					}
				}
				
				Method[] methods = targetClass.getDeclaredMethods();
				for (Method method : methods) {
					Bean beanAnno = method.getAnnotation(Bean.class);
					if( beanAnno != null ) {
						className = beanAnno.value();
						if( StringUtil.isBlank(className) ){
							// 如果没有提供bean的别名,就使用全限定名来作为查询key
							className = targetClass.getName();
						}
						
						// 执行该方法,获得Bean,检查参数是否需要传入Beandefinition
						try {
							int pCount = method.getParameterCount();
							Object resultBean = null;
							if( pCount == 0 ) {
								resultBean = method.invoke(bean);
							}else {
								resultBean = method.invoke(bean, new Object[] {beandefinition});
							}
							
							beandefinition.setBean(className, resultBean);
						} catch (IllegalAccessException | IllegalArgumentException
								| InvocationTargetException e) {
							throw new StartFailException(String.format("类%s的方法%s只能选择无参或者传入Beandefinition。", targetClass.getName(),method.getName()),e);
						}
					}
				}
				// 存在Component注解的,不会再参与其他注解的扫描,观察一下是否需要这样处理
				continue;
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
				Autowired fieldAutowired = null;String beanName = null;
				for (Field field : fields) {
					fieldAutowired = field.getAnnotation(Autowired.class);
					if( fieldAutowired != null ){
						beanName = fieldAutowired.value();
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
						}finally {
							field.setAccessible(false);
						}
					}

				}
			}
		}
	}	
		
}
