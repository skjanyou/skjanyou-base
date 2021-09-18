package com.skjanyou.start.process.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.annotation.api.Util.PropertyBean;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.plugin.PluginManager;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.Plugin;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.start.config.ComplexPluginConfig;
import com.skjanyou.start.config.ConfigManager;
import com.skjanyou.start.exception.StartFailException;
import com.skjanyou.start.process.PluginProcess;
import com.skjanyou.start.util.InstanceUtil;
import com.skjanyou.util.ClassUtil;
import com.skjanyou.util.CommUtil;
import com.skjanyou.util.ResourcesUtil;
import com.skjanyou.util.ScanUtil;
import com.skjanyou.util.StringUtil;
import com.skjanyou.util.convert.ConvertUtil;

public class DefaultPluginProcess implements PluginProcess {
	private Logger logger = LogUtil.getLogger(DefaultPluginProcess.class);
	
	@Override
	public void findPlugin(ConfigManager manager, ClassLoader classLoader,List<String> pluginScanPath) {
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
					DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
					DocumentBuilder dombuilder = domfac.newDocumentBuilder();
					Document doc = dombuilder.parse(is);
					Element root = doc.getDocumentElement();
					String id = root.getAttribute("id");
					
					// 判断插件是否为启动状态
					String enableId = id + ".enable";
					String enableStart = manager.getString(enableId);
					if( enableStart != null && !Boolean.valueOf(enableStart) ) { logger.error("插件" + id + "在配置文件中设置禁用,将不会启动。如需启动,请修改配置文件,设置" + enableId +"=true。" ); continue; }
					String displayName = root.getAttribute("displayName"); //插件名称
					String activatorString = root.getAttribute("activator");
					Class activator = null;
					try{
						activator = classLoader.loadClass( activatorString ); //启动类
					} catch ( ClassNotFoundException e){
						activator = Class.forName( activatorString );
					}
					int order = Integer.valueOf(root.getAttribute("order"));				//排序
					Boolean enable = Boolean.valueOf(root.getAttribute("enable"));	//是否启动
					Boolean failOnInitError = Boolean.valueOf(root.getAttribute("failOnInitError"));						//报错时是否终止启动
					String defaultConfig = root.getAttribute("defaultConfig");	//默认配置文件路径
					String classScanPath = root.getAttribute("classScanPath");
					logger.info("扫描到插件:{id:" + id + ",displayName:" + displayName + "}");
					
					plugin = new Plugin();
					plugin.setId(id);plugin.setActivator(activator);plugin.setDisplayName(displayName);
					plugin.setEnable(enable);plugin.setFailOnInitError(failOnInitError);plugin.setOrder(order);
					plugin.setDefaultConfig(defaultConfig);plugin.setClassScanPath(classScanPath);

					PluginManager.registPlugin(plugin);
					if( !StringUtil.isBlank(classScanPath) ){
						pluginScanPath.add(classScanPath);
					}
				} catch ( Exception e) {
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

	@Override
	public void initPlugin(ConfigManager manager, ClassLoader classLoader) {
		logger.info("----------------开始加载插件----------------");
		List<Plugin> pluginList = PluginManager.getPluginList();
		PluginSupport pluginSupport = null;
		List<Class<?>> classList = null;
		PluginConfig properties = null;
		Class<? extends PluginSupport> activatorClass = null;
		for (Plugin plugin : pluginList) {
			// 启动开启的插件
			if( plugin.getEnable() ){
				logger.info("开始加载插件:{ id:" + plugin.getId() + ",displayName:" + plugin.getDisplayName() + "}");
				Properties defaultProps = CommUtil.isNullOrEmpty(plugin.getDefaultConfig()) ? new Properties() : ResourcesUtil.getInnerResources(plugin.getDefaultConfig(), classLoader);
				properties = new ComplexPluginConfig( manager, defaultProps );
				classList = CommUtil.isNullOrEmpty(plugin.getClassScanPath()) ? Collections.emptyList() : scanPluginClassList(plugin.getClassScanPath(),classLoader);
				activatorClass = plugin.getActivator();
				pluginSupport = InstanceUtil.newInstance(activatorClass);
				// 填充值
				fillPluginBeanWithProperties(activatorClass,pluginSupport,properties);
				try{
					PluginManager.initPlugin(pluginSupport,plugin, classList, properties);
				} catch( Exception e ) {
					logger.error(String.format("插件[%s]启动失败,原因:[%s]", plugin.getDisplayName(),e.getMessage()),e);
					PluginManager.getPluginList().remove(plugin);
					// 失败终止
					if(plugin.getFailOnInitError()) {
						throw new RuntimeException(String.format("插件[%s]启动失败,原因:[%s]", plugin.getDisplayName(),e.getMessage()));
					}
				}
				
				logger.info("加载插件:{ id:" + plugin.getId() + ",displayName:" + plugin.getDisplayName() + "}完成");
			}else{
				PluginManager.getPluginList().remove(plugin);
				logger.info("插件:{id:",plugin.getId(),",displayName:",plugin.getDisplayName(),"}因未启用,不会进行加载。");
			}
		}
		logger.info("----------------加载插件完成----------------");
	}

	@Override
	public void startPlugin() {
		logger.info("----------------开始启动插件----------------");
		List<PluginSupport> loadPluginList =  PluginManager.getAllLoadPlugin();
		for (PluginSupport support : loadPluginList) {
			Plugin plugin = PluginManager.getPluginByMapping(support);
			try {
				logger.info("开始启动插件:{ id:" + plugin.getId() + ",displayName:" + plugin.getDisplayName() + "}");
				support.startup();
				logger.info("启动插件:{ id:" + plugin.getId() + ",displayName:" + plugin.getDisplayName() + "}完成");
			} catch (Exception e) {
				logger.error(e);
				logger.info("启动插件:{ id:" + plugin.getId() + ",displayName:" + plugin.getDisplayName() + "}出现异常",e);
				// 失败终止
				if(plugin.getFailOnInitError()) {
					throw new RuntimeException(String.format("插件[%s]启动失败,原因:[%s]", plugin.getDisplayName(),e.getMessage()));
				}
			}
		}		
		logger.info("----------------启动插件完成----------------");
	}

	@Override
	public void shutdownPlugin() {
		logger.info("----------------开始关闭插件----------------");
		List<PluginSupport> loadPluginList =  PluginManager.getAllLoadPlugin();		
		for (PluginSupport support : loadPluginList) {
			Plugin plugin = PluginManager.getPluginByMapping(support);
			try {
				logger.info("开始关闭插件:{ id:" + plugin.getId() + ",displayName:" + plugin.getDisplayName() + "}");
				support.shutdown();
				logger.info("关闭插件:{ id:" + plugin.getId() + ",displayName:" + plugin.getDisplayName() + "}完成");

			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
				logger.info("关闭插件:{ id:" + plugin.getId() + ",displayName:" + plugin.getDisplayName() + "}出现异常",e);
			}
		}		
		logger.info("----------------关闭插件完成----------------");
	}
	/** 从列表中获取满足扫描规则的类  **/
	private static List<Class<?>> scanPluginClassList( String classScanPath,ClassLoader classLoader ){
		return ClassUtil.getClasses(classScanPath, classLoader);
	}
	
	/** 向插件类中的成员变量注入值  **/
	@SuppressWarnings("unchecked")
	private static void fillPluginBeanWithProperties( Class<?> clazz,PluginSupport pluginSupport,PluginConfig properties ){
		Field[] fileds = clazz.getDeclaredFields();
		Property property = null;
		PropertyBean propertyBean = null;
		String key = null;Object value = null;
		for (Field field : fileds) {
			property = field.getAnnotation(Property.class);
			if( property != null ){
				key = property.value();
				value = properties.getProperty(key);
				field.setAccessible(true);
				// 1.判断一下成员变量类型
				if( field.getType().isAssignableFrom(List.class) ) {
					// 多条数据,value的数据应当为[A,B]这种格式
					List list = new ArrayList<>();
					String arrString = value.toString();
					arrString = arrString.substring(1, arrString.length()-1);
					String[] arr = arrString.split(",");
					for (String cla : arr) {
						if( cla != null && !StringUtil.isBlank(cla.toString()) ){
							list.add(cla);
						}
					}
					try {
						value = list;
						field.set(pluginSupport, value);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}							
				
				}else {
					// 单个数据
					if( value != null && !StringUtil.isBlank(value.toString()) ){
						value = ConvertUtil.convert(value, field.getType());
						try {
							field.set(pluginSupport, value);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
			propertyBean = field.getAnnotation(PropertyBean.class);
			if( propertyBean != null ){
				key = propertyBean.value();
				value = properties.getProperty(key);
				field.setAccessible(true);
				// 1.判断一下成员变量类型
				if(field.getType().isAssignableFrom(List.class)){
					// 多条数据,value的数据应当为[A,B]这种格式
					List list = new ArrayList<>();
					String arrString = value.toString();
					arrString = arrString.substring(1, arrString.length()-1);
					String[] arr = arrString.split(",");
					for (String cla : arr) {
						if( ClassUtil.isClass( cla ) ){
							list.add(InstanceUtil.newInstance(ClassUtil.convert2Class(cla)));
						}
					}
					try {
						value = list;
						field.set(pluginSupport, value);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}							
				}else{
					// 单个数据
					if( value != null && !StringUtil.isBlank(value.toString()) ){
						if( ClassUtil.isClass(value.toString()) ){
							value = InstanceUtil.newInstance(ClassUtil.convert2Class(value.toString()));
							try {
								field.set(pluginSupport, value);
							} catch (IllegalArgumentException | IllegalAccessException e) {
								e.printStackTrace();
							}						
						}
					}
				}
			}
		}
		
	}

	@Override
	public void listAllPluginInfo( ConfigManager manager,ClassLoader classLoader ) {
		List<Plugin> pluginList = PluginManager.getPluginList();
		for (Plugin plugin : pluginList) {
			// 插件类
			Class<?> support = plugin.getActivator();
			// 插件名
			String id = plugin.getId();
			// 插件描述
			String displayName = plugin.getDisplayName();
			// 默认配置
			Properties defaultProperties = ResourcesUtil.getInnerResources(plugin.getDefaultConfig(), classLoader);
			// 当前配置
			ComplexPluginConfig currentProperties = new ComplexPluginConfig(manager, defaultProperties);
			
			
		}
		
		
		
		
	}
}
