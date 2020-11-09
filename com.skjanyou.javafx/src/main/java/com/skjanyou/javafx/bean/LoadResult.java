package com.skjanyou.javafx.bean;

import java.io.Serializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoadResult implements Serializable{
	private static final long serialVersionUID = 7715187253694143746L;
	private Scene scene;
	private Stage stage;
	private Object controller;
	private Parent parent;
	
	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
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
