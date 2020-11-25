package com.skjanyou.javafx.component.tray;

import com.skjanyou.javafx.anno.FxAnnotation.FxController;
import com.skjanyou.javafx.anno.FxAnnotation.FxDecorator;
import com.skjanyou.javafx.constant.DecoratorType;

import javafx.scene.Scene;
import javafx.stage.Stage;

@FxController(
		id="Main",
		fxml="jfx/component/BlackTray.fxml")
@FxDecorator(
	type = DecoratorType.NONE,
	moveable = false,
	resizeable = false
)
public class BlackTrayController {
	private Stage stage;
	private Scene scene;
}
