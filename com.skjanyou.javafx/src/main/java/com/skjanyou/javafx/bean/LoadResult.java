package com.skjanyou.javafx.bean;

import javafx.scene.Parent;

public class LoadResult {
	private Object controller;
	private Parent parent;
	public Object getController() {
		return controller;
	}
	public void setController(Object controller) {
		this.controller = controller;
	}
	public Parent getParent() {
		return parent;
	}
	public void setParent(Parent parent) {
		this.parent = parent;
	}
}
