package com.skjanyou.javafx.inter.builder;

import java.net.URL;
import java.util.HashMap;

import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.impl.DefaultFxControllerFactory;
import com.skjanyou.javafx.inter.impl.DefaultFxFXMLLoader;
import com.skjanyou.util.convert.ConvertUtil;

import javafx.scene.Parent;
import javafx.util.Builder;

public class Include extends HashMap<String,Object> implements Builder<Parent> {
	private static final long serialVersionUID = 8869204140430174769L;

	@SuppressWarnings("unchecked")
	public<T> T getByType( String name, Class<T> type ) {
		T result = null;
		Object value = get(name);
		if( value != null ) {
			result = (T) ConvertUtil.convert(value, type);
		}
		return result;
	}
	
	@Override
	public Parent build() {
		Class<?> controller = getByType("controller",Class.class);
		String location = getByType("location",String.class);
		boolean visible = getByType("visible",Boolean.class);
		String styleClass = getByType("styleClass", String.class);
		
		LoadResult result = null;
		if( controller != null ) {
			FxControllerFactory factory = new DefaultFxControllerFactory(controller);
			result = factory.createController();
		}else if( location != null ) {
			DefaultFxFXMLLoader loader = new DefaultFxFXMLLoader();
			loader.setLocation(getFXMLURL(location));
			loader.setBuilderFactory(new JavaFxBuilderFactoryPlus());
			result = loader.loader();
		}else{
			throw new IllegalArgumentException("使用Include组件,必须传location/controller其中一个");
		}
		Parent parent = result.getParent();
		parent.getStyleClass().addAll(styleClass);
		parent.setVisible(visible);
		return parent;
	}
	
	private URL getFXMLURL( String location ) {
		String fxml = location.startsWith("/") ? location.substring(1) : location;
		return Thread.currentThread().getContextClassLoader().getResource(fxml);
	}

}
