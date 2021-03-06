package com.skjanyou.javafx.inter.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skjanyou.javafx.anno.FxAnnotation.FxEventHandler;
import com.skjanyou.javafx.inter.FxEventDispatcher;
import com.skjanyou.util.MethodUtil;
import com.sun.javafx.css.Selector;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.text.Text;


/**
 * <pre>
 * 事件分发器,基于过滤器的父组件事件分发器
 * 1.先搜索类中的所有事件,对事件进行分组,在捕获到不在其中的事件时,不进行处理（提升效率）  完成
 * 2.收集所有的选择器，按照事件,对选择器进行分组
 * 3.先通过事件类型过滤掉一部分无用事件，然后通过事件获取选择器，
 * 4.遍历当前目标组件以及父组件，通过选择器进行匹配，找到满足要求的目标组件
 * 5.替换event对象中的源组件
 * 6.调用类中的handler进行处理
 * </pre>
 * @author skjanyou
 *
 */
@SuppressWarnings("restriction")
public class DefaultFxEventDispatcher implements FxEventDispatcher,EventHandler<Event> {
	protected Object controller;
	protected Class<?> targetClass;
	protected Set<String> eventTypeSet; 		//  保存所有的事件类型
	protected Map<String,Set<Selector>> eventTypeAndSelectorMap;   // 事件类型与选择器的映射
	protected Map<Selector,Set<Method>> selectorMethodMap;	// 保存选择器对应的所有方法
	
	protected Map<String,Map<Selector,List<Method>>> mappingCache;   // 缓存,初次加载之后会缓存事件监听器，后续再次调用时,可以通过缓存查找，加快效率

	public DefaultFxEventDispatcher(Object controller, Class<?> targetClass) {
		this.controller = controller;
		this.targetClass = targetClass;
		if( this.controller == null || this.targetClass == null ) {
			throw new IllegalArgumentException("未设置controller以及目标类对象");
		}
		this.initMethodCollection();
		this.resolveClass();
	}


	/** 初始化方法容器 **/
	protected void initMethodCollection() {
		this.eventTypeSet = new HashSet<>();
		this.eventTypeAndSelectorMap = new HashMap<>();
		this.selectorMethodMap = new HashMap<>();
		this.mappingCache = new HashMap<>();
	}

	/** 解析类中的方法 **/
	protected void resolveClass() {
		Method[] methods = targetClass.getMethods();
		FxEventHandler handler = null;
		boolean bubble;
		String[] selectors;
		String[] eventTypes;
		for (Method method : methods) {
			handler = method.getAnnotation(FxEventHandler.class);
			if( handler != null ) {
				bubble = handler.bubble();
				selectors = handler.selectors();
				eventTypes = handler.eventType();
				this.eventTypeSet.addAll(Arrays.asList(eventTypes));
				
				for (String eventType : eventTypes) {
					Set<Selector> selectorSet = this.eventTypeAndSelectorMap.get(eventType) == null ? new HashSet<>() : this.eventTypeAndSelectorMap.get(eventType);
					for (String cssSelector : selectors) {
						Selector selector = Selector.createSelector(cssSelector);
						// 选择器对应的所有方法进行处理
						Set<Method> methodSet = this.selectorMethodMap.get(selector);
						if( methodSet == null ) {
							methodSet = new HashSet<>();
						}
						methodSet.add(method);
						this.selectorMethodMap.put(selector, methodSet);
						
						selectorSet.add(selector);
					}
					
					this.eventTypeAndSelectorMap.put(eventType, selectorSet);
				}

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
		// 过滤掉未捕获事件
		if( !this.eventTypeSet.contains(eventTypeName) ) { return ; }
		
		// 当前要分发的事件对于的选择器
		Set<Selector> selectorList = this.eventTypeAndSelectorMap.get(eventTypeName);
		
		// 遍历选择器列表,选择满足条件额组件
		for (Selector selector : selectorList) {
			// 遍历当前目标组件以及父组件，通过选择器进行匹配，找到满足要求的目标组件
			// 事件目标
			Object target = event.getTarget();
			// 1.在缓存中查找
			boolean hasHit = false;
			Map<Selector,List<Method>> map = this.mappingCache.get(eventTypeName);
			if( map != null ) {
				List<Method> methodList = map.get(selector);
				if( methodList != null && methodList.size() != 0 ) {
					Event newEvent = event;
					Node parent = null;
					// 处理一下事件对象
					if( target instanceof Node ){
						parent = (Node) target;
						if( target instanceof Text) {
							// Button之类的组件上面的文字,需要获取Parent作为事件目标,并用来做匹配
							Text tt = (Text) target;
							parent = tt.getParent();
							newEvent = event.copyFor(event.getSource(), parent);
						}			
					}
					if( selector.applies(parent) ) {
						hasHit = true;
						
						// 触发绑定选择器的方法
						for( Method tMethod : methodList ) {
							MethodUtil.invokeAndIgnoreException(tMethod, controller, new Object[]{ newEvent });
						}
					}
				}
			}
			
			// 2.缓存未命中,继续解析
			if( !hasHit ) {
				List<Method> result = loopMatcherSelector(target,selector,event);
				Map<Selector,List<Method>> selectorMap = this.mappingCache.get(eventTypeName);
				if( selectorMap == null ) {
					selectorMap = new HashMap<>();
				}
				selectorMap.put(selector, result);
				this.mappingCache.put(eventTypeName, selectorMap);
			}
		}
	}
	

	private List<Method> loopMatcherSelector( Object target,Selector selector,Event event ) {
		List<Method> result = new ArrayList<>();
		Node parent = null;
		Event newEvent = event;
		
		if( target instanceof Node ){
			parent = (Node) target;
			if( target instanceof Text) {
				// Button之类的组件上面的文字,需要获取Parent作为事件目标,并用来做匹配
				Text tt = (Text) target;
				parent = tt.getParent();
				newEvent = event.copyFor(event.getSource(), parent);
			}			
		}		
		// 递归临界值
		if( parent == null || parent.getStyleableParent() == null ) {
			return result;
		}
		
		// 待匹配搜索到的方法,全部找出来再执行,免得多个选择器的情况下,方法执行间隔过长
		Set<Method> targetMethod = new HashSet<>();
		boolean matchResult = selector.applies(parent);
		if( matchResult ) {
			Set<Method> methods = selectorMethodMap.get(selector);
			targetMethod.addAll(methods);
		}
		
		// 触发绑定选择器的方法
		for( Method tMethod : targetMethod ) {
			MethodUtil.invokeAndIgnoreException(tMethod, controller, new Object[]{ newEvent });
		}

		result = new ArrayList<>(targetMethod);
		
		// 递归调用父组件
		List<Method> parentResult = loopMatcherSelector(parent.getParent(),selector,event);
		result.addAll(parentResult);
		
		return result;
	}

}
