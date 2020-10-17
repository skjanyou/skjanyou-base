package com.skjanyou.javafx.inter.impl;

import com.skjanyou.javafx.inter.FxEventDispatcher;

import javafx.event.Event;
import javafx.event.EventHandler;

public class DefaultFxEventDispatcher implements FxEventDispatcher,EventHandler<Event> {

	@Override
	public EventHandler<Event> getEventHandler() {
		return null;
	}


	@Override
	public void handle(Event event) {
		
	}

}
