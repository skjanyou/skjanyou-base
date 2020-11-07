package com.skjanyou.javafx.inter.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skjanyou.javafx.anno.FxAnnotation.FxEventHandler;
import com.skjanyou.javafx.inter.FxEventDispatcher;
import com.skjanyou.util.MethodUtil;
import com.skjanyou.util.StringUtil;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.text.Text;

public class DefaultFxEventDispatcher implements FxEventDispatcher,EventHandler<Event> {
	protected Object controller;
	protected Class<?> targetClass;

	public DefaultFxEventDispatcher(Object controller, Class<?> targetClass) {
		this.controller = controller;
		this.targetClass = targetClass;
		if( this.controller == null || this.targetClass == null ) {
			throw new IllegalArgumentException("未设置controller以及目标类对象");
		}
		this.initMethodCollection();
		this.resolveClass();
	}

	
	
	// 事件监听相关方法
	protected List<Method> eventHandlerMethods;   			// 有绑定事件监听的方法
	protected Map<String,List<Method>> idSelectorMethodMap;			// 通过ID选择器绑定事件的方法
	protected Map<String,List<Method>> classSelectorMethodMap;		// 通过类选择器绑定事件的方法
	protected Map<String,List<Method>> attrSelectorMethodMap;		// 通过属性选择器绑定事件的方法
	protected Map<String,List<Method>> complexSelectorsMethodMap;	// 通过混合选择器绑定事件的方法


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
	
	@Override
	public EventHandler<Event> getEventHandler() {
		return this;
	}


	@Override
	public void handle(Event event) {
		String eventTypeName = event.getEventType().getName();
		// 事件目标
		Object target = event.getTarget();
		Parent parent = null;
		
		if( target instanceof Parent ) {
			parent = (Parent) target;
		} else if( target instanceof Text) {
			// Button之类的组件上面的文字,需要获取Parent作为事件目标,并用来做匹配
			Text tt = (Text) target;
			parent = tt.getParent();
			event = event.copyFor(event.getSource(), parent);
		}else if( target instanceof Node ){
			Node node = (Node) target;
			parent = node.getParent();
		}



		// 全部找出来再执行,免得多个选择器的情况下,方法执行间隔过长
		List<Method> idMethod = new ArrayList<>();
		List<Method> classListMethod = new ArrayList<>();
		String id = parent.getId();
		List<String> classList = parent.getStyleClass().subList(0, parent.getStyleClass().size());
		// 1. 搜索ID
		if( idSelectorMethodMap.containsKey(id) ) {
			idSelectorMethodMap.get(id).forEach( idItem -> {
				FxEventHandler handler = idItem.getAnnotation(FxEventHandler.class);
				String[] eventTypeArr = handler.eventType();
				for (String eventType : eventTypeArr) {
					if( eventTypeName.equals(eventType) ) {
						idMethod.add(idItem);
						return ;
					}
				}
			});

		}

		// 2.搜索Class
		for (String clazz : classList) {
			if( classSelectorMethodMap.get(clazz) != null ) {
				classSelectorMethodMap.get(clazz).forEach( idItem -> {
					FxEventHandler handler = idItem.getAnnotation(FxEventHandler.class);
					String[] eventTypeArr = handler.eventType();
					for (String eventType : eventTypeArr) {
						if( eventTypeName.equals(eventType) ) {
							classListMethod.add(idItem);
							return ;
						}
					}
				});
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
