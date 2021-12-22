package com.skjanyou.javafx.inter.builder;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

public class JavaFxBuilderFactoryPlus implements BuilderFactory {
	private BuilderFactory DEFAULT_BUILDER_FACTORY = new JavaFXBuilderFactory();
	@Override
	public Builder<?> getBuilder(Class<?> type) {
		Builder<?> builder = null;
		if( type == Include.class ) {
			builder = new Include();
		} else {
			builder = DEFAULT_BUILDER_FACTORY.getBuilder(type);
		}
		return builder;
	}

}
