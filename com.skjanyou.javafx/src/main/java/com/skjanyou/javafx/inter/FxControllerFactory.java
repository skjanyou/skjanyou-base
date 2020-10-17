package com.skjanyou.javafx.inter;

/**
  * 控制类工厂
 * @author skjanyou
  * 时间 : 2020-10-17
  * 作用 :
 */
public interface FxControllerFactory {
	public<R> R createController();
}
