package com.skjanyou.javafx.core;

import java.lang.reflect.Method;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

//这里需要两个事件分发器才能拿到正确的事件触发源
public class DoubleEventDispatcher implements EventDispatcher {
	// 控制器
	private SkjanyouController skjanyouController;
	
	/***
	 * @param eventDispatchOrigin 	事件委托触发源
	 * @param skjanyouController	控制器
	 */
	public DoubleEventDispatcher( Object eventDispatchOrigin,SkjanyouController skjanyouController ) {
		this.skjanyouController = skjanyouController;
		this.setEventDispatchOrigin(eventDispatchOrigin);
	}
	
	public DoubleEventDispatcher( SkjanyouController skjanyouController ) {
		this.skjanyouController = skjanyouController;
	}
	
	/** 事件分发源,如果对于布局/组件等元素,可以使用Node类型,但是Scene和Window之类的不是使用统一接口,所以这里通过反射来添加事件 **/
	public void setEventDispatchOrigin( Object eventDispatchOrigin ) {
		Class<?> clazz = eventDispatchOrigin.getClass();
		Method setEventDispatcherMethod = null;
		try {
			setEventDispatcherMethod = clazz.getMethod("setEventDispatcher", EventDispatcher.class);
			setEventDispatcherMethod.invoke(eventDispatchOrigin, this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	private EventDispatcher eventDispatcherHandler = new EventDispatcher() {
		@Override
		public Event dispatchEvent(Event event, EventDispatchChain tail) {
			DoubleEventDispatcher.this.skjanyouController.doMatcherAndDispatcher(event);
			return tail.dispatchEvent(event);
		}
	};
	
	
	@Override
	public Event dispatchEvent(Event event, EventDispatchChain tail) {
		return tail.append(eventDispatcherHandler).dispatchEvent(event);
	}

}
