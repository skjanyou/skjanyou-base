package com.skjanyou.javafx.component.popup;

import com.skjanyou.javafx.anno.FxAnnotation.FxContext;
import com.skjanyou.javafx.anno.FxAnnotation.FxController;
import com.skjanyou.javafx.anno.FxAnnotation.FxEventHandler;
import com.skjanyou.javafx.constant.EventTypeConst;
import com.skjanyou.javafx.inter.ControllerLifeCycle;
import com.skjanyou.javafx.util.DesktopUtil;
import com.skjanyou.javafx.util.SVGUtil;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

@FxController(fxml = "jfx/component/popup/BlackPopup.fxml", id = "BlackPopup")
public class BlackPopup implements ControllerLifeCycle {
	private static int WIDTH = 350;
	private static int HEIGHT = 100;
	private Popup popup;
	
	@FXML
	private Region tools_tegion;
	@FXML
	private Label popup_title;
	@FXML
	private Label popup_context;
	@FxContext
	private Stage stage;
	@FxContext
	private Scene scene;
	
	
	
	// X的坐标
	private static int LOCATION_X = DesktopUtil.getScreenSize().getScreenWidth() - WIDTH;	
	public BlackPopup(){
		this.popup = new Popup();
		this.popup.setX(LOCATION_X);
	}
	
	@FxEventHandler(selectors = {"#tools_tegion"},eventType = { EventTypeConst.MouseEvent.MOUSE_CLICKED })
	public void closePopup( ) {
		this.popup.hide();
	}
	
	public BlackPopup setTitle( String title ) {
		this.popup_title.setText(title);
		return this;
	}
	
	public BlackPopup setContext( String context ) {
		this.popup_context.setText(context);
		return this;
	}
	
	public void show(double anchorX, double anchorY) {
		// 隐藏任务栏图标
		Stage primaryStage = new Stage();
        // 设置风格为 UTILITY
        primaryStage.initStyle(StageStyle.UTILITY);
        // 设置父级透明度为0
        primaryStage.setOpacity(0);
		
		Stage stage = new Stage();
		stage.initOwner(primaryStage);
		
		primaryStage.show();
		this.show(primaryStage,anchorX,anchorY);
	}
	
	public void show() {
		int location_y = DesktopUtil.getScreenSize().getScreenHeight() - DesktopUtil.getScreenSize().getTaskHeight() -  HEIGHT;
		this.show(LOCATION_X,location_y);
	}
	
	public void show(Window ownerNode, double anchorX, double anchorY) {
		this.popup.setX(anchorX);
		this.popup.setY(anchorY);
		this.popup.show(ownerNode);
	}
	
	public void show(Window owner) {
		int location_y = DesktopUtil.getScreenSize().getScreenHeight() - DesktopUtil.getScreenSize().getTaskHeight() -  HEIGHT;
		this.show(owner,LOCATION_X,location_y);
	}

	@Override
	public void onLoad(Stage stage) {
	}

	@Override
	public void onInit(Stage stage,Parent root) {
		SVGPath closeSvgPath = SVGUtil.loadSvg("classpath://icon/svg/exit.svg");
		this.tools_tegion.setShape(closeSvgPath);
		this.popup.getContent().add(root);
	}

	@Override
	public void onMount(Stage stage) {
		
	}


}
