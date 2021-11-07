package com.skjanyou.javafx.component.loading;

import com.skjanyou.javafx.anno.FxAnnotation.FxController;
import com.skjanyou.javafx.inter.ControllerLifeCycle;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@FxController(fxml = "jfx/component/loading/BlackLoading.fxml", id = "BlackLoading")
public class BlackLoading implements ControllerLifeCycle {
	@FXML
	private BorderPane Loading_Pane;
	
	
	@Override
	public void onLoad(Stage stage) {
		
	}

	@Override
	public void onInit(Stage stage, Parent root) {
		
	}

	@Override
	public void onShow(Stage stage) {
		
	}

	@Override
	public void onDestroy(Stage stage) {
		
	}

	public void showLoading( Stage parent, String titps ) {
		Stage stage = new Stage();
        stage.initOwner(parent);
        
        // show center of parent
        double x = parent.getX() + (parent.getWidth() - stage.getWidth()) / 2;
        double y = parent.getY() + (parent.getHeight() - stage.getHeight()) / 2;
        stage.setX(x);
        stage.setY(y);
        
        // scene
        Scene scene = new Scene(Loading_Pane);
        scene.setFill(null);
        stage.setScene(scene);
        stage.setWidth(120);
        stage.setHeight(120);
        
        stage.show();
	}
}
