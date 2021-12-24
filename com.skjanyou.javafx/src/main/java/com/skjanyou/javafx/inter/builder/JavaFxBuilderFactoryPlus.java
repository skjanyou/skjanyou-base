package com.skjanyou.javafx.inter.builder;

import javafx.util.Builder;
import javafx.util.BuilderFactory;

public class JavaFxBuilderFactoryPlus implements BuilderFactory {
	@Override
	public Builder<?> getBuilder(Class<?> type) {
		Builder<?> builder = null;
		if( type == Include.class ) {
			builder = new Include();
		} 
		return builder;
	}

}
