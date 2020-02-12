package com.skjanyou.desktop.jxbrowser;

import com.skjanyou.desktop.window.Window;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;

public class JxbrowserWindowTest {

	public static void main(String[] args) {
        BrowserPreferences.setChromiumSwitches("--remote-debugging-port=9221");
		Window window = new JxbrowserWindow();
		window.setUrl("https://www.baidu.com");
		window.showWindow();
		
	}

}
