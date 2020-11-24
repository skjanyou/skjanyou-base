package com.skjanyou.javafx.inter;

import com.skjanyou.javafx.anno.FxAnnotation.FxDecorator;

import javafx.scene.Parent;
import javafx.stage.Stage;

public interface JavaFxDecorator {
	public void initDecorator(Stage stage,FxDecorator fxDecorator);
	public JavaFxDecorator addContent( Parent parent );
}
