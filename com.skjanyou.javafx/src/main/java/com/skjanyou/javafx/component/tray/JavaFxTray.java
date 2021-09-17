package com.skjanyou.javafx.component.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.impl.DefaultFxControllerFactory;
import com.skjanyou.util.StreamUtil;
import com.skjanyou.util.StringUtil;
import com.sun.javafx.application.PlatformImpl;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

@SuppressWarnings("restriction")
public class JavaFxTray {
	private static final boolean isSupport = SystemTray.isSupported();
	private static JavaFxTray instance ;
	private int width;
	private Image image;
	private LoadResult result;
	private static boolean isMount = false;
	private BlackTrayController blackTrayController;
	private static int rowHeight = 45;
	private static TrayIcon ti;
	
	private JavaFxTray() {
		FxControllerFactory controllerFactory = new DefaultFxControllerFactory(BlackTrayController.class);
		result = controllerFactory.createController();
		blackTrayController = (BlackTrayController) result.getController();
	}
	
	public static boolean isSupport() {
		return isSupport;
	}
	
	public static JavaFxTray getTray() {
		if( !isSupport ) {
			throw new RuntimeException("系统不支持托盘");
		}
		if( instance == null ) {
			instance = new JavaFxTray();
		}
		return instance;
	}
	
	public JavaFxTray setTrayMenuItemList( TrayMenuItem... item ) {
		for (TrayMenuItem trayMenuItem : item) {
			HBox hbox = new HBox();
			hbox.getStyleClass().add("tray-row");
			hbox.setPadding(new Insets(0, 10, 0, 10));
			hbox.setMaxHeight(rowHeight);
			// 图标
			ImageView imageView = new ImageView();
			imageView.getStyleClass().add("tray-image-view");
			imageView.setFitWidth(45.0);
			imageView.setFitHeight(rowHeight);
			String icon = trayMenuItem.getIcon();
			if( !StringUtil.isBlank(icon) ) {
				InputStream input = null;
				try {
					input = StreamUtil.getInputStreamIgnoreLocation(icon);
					javafx.scene.image.Image image = new javafx.scene.image.Image(input);
					imageView.setImage(image);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			// 文字
			Label label = new Label(trayMenuItem.getTitle());
			label.getStyleClass().add("tray-title");
			label.prefWidthProperty().bind(hbox.widthProperty().subtract(45));
			label.setPrefHeight(rowHeight);
			label.setPadding(new Insets(0, 0, 0, 10));
			
			// 回调
			hbox.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
				@Override
				public void handle(javafx.scene.input.MouseEvent event) {
					PlatformImpl.startup( () -> {
						// 这里有问题,导致面板没有关闭
						if( result.getScene() == null ) {
							result.getStage().hide();
						}
					});
					if( trayMenuItem.getActionListener() != null ) { 
						trayMenuItem.getActionListener().handler( JavaFxTray.this );
					};
				}
			});

			hbox.getChildren().addAll(imageView,label);
			
			blackTrayController.root.getChildren().add(hbox);
		}
		
		return this;
	}
	
	public JavaFxTray setSize( int width ) {
		this.width = width;
		result.getStage().setWidth(width);
		blackTrayController.root.setPrefWidth(width);
		return this;
	}
	
	public JavaFxTray setIcon( String image ) {
		try {
			this.image = ImageIO.read(StreamUtil.getInputStreamIgnoreLocation(image));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	
	public JavaFxTray mount() {
		if( isMount ) {
			return this;
		}
		PlatformImpl.startup( () -> {
			result.getStage().setScene(result.getScene());
			result.getStage().focusedProperty().addListener(new InvalidationListener() {
				@Override
				public void invalidated(Observable observable) {
					if(!result.getStage().isFocused()) {
						result.getStage().hide();
					}
				}
			});
			result.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					event.consume();
				}
			});

			// 隐藏任务栏图标
			Stage primaryStage = new Stage();
	        // 设置风格为 UTILITY
	        primaryStage.initStyle(StageStyle.UTILITY);
	        // 设置父级透明度为0
	        primaryStage.setOpacity(0);

	        // 将 primaryStage 设置为归属对象，即父级窗口
	        result.getStage().initOwner(primaryStage);

	        // 先把 primaryStage 显示，再显示其他内容（顺序必须这样，因为父级必须显示，如果直接显示 mainStage, 则任务栏图标隐藏无效）
	        primaryStage.show();
	        
	        primaryStage.toBack();
		});
		
		ti = new TrayIcon(image);
		ti.setImageAutoSize(true); 
		
		ti.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if( e.getClickCount() != 2 && e.getButton() != MouseEvent.BUTTON3) {
					return ;
				}
				Point point = e.getPoint();
				PlatformImpl.startup( () -> {
					// 先做最简单的场景
					Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
					double sceenWidth = screenRectangle.getWidth();
					double sceenHeight = screenRectangle.getHeight();
					double pX = point.getX();
					double pY = point.getY();
					double resultX = 0 ,resultY = 0;
					if( pX + JavaFxTray.this.width > sceenWidth ) {
						resultX = pX - JavaFxTray.this.width;
					}else {
						resultX = pX;
					}
					
					
					result.getStage().show();
					resultY = pY - result.getStage().getHeight();
					result.getStage().setX(resultX);
					result.getStage().setY(resultY);
					result.getStage().setAlwaysOnTop(true);
					result.getStage().requestFocus();
				});
			}
		});
		isMount = true;
		return this;
	}
	
	public void show() {
		try {
			if( SystemTray.getSystemTray().getTrayIcons().length == 0 ) {
				SystemTray.getSystemTray().add(ti);
			}
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
	}
	
	public void hide() {
		// 这里涉及到线程占用问题,必须使用Swing的UI线程才可以进行移除操作
		SwingUtilities.invokeLater(()->{
			SystemTray.getSystemTray().remove(ti);
		});
	}
	
}
