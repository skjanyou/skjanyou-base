package com.skjanyou.javafx.component.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.impl.DefaultFxControllerFactory;
import com.skjanyou.util.StreamUtil;
import com.sun.javafx.application.PlatformImpl;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
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
	private int height;
	private Image image;
	private LoadResult result;
	private static boolean isMount = false;
	
	private JavaFxTray() {
		FxControllerFactory controllerFactory = new DefaultFxControllerFactory(BlackTrayController.class);
		result = controllerFactory.createController();
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
			// 图标
			
			// 文字
			
			// 回调
			
			hbox.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

				@Override
				public void handle(javafx.scene.input.MouseEvent event) {
					trayMenuItem.getActionListener().handler();
				}

			});
		}
		
		return this;
	}
	
	public JavaFxTray setSize( int w, int h ) {
		this.width = w;
		this.height = h;
		result.getStage().setWidth(width);
		result.getStage().setHeight(height);
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
		});
		
		SystemTray st = SystemTray.getSystemTray();
		TrayIcon ti = new TrayIcon(image);
		try {
			st.add(ti);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		
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
					
					resultY = pY - JavaFxTray.this.height;
					
					result.getStage().show();
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
	
}
