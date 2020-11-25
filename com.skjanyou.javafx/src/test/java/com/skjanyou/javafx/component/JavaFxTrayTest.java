package com.skjanyou.javafx.component;

import com.skjanyou.javafx.component.tray.JavaFxTray;

public class JavaFxTrayTest {

	public static void main(String[] args) {
		JavaFxTray tray = JavaFxTray.getTray().setSize(300, 400).setIcon("classpath:jfx/icon/liulian.png").mount();
	}
}
