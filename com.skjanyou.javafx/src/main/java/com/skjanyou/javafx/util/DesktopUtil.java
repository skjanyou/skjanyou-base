package com.skjanyou.javafx.util;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JWindow;

public class DesktopUtil {
	public static class ScreenSize {
		private int taskHeight;
		private int screenWidth;
		private int screenHeight;
		
		public ScreenSize(int taskHeight, int screenWidth, int screenHeight) {
			super();
			this.taskHeight = taskHeight;
			this.screenWidth = screenWidth;
			this.screenHeight = screenHeight;
		}
		public int getTaskHeight() {
			return taskHeight;
		}
		public void setTaskHeight(int taskHeight) {
			this.taskHeight = taskHeight;
		}
		public int getScreenWidth() {
			return screenWidth;
		}
		public void setScreenWidth(int screenWidth) {
			this.screenWidth = screenWidth;
		}
		public int getScreenHeight() {
			return screenHeight;
		}
		public void setScreenHeight(int screenHeight) {
			this.screenHeight = screenHeight;
		}
		
	}
	private static Insets taskSet = Toolkit.getDefaultToolkit().getScreenInsets((new JWindow()).getGraphicsConfiguration());
	private static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private static ScreenSize screenSize = new ScreenSize(taskSet.bottom,(int)screen.getWidth(),(int)screen.getHeight());
	
	public static ScreenSize getScreenSize() {
		return screenSize;
	}
}
