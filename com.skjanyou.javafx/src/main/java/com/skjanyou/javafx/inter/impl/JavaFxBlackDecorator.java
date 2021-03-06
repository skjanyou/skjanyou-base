package com.skjanyou.javafx.inter.impl;

import com.skjanyou.javafx.anno.FxAnnotation.FxDecorator;
import com.skjanyou.javafx.constant.DecoratorType;
import com.skjanyou.javafx.inter.JavaFxDecorator;
import com.skjanyou.javafx.util.DragUtil;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *   暗黑主题
 * @author skjanyou
 * 	时间 : 2020-11-7
 * 	作用 :
 */
public class JavaFxBlackDecorator implements JavaFxDecorator {
	private Stage stage;
	private Point point = new Point();
	private FxDecorator fxDecorator;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private HBox banner;
	@FXML
	private VBox stageRoot;
	@FXML
	private Label title;
	@FXML
	private ImageView iconImage;
	@FXML
	private ImageView maximize;
	@FXML
	private ImageView restore;
	
	@FXML
	private ImageView color_image;
	@FXML
	private VBox min_box;
	@FXML
	private VBox resize_box;
	@FXML
	private VBox close_box;
	
	
	private DragUtil dragUtil = new DragUtil();
    double appMinWidth = 0;
    double appMinHeight = 0;
	@Override
	public void initDecorator(Stage stage,FxDecorator fxDecorator) {
		this.stage = stage;
		this.fxDecorator = fxDecorator;
		this.bindHeader();
		this.bindDrager();
	}

	@Override
	public JavaFxBlackDecorator addContent(Parent content) {
		this.stageRoot.getChildren().add(content);
		VBox.setVgrow(content, Priority.ALWAYS);
		Pane pane = (Pane) content;
		appMinWidth = pane.getMinWidth();
		appMinHeight = pane.getMinHeight() + banner.getMinHeight();
		return this;
	}

	protected void bindHeader() {
		DecoratorType type = this.fxDecorator.type();
		switch (type) {
		case CLOSE:
			resize_box.setVisible(false);
			color_image.setVisible(false);
			min_box.setVisible(false);
			break;
		case MAX_CLOSE:
			color_image.setVisible(false);
			min_box.setVisible(false);
			break;
		case MIN_MAX_CLOSE:
			color_image.setVisible(false);
			break;
		case NONE:
			banner.setVisible(false);
			stageRoot.getChildren().removeAll(banner);
			break;
		default:
			break;
		}
		
		this.title.textProperty().bindBidirectional(stage.titleProperty());
		stage.getIcons().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable observable) {
				setIconImage();
			}
		});
		setIconImage();
		
		stage.maximizedProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				boolean maximized = stage.isMaximized();
				if(maximized) {
					// 最大化状态
					maximize.getStyleClass().add("hidden");
					maximize.setFitHeight(1);
					maximize.setFitWidth(1);
					restore.getStyleClass().remove("hidden");
					restore.setFitWidth(15);
					restore.setFitHeight(15);
				}else {
					// 最大化状态
					maximize.getStyleClass().remove("hidden");
					maximize.setFitHeight(15);
					maximize.setFitWidth(15);
					restore.getStyleClass().add("hidden");
					restore.setFitWidth(1);
					restore.setFitHeight(1);
				}
			}
		});
	}

	private void setIconImage () {
		ObservableList<Image> list = stage.getIcons();
		Image image = null;
		if( list.size() > 0 ) {
			image = stage.getIcons().get(0);
		}
		iconImage.setImage(image);
	}

	protected void bindDrager() {
		// 添加窗口移动事件
		if( fxDecorator.moveable() ) {
			banner.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					point.setX(stage.getX() - event.getScreenX());
					point.setY(stage.getY() - event.getScreenY());
				}
			});
			
			banner.setOnMouseReleased(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (stage.getY() < 0) {
						stage.setY(0);
					}
				}
			});
			
			banner.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (stage.isFullScreen()) {
						return;
					}
					
					double x = (event.getScreenX() + point.getX());
					double y = (event.getScreenY() + point.getY());
					
					Platform.runLater(() -> {
						stage.setX(x);
						stage.setY(y);
					});
				}
			});
		}

		// 添加拖拽事件
		if( fxDecorator.resizeable() ) {
			dragUtil.addDrawFunc(stage, stageRoot,this.appMinWidth,this.appMinHeight);
		}

	}

	public void min() {
		stage.setIconified(true);
	}

	public void max() {
		stage.setMaximized(!stage.isMaximized());
	}

	public void close() {
		stage.close();
	}

	public void color() {
		colorPicker.show();
	}

	public void changeColor(ActionEvent event) {
		ColorPicker colorPicker = (ColorPicker) event.getSource();
		String rgbString = "#" + Integer.toHexString(colorPicker.getValue().hashCode());
		stageRoot.setStyle(String.format("-fx-background-color:%s;",rgbString));
	}

}
