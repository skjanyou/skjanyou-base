package com.skjanyou.javafx.inter.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import com.skjanyou.javafx.anno.FxAnnotation.FxController;
import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.inter.FxFXMLLoader;
import com.sun.javafx.application.PlatformImpl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

@SuppressWarnings("restriction")
public class DefaultFxFXMLLoader extends FXMLLoader implements FxFXMLLoader,Callback<Class<?>, Object>,MethodInterceptor {
	private FxController fxControllerAnno;
	private Stage stage;
	public DefaultFxFXMLLoader( Class<?> controllerClass,Stage stage ) {
		this.fxControllerAnno = controllerClass.getAnnotation(FxController.class);
		this.stage = stage;
		if( fxControllerAnno == null ) {
			throw new RuntimeException("Controller类上面必须携带FxController注解");
		}
		this.setLocation(getFXMLURL());
		this.setControllerFactory(this);
	}
	
	@Override
	public LoadResult loader() {
		LoadResult loadResult = new LoadResult();
		if( PlatformImpl.isFxApplicationThread() ) {
			try {
				Parent parent = DefaultFxFXMLLoader.super.load();
				Object controller = DefaultFxFXMLLoader.super.getController();
				if( this.stage == null ) {
					this.stage = new Stage();
				}
				loadResult.setParent(parent);
				loadResult.setController(controller);
				loadResult.setStage(this.stage);
				
			} catch (IOException e) {
				throw new RuntimeException("加载FXML文件出现异常",e);
			}
		}else {
			CountDownLatch latch = new CountDownLatch(1);
			PlatformImpl.startup(new Runnable() {
				@Override
				public void run() {
					try {
						Parent parent = DefaultFxFXMLLoader.super.load();
						Object controller = DefaultFxFXMLLoader.super.getController();
						if( DefaultFxFXMLLoader.this.stage == null ) {
							DefaultFxFXMLLoader.this.stage = new Stage();
						}
						
						loadResult.setParent(parent);
						loadResult.setController(controller);
						loadResult.setStage(DefaultFxFXMLLoader.this.stage);
					} catch (IOException e) {
						throw new RuntimeException("加载FXML文件出现异常",e);
					} finally {
						latch.countDown();
					}
				}
			});
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return loadResult;
	}

	@Override
	public Object call(Class<?> param) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(param);
        enhancer.setCallback(this);
		return enhancer.create();
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		return proxy.invokeSuper(obj, args);
	}

	private URL getFXMLURL() {
		String fxml = fxControllerAnno.fxml().startsWith("/") ? fxControllerAnno.fxml().substring(1) : fxControllerAnno.fxml();
		return Thread.currentThread().getContextClassLoader().getResource(fxml);
	}
}
