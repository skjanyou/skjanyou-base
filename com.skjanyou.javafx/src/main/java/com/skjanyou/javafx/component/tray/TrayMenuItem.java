package com.skjanyou.javafx.component.tray;

public class TrayMenuItem {
	public static class MenuItemActionListener {
		public void handler( JavaFxTray tray ) {
			
		}
	}
	
	private String icon;
	private String title;
	private MenuItemActionListener actionListener;
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public MenuItemActionListener getActionListener() {
		return actionListener;
	}
	public void setActionListener(MenuItemActionListener actionListener) {
		this.actionListener = actionListener;
	}
	
}
