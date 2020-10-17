package com.skjanyou.javafx.inter;

import javafx.event.Event;
import javafx.event.EventHandler;

public interface FxEventDispatcher {
	public EventHandler<Event> getEventHandler();
}
