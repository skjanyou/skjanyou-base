package com.skjanyou.javafx.plugin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.core.FxPluginDefineAnnotationClassAdapter;
import com.skjanyou.plugin.PluginDefineAnnotationClassManager;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.PluginConfig;
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
	private FxPluginDefineAnnotationClassAdapter fxPluginDefineAnno;

	@Override
	public void init(List<Class<?>> plugnInnerClass, PluginConfig properties) throws Exception  {
		fxPluginDefineAnno = new FxPluginDefineAnnotationClassAdapter();
		PluginDefineAnnotationClassManager.regist(fxPluginDefineAnno);
	}

	@Override
	public void startup() throws Exception  {
		if( CommUtil.isNullOrEmpty(mainViewClass) ) {
			return ;
		}
		// 获取注解遍历器创建的LoadResult
		Map<String,LoadResult> loadResultMap = fxPluginDefineAnno.getLoadResultList();
		
		CountDownLatch latch = new CountDownLatch(1);
		JavaFxPluginRunnable jfpr = new JavaFxPluginRunnable(latch,loadResultMap);
		PlatformImpl.startup(jfpr);
		latch.await();
		if( jfpr.catchException != null ) {
			PlatformImpl.exit();
			throw jfpr.catchException;
		}
	}

	class JavaFxPluginRunnable implements Runnable {
		public Exception catchException;
		private CountDownLatch latch;
		private Map<String,LoadResult> loadResultMap;
		public JavaFxPluginRunnable( CountDownLatch latch,Map<String,LoadResult> loadResultMap ) {
			this.latch = latch;
			this.loadResultMap = loadResultMap;
		}
		@Override
		public void run() {
			try {
				LoadResult bean = loadResultMap.get(mainViewClass);
				if( bean != null ) {
					if( bean.getParent() != null ) {
						Stage stage = bean.getStage();
						// TODO 这个地方要优化,没有加@FxDecorator注解的界面也会被去掉装饰
						Scene scene = bean.getScene();
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
