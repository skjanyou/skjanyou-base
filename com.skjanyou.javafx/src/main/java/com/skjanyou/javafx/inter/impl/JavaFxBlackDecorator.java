package com.skjanyou.javafx.inter.impl;

import java.io.FileNotFoundException;

import com.skjanyou.javafx.inter.JavaFxDecorator;
import com.skjanyou.util.StreamUtil;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
