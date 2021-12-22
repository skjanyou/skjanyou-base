package com.skjanyou.javafx.inter.builder;

import java.net.URL;

import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.inter.impl.DefaultFxFXMLLoader;

import javafx.scene.Parent;
import javafx.util.Builder;

public class Include implements Builder<Parent> {
	private String location;

	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Include() {}
	public Include( String location ) {
		this.location = location;
	}
	
	@Override
	public Parent build() {
		DefaultFxFXMLLoader loader = new DefaultFxFXMLLoader();
		loader.setLocation(getFXMLURL());
		LoadResult lr = loader.loader();
		return lr.getParent();
	}
	
	private URL getFXMLURL() {
		String fxml = location.startsWith("/") ? location.substring(1) : location;
		return Thread.currentThread().getContextClassLoader().getResource(fxml);
	}

}
