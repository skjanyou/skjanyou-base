package com.skjanyou.javafx.component;

import com.skjanyou.javafx.component.tray.JavaFxTray;
import com.skjanyou.javafx.component.tray.TrayMenuItem;
import com.skjanyou.javafx.component.tray.TrayMenuItem.MenuItemActionListener;

public class JavaFxTrayTest {

	public static void main(String[] args) {
		TrayMenuItem item = new TrayMenuItem();
		item.setActionListener(new MenuItemActionListener() {
			@Override
			public void handler() {
				System.out.println("click");
			}
		});
		item.setIcon("classpath:jfx/icon/liulian.png");
		item.setTitle("Exit");
		JavaFxTray tray = JavaFxTray.getTray()
				.setSize(150, 200)
				.setIcon("classpath:jfx/icon/liulian.png")
				.setTrayMenuItemList(item)
				.mount();
	}
}
