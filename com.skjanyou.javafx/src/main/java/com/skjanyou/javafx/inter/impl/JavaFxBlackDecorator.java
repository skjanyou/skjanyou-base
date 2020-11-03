package com.skjanyou.javafx.inter.impl;

import com.skjanyou.javafx.inter.JavaFxDecorator;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFxBlackDecorator implements JavaFxDecorator {
	private Stage stage;
	private Point point = new Point();
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


	//窗体拉伸属性

	private static boolean isRight;// 是否处于右边界调整窗口状态

	private static boolean isBottomRight;// 是否处于右下角调整窗口状态

	private static boolean isBottom;// 是否处于下边界调整窗口状态

	private final static int RESIZE_WIDTH = 5;// 判定是否为调整窗口状态的范围与边界距离

	private final static double MIN_WIDTH = 300;// 窗口最小宽度

	private final static double MIN_HEIGHT = 250;// 窗口最小高度

	private double xOffset = 0;

	private double yOffset = 0;
	@Override
	public void initDecorator(Stage stage) {
		this.stage = stage;
		this.bindTitle();
		this.bindDrager();
	}

	@Override
	public JavaFxBlackDecorator addContent(Parent content) {
		this.stageRoot.getChildren().add(content);
		VBox.setVgrow(content, Priority.ALWAYS);
		return this;
	}

	protected void bindTitle() {
		this.title.textProperty().bindBidirectional(stage.titleProperty());
		stage.getIcons().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable observable) {
				setIconImage();
			}
		});
		setIconImage();
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



		stageRoot.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				event.consume();
				double x = event.getSceneX();
				double y = event.getSceneY();
				double width = stage.getWidth();
				double height = stage.getHeight();
				Cursor cursorType = Cursor.DEFAULT;// 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型

				// 先将所有调整窗口状态重置
				isRight = isBottomRight = isBottom = false;
				if (y >= height - RESIZE_WIDTH) {

					if (x <= RESIZE_WIDTH) {// 左下角调整窗口状态

					} else if (x >= width - RESIZE_WIDTH) {// 右下角调整窗口状态
						isBottomRight = true;
						cursorType = Cursor.SE_RESIZE;
					} else {// 下边界调整窗口状态
						isBottom = true;
						cursorType = Cursor.S_RESIZE;
					}
				} else if (x >= width - RESIZE_WIDTH) {// 右边界调整窗口状态
					isRight = true;
					cursorType = Cursor.E_RESIZE;
				}

				// 最后改变鼠标光标
				stageRoot.setCursor(cursorType);
			}
		});



		stageRoot.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				double x = event.getSceneX();
				double y = event.getSceneY();

				// 保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
				double nextX = stage.getX();
				double nextY = stage.getY();
				double nextWidth = stage.getWidth();
				double nextHeight = stage.getHeight();

				if (isRight || isBottomRight) {// 所有右边调整窗口状态
					nextWidth = x;
				}

				if (isBottomRight || isBottom) {// 所有下边调整窗口状态
					nextHeight = y;
				}

				if (nextWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
					nextWidth = MIN_WIDTH;
				}

				if (nextHeight <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
					nextHeight = MIN_HEIGHT;
				}

				// 最后统一改变窗口的x、y坐标和宽度、高度，可以防止刷新频繁出现的屏闪情况
				stage.setX(nextX);
				stage.setY(nextY);
				stage.setWidth(nextWidth);
				stage.setHeight(nextHeight);

				if(!isBottom && !isBottomRight && !isRight) {
					stage.setX(event.getScreenX() - xOffset);
					stage.setY(event.getScreenY() - yOffset);
				}

			}
		});



		stageRoot.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});


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
