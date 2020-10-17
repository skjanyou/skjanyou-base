package com.skjanyou.javafx.inter;

import com.skjanyou.javafx.bean.LoadResult;

/**
  * 控制类工厂
 * @author skjanyou
  * 时间 : 2020-10-17
  * 作用 :
 */
public interface FxControllerFactory {
	public LoadResult createController();
}
