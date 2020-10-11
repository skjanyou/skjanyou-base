package com.skjanyou.javafx.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skjanyou.javafx.anno.FxAnnotation.FxEventHandler;
import com.skjanyou.util.MethodUtil;
import com.skjanyou.util.StringUtil;

import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

/**
 * 	代理控制器,提供事件监听,响应式数据等功能</br>
 *  1. 选择器优先级 ID > Class > attr > complex
 * 	@author skjanyou
 * 	时间 : 2020-10-11
 * 	作用 :
 */
public class SkjanyouController {
	/**  **/
	protected Parent root;
	
	
	protected Object controller;
	protected Class<?> targetClass;
	
	// 事件监听相关方法
	protected List<Method> eventHandlerMethods;   			// 有绑定事件监听的方法
	protected Map<String,List<Method>> idSelectorMethodMap;			// 通过ID选择器绑定事件的方法
	protected Map<String,List<Method>> classSelectorMethodMap;		// 通过类选择器绑定事件的方法
	protected Map<String,List<Method>> attrSelectorMethodMap;		// 通过属性选择器绑定事件的方法
	protected Map<String,List<Method>> complexSelectorsMethodMap;	// 通过混合选择器绑定事件的方法
	
	// 响应式Bean相关
	private List<Field> responsiveBeanFields;
	
	public SkjanyouController( Object controller,Class<?> targetClass ) {
		this.controller = controller;
		this.targetClass = targetClass;
		this.initMethodCollection();
		this.resolveClass();
	}
	
	/** 初始化方法容器 **/
	protected void initMethodCollection() {
		this.eventHandlerMethods = new ArrayList<>();
		this.idSelectorMethodMap = new HashMap<>();
		this.classSelectorMethodMap = new HashMap<>();
		this.attrSelectorMethodMap = new HashMap<>();
		this.complexSelectorsMethodMap = new HashMap<>();
	}
	
	/** 解析类中的方法 **/
	protected void resolveClass() {
		Method[] methods = targetClass.getMethods();
		FxEventHandler handler = null;
		String id;
		String attr;
		String clazz;
		boolean bubble;
		String[] complexSelectors;
		for (Method method : methods) {
			handler = method.getAnnotation(FxEventHandler.class);
			if( handler != null ) {
				id = handler.id();
				clazz = handler.clazz();
				attr = handler.attr();
				bubble = handler.bubble();
				complexSelectors = handler.complexSelectors();
				
				checkAndFillMap(this.idSelectorMethodMap,id,method);
				checkAndFillMap(this.classSelectorMethodMap,clazz,method);
				checkAndFillMap(this.attrSelectorMethodMap,attr,method);
				
//				checkAndFillMap(this.complexSelectorsMethodMap,complexSelectors,method);
				
			}
			
		}
		
	}
	
	private void checkAndFillMap( Map<String,List<Method>> targetMethodMethod,String key,Method tMethod ) {
		if( !StringUtil.isBlank(key) ) {
			List<Method> tempList = null; 
			tempList = targetMethodMethod.get(key);
			if( tempList == null ) {
				tempList = new ArrayList<>();
				tempList.add(tMethod);
				targetMethodMethod.put(key, tempList);
			}else {
				tempList.add(tMethod);
			}
		}
	}
	
	
	
	public void doMatcherAndDispatcher( Event event ) {
		Object source = event.getSource();
		if(!(event instanceof MouseEvent)) {return;}
		if( event.getEventType() != MouseEvent.MOUSE_CLICKED ) {return;}
		if( source instanceof Parent ) {
			Parent parent = (Parent) source;
			// 全部找出来再执行,免得多个选择器的情况下,方法执行间隔过长
			List<Method> idMethod = new ArrayList<>();
			List<Method> classListMethod = new ArrayList<>();
			String id = parent.getId();
			List<String> classList = parent.getStyleClass().subList(0, parent.getStyleClass().size());
			// 1. 搜索ID
			if( idSelectorMethodMap.containsKey(id) ) {
				idMethod.addAll(idSelectorMethodMap.get(id));
			}
			
			// 2.搜索Class
			for (String clazz : classList) {
				if( classSelectorMethodMap.get(clazz) != null ) {
					classListMethod.addAll(classSelectorMethodMap.get(clazz));
				}
			}
			
			// 3.触发绑定Id选择器的方法
			for( Method tMethod : idMethod ) {
				MethodUtil.invokeAndIgnoreException(tMethod, controller, new Object[]{ event });
			}
			// 4.触发绑定类选择器的方法
			for( Method tMethod : classListMethod ) {
				MethodUtil.invokeAndIgnoreException(tMethod, controller, new Object[]{ event });
			}
		}
		
	}

	public Object getController() {
		return controller;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public Parent getRoot() {
		return root;
	}
	
}
