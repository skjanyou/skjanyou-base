package com.skjanyou.start.process.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
					SAXReader sr = new SAXReader();
					Document document = sr.read(is);
					Element root = document.getRootElement();
					String id = root.attributeValue("id");	// 插件ID
					// 判断插件是否为启动状态
					String enableId = id + ".enable";
					String enableStart = manager.getString(enableId);
					if( enableStart != null && !Boolean.valueOf(enableStart) ) { logger.error("插件" + id + "在配置文件中设置禁用,将不会启动。如需启动,请修改配置文件,设置" + enableId +"=true。" ); continue; }
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
					if( !StringUtil.isBlank(classScanPath) ){
						pluginScanPath.add(classScanPath);
					}
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
				properties = new ComplexPluginConfig( manager, ResourcesUtil.getInnerResources(plugin.getDefaultConfig(), classLoader) );
				if( !CommUtil.isNullOrEmpty(plugin.getClassScanPath()) ){
					classList = scanPluginClassList(plugin.getClassScanPath(),classLoader);
				}else{
					classList = null;
				}
				activatorClass = plugin.getActivator();
				pluginSupport = InstanceUtil.newInstance(activatorClass);
				// 填充值
				fillPluginBeanWithProperties(activatorClass,pluginSupport,properties);
				PluginManager.initPlugin(pluginSupport, classList, properties);
				logger.info("加载插件:{ id:" + plugin.getId() + ",displayName:" + plugin.getDisplayName() + "}完成");
			}else{
				logger.info("插件:{id:",plugin.getId(),",displayName:",plugin.getDisplayName(),"}因未启用,不会进行加载。");
			}
		}
		logger.info("----------------加载插件完成----------------");
	}

	@Override
	public void startPlugin() {
		logger.info("----------------开始启动插件----------------");
		PluginManager.loadAllPlugins();
		logger.info("----------------启动插件完成----------------");
	}

	@Override
	public void shutdownPlugin() {
		logger.info("----------------开始关闭插件----------------");
		PluginManager.shutdownAllPlugins();
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
				if( value != null && !StringUtil.isBlank(value.toString()) ){
					value = ConvertUtil.convert(value, field.getType());
					try {
						field.set(pluginSupport, value);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
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
