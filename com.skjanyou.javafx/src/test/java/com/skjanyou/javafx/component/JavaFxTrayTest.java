package com.skjanyou.javafx.component;

import com.skjanyou.javafx.component.tray.JavaFxTray;
import com.skjanyou.javafx.component.tray.TrayMenuItem;
import com.skjanyou.javafx.component.tray.TrayMenuItem.MenuItemActionListener;

public class JavaFxTrayTest {

	public static void main(String[] args) {
		TrayMenuItem item = new TrayMenuItem();
		item.setActionListener(new MenuItemActionListener() {
			@Override
			public void handler( JavaFxTray tray ) {
				System.out.println("click hide");
				tray.hide();
			}
		});
		item.setIcon("classpath:jfx/icon/liulian.png");
		item.setTitle("Exit");

		TrayMenuItem item2 = new TrayMenuItem();
		item2.setActionListener(new MenuItemActionListener() {
			@Override
			public void handler( JavaFxTray tray ) {
				System.out.println("click show");
				tray.show();
			}
		});
		item2.setIcon("classpath:jfx/icon/liulian.png");
		item2.setTitle("Exit");
		
		JavaFxTray tray = JavaFxTray.getTray()
				.setSize(200)
				.setIcon("classpath:jfx/icon/liulian.png")
				.setTrayMenuItemList(item,item2)
				.mount();
		tray.show();
	}
}
