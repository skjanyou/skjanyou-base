package com.skjanyou.javafx.core;

import javafx.event.Event;
import javafx.event.EventHandler;

public class EventProxyDispatcher implements EventHandler<Event> {
	// 控制器
	private ControllerHelper skjanyouController;
	
	/***
	 * @param skjanyouController	控制器
	 */
	public EventProxyDispatcher( ControllerHelper skjanyouController ) {
		this.skjanyouController = skjanyouController;
	}

	@Override
	public void handle(Event event) {
		this.skjanyouController.doMatcherAndDispatcher(event);
	}

}
