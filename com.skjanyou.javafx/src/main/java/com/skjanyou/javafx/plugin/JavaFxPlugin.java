package com.skjanyou.javafx.plugin;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.javafx.anno.FxAnnotation.FxController;
import com.skjanyou.javafx.core.ApplicationContext;
import com.skjanyou.javafx.core.EventProxyDispatcher;
import com.skjanyou.javafx.core.SkjanyouFXMLLoader;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.impl.DefaultFxControllerFactory;
import com.skjanyou.plugin.PluginDefineAnnotationClassManager;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.adapter.PluginDefineAnnotationClassAdapter;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.plugin.constant.DefineAnnotationClassPosition;
import com.skjanyou.util.CommUtil;
import com.skjanyou.util.StreamUtil;
import com.sun.javafx.application.PlatformImpl;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class JavaFxPlugin implements PluginSupport{
	@Property("fx.mainViewClass")
	private String mainViewClass;
	@Property("fx.title")
	private String title;
	@Property("fx.icon")
	private String icon;

	@Override
	public void init(List<Class<?>> plugnInnerClass, PluginConfig properties) {
		PluginDefineAnnotationClassManager.regist(new PluginDefineAnnotationClassAdapter() {

			@Override
			public Class<? extends Annotation> defineClass() {
				return FxController.class;
			}

			@Override
			public DefineAnnotationClassPosition defineAnnotationClassPosition() {
				return DefineAnnotationClassPosition.CLASS;
			}

			@Override
			public void classProcess(Class<?> targetClass, Beandefinition beandefinition) {
				FxControllerFactory controllerFactory = new DefaultFxControllerFactory(targetClass);
				Object bean = controllerFactory.createController();
				beandefinition.setBean(targetClass.getName(), bean);

			}
		});

	}

	@Override
	public void startup() {
		if( CommUtil.isNullOrEmpty(mainViewClass) ) {
			return ;
		}
		PlatformImpl.startup(new Runnable() {
			@Override
			public void run() {
				try {
					Object bean = ApplicationContext.getBean(mainViewClass);
					if( bean instanceof SkjanyouFXMLLoader ) {
						SkjanyouFXMLLoader loader = (SkjanyouFXMLLoader) bean;
						
						if( loader.getSkjanyouController().getRoot() != null ) {
							Parent root = loader.getSkjanyouController().getRoot();
							Stage stage = new Stage();
							Scene scene = new Scene(root);
							
							stage.setScene(scene);
							System.out.println(title);
							stage.setTitle(title);
							stage.getIcons().add(new Image(StreamUtil.getInputStreamIgnoreLocation(icon)));
							stage.show();
						}else {
							throw new RuntimeException("启动失败");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
				}
			}
		});

	}

	@Override
	public void shutdown() {

	}

}
