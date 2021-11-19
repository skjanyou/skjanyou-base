package com.skjanyou.javafx.component.tray;

public class JavaFxTrayMock extends JavaFxTray {
	JavaFxTrayMock( ) {
	}
	@Override
	public void hide() {
	}
	@Override
	public JavaFxTray setTrayMenuItemList(TrayMenuItem... item) {
		return this;
	}
	@Override
	public JavaFxTray setSize(int width) {
		return this;
	}
	@Override
	public JavaFxTray setIcon(String image) {
		return this;
	}
	@Override
	public JavaFxTray mount() {
		return this;
	}
	@Override
	public void show() {
	}
	
}
