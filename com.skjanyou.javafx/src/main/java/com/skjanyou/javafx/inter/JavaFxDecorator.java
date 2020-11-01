package com.skjanyou.javafx.inter;

import javafx.scene.Parent;
import javafx.stage.Stage;

public interface JavaFxDecorator {
	public void initDecorator(Stage stage);
	public JavaFxDecorator addContent( Parent parent );
}
