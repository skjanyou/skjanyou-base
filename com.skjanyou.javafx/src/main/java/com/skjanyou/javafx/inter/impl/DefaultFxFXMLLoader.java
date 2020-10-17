package com.skjanyou.javafx.inter.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

import com.skjanyou.javafx.anno.FxAnnotation.FxController;
import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.inter.FxFXMLLoader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class DefaultFxFXMLLoader extends FXMLLoader implements FxFXMLLoader,Callback<Class<?>, Object>,MethodInterceptor {
	private FxController fxControllerAnno;
	public DefaultFxFXMLLoader( Class<?> controllerClass ) {
		this.fxControllerAnno = controllerClass.getAnnotation(FxController.class);
		if( fxControllerAnno == null ) {
			throw new RuntimeException("Controller类上面必须携带FxController注解");
		}
		this.setLocation(getFXMLURL());
		this.setControllerFactory(this);
	}
	
	@Override
	public LoadResult loader() {
		LoadResult loadResult = new LoadResult();
		try {
			Parent parent = super.load();
			loadResult.setParent(parent);
		} catch (IOException e) {
			throw new RuntimeException("加载FXML文件出现异常",e);
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
		return Thread.currentThread().getContextClassLoader().getResource(fxControllerAnno.fxml());
	}
}
