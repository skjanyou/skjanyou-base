package com.skjanyou.javafx.inter;

import javafx.scene.Parent;
import javafx.stage.Stage;

public interface ControllerLifeCycle {
	/**
	 * 开始加载,此时还没有读取FXML文件
	 * @param stage stage有可能为空
	 */
	public void onLoad( Stage stage );
	/**
	 * 已经加载完FXML,故所有的@FXML\@FXContext\@EventHandler都已经注入完成
	 * @param stage 
	 */
	public void onInit( Stage stage, Parent root );
	/**
	 * 该视图开始被挂载的时候
	 * @param stage
	 */
	public void onMount( Stage stage );
}
