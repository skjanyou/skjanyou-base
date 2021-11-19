package com.skjanyou.javafx.component.loading;

import com.skjanyou.javafx.anno.FxAnnotation.FxController;
import com.skjanyou.javafx.inter.ControllerLifeCycle;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

@FxController(fxml = "jfx/component/loading/BlackLoading.fxml", id = "BlackLoading")
public class BlackLoading implements ControllerLifeCycle {
	private Stage stage = new Stage();
	@FXML
	private BorderPane Loading_Pane;
	public static interface CloseLoadingCallback {
		public void cancel();
	}
	
	
	@Override
	public void onLoad(Stage stage) {
		
	}

	@Override
	public void onInit(Stage stage, Parent root) {
		
	}

	@Override
	public void onMount(Stage stage) {
	}	

	/** 
	 * 关闭加载窗口
	 */
	public void closeLoading() {
		stage.hide();
	}
	
	public BlackLoading showLoading( Window parent, String titps , CloseLoadingCallback callback ) {
		stage.setWidth(parent.getWidth());
		stage.setHeight(parent.getHeight());
        stage.initOwner(parent);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        
        // show center of parent
        double x = parent.getX() + (parent.getWidth() - stage.getWidth()) / 2;
        double y = parent.getY() + (parent.getHeight() - stage.getHeight()) / 2;
        stage.setX(x);
        stage.setY(y);
        
        // scene
        Scene scene = new Scene(Loading_Pane);
        scene.setFill(null);
        scene.setRoot(Loading_Pane);
//        Loading_Pane.setStyle("-fx-background:transparent;");
        stage.setScene(scene);
        
        stage.addEventFilter(KeyEvent.KEY_RELEASED, e->{
        	if( e.getCode() == KeyCode.ESCAPE ) {
        		this.closeLoading();
        	}
        });
        stage.addEventFilter(WindowEvent.WINDOW_HIDDEN, e->{
        	if( callback != null ) {
        		callback.cancel();
        	}        	
        });
        
        stage.show();		
        return this;
	}
	
	public BlackLoading showLoading( Window parent, String titps ) {
		return showLoading(parent,titps,null);
	}


}
