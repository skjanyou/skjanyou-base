package com.skjanyou.javafx.util;

import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.component.popup.BlackPopup;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.impl.DefaultFxControllerFactory;

public class FxPaneUtil {
	public static<T> T createPane( Class<T> clazz ) {
		FxControllerFactory controllerFactory = new DefaultFxControllerFactory(BlackPopup.class);
		LoadResult loadResult = controllerFactory.createController();
		Object result = loadResult.getController();
		
		// 这里获得的是CGlib代理后的类,不能强转成父类
		if( result != null ) {
			return (T) result;
		}
		
		return null;
	}
}
