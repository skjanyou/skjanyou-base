package com.skjanyou.javafx.inter;

import javafx.stage.Stage;

public interface ControllerLifeCycle {
	public void onLoad( Stage stage );
	public void onInit( Stage stage );
	public void onShow( Stage stage );
	public void onDestroy( Stage stage );
}
