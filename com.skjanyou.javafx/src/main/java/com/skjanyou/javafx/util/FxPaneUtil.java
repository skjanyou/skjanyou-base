package com.skjanyou.javafx.util;

import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.impl.DefaultFxControllerFactory;

public class FxPaneUtil {
	public static<T> T createPane( Class<T> clazz ) {
		FxControllerFactory controllerFactory = new DefaultFxControllerFactory(clazz);
		LoadResult loadResult = controllerFactory.createController();
		Object result = loadResult.getController();
		if( result != null ) {
			return (T) result;
		}
		
		return null;
	}
}
