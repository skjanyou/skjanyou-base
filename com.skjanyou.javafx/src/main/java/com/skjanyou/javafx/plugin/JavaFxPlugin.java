package com.skjanyou.javafx.plugin;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.javafx.anno.FxAnnotation.MainFxController;
import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.core.ApplicationContext;
import com.skjanyou.javafx.inter.ControllerLifeCycle;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.impl.DefaultFxControllerFactory;
import com.skjanyou.javafx.inter.impl.NoneControllerLifeCycle;
import com.skjanyou.plugin.PluginDefineAnnotationClassManager;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.adapter.PluginDefineAnnotationClassAdapter;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.plugin.constant.DefineAnnotationClassPosition;
import com.skjanyou.util.CommUtil;
import com.skjanyou.util.StreamUtil;
import com.sun.javafx.application.PlatformImpl;

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
	public void init(List<Class<?>> plugnInnerClass, PluginConfig properties) throws Exception  {
		PluginDefineAnnotationClassManager.regist(new PluginDefineAnnotationClassAdapter() {

			@Override
			public Class<? extends Annotation> defineClass() {
				return MainFxController.class;
			}

			@Override
			public DefineAnnotationClassPosition defineAnnotationClassPosition() {
				return DefineAnnotationClassPosition.CLASS;
			}

			@Override
			public void classProcess(Class<?> targetClass, Beandefinition beandefinition) {
				FxControllerFactory controllerFactory = new DefaultFxControllerFactory(targetClass);
				LoadResult bean = controllerFactory.createController();
				beandefinition.setBean(targetClass.getName(), bean);
			}
		});

	}

	@Override
	public void startup() throws Exception  {
		if( CommUtil.isNullOrEmpty(mainViewClass) ) {
			return ;
		}
		CountDownLatch latch = new CountDownLatch(1);
		JavaFxPluginRunnable jfpr = new JavaFxPluginRunnable(latch);
		PlatformImpl.startup(jfpr);
		latch.await();
		if( jfpr.catchException != null ) {
			throw jfpr.catchException;
		}
	}

	class JavaFxPluginRunnable implements Runnable {
		public Exception catchException;
		private CountDownLatch latch;
		public JavaFxPluginRunnable( CountDownLatch latch ) {
			this.latch = latch;
		}
		@Override
		public void run() {
			try {
				LoadResult bean = ApplicationContext.getBean(mainViewClass);
				if( bean != null ) {
					if( bean.getParent() != null ) {
						Object controller = bean.getController();
						Stage stage = bean.getStage();
						ControllerLifeCycle life = ( controller instanceof ControllerLifeCycle) ? (ControllerLifeCycle) controller : new NoneControllerLifeCycle();
						// TODO 这个地方要优化,没有加@FxDecorator注解的界面也会被去掉装饰
						Scene scene = bean.getScene();
						life.onInit(stage);
						stage.setScene(scene);
						stage.setTitle(title);
						stage.getIcons().add(new Image(StreamUtil.getInputStreamIgnoreLocation(icon)));
						stage.show();
					}else {
						throw new RuntimeException("启动失败,未加载成功");
					}
				}else {
					throw new RuntimeException("启动失败,找不到主类");
				}
			} catch (Exception e) {
				e.printStackTrace();
				catchException = e;
			} finally {
				this.latch.countDown();
			}
		}
	}

	@Override
	public void shutdown() throws Exception {

	}

}
