package com.skjanyou.desktop.jxbrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.skjanyou.desktop.implant.Implant;
import com.skjanyou.desktop.window.Window;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class JxbrowserWindow extends JFrame implements Window {
	private static final long serialVersionUID = -8862411064445056786L;
	private Browser browser = null;
	private BrowserView browserView = null;
	private String remoteDebuggingURL = null;
	private JFrame debuggerJFrame = null;
	private Browser debuggerBrowser = null;
	private BrowserView debuggerView = null;
	
	public JxbrowserWindow(){
		this.browser = new Browser();
		this.browserView = new BrowserView(browser);
		this.add(browserView,BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setWidth(700);
		this.setHeight(500);
        this.setLocationRelativeTo(null);
        
        // debug
        this.debuggerJFrame = new JFrame();
        this.debuggerBrowser = new Browser();
        this.debuggerView = new BrowserView(debuggerBrowser);
        this.remoteDebuggingURL = browser.getRemoteDebuggingURL();
        System.out.println(this.remoteDebuggingURL);
        this.debuggerJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.debuggerJFrame.add(debuggerView, BorderLayout.CENTER);
        this.debuggerJFrame.setSize(700, 500);
        this.debuggerJFrame.setLocationRelativeTo(null);
        this.debuggerJFrame.setVisible(false);
        this.debuggerBrowser.loadURL(remoteDebuggingURL);
	}
	@Override
	public Window setWidth(float width) {
		Dimension d = getSize();
		d.width = (int) width;
		setSize(d);
		return this;
	}

	@Override
	public Window setHeight(float height) {
		Dimension d = getSize();
		d.height = (int) height;
		setSize(d);		
		return this;
	}

	@Override
	public void showWindow() {
		setVisible(true);
	}

	@Override
	public void hideWindow() {
		setVisible(false);
	}

	@Override
	public Window setLocation(float x, float y) {
		return this;
	}

	@Override
	public Window destroy() {
		return this;
	}

	@Override
	public Window setUrl(String url) {
		browser.loadURL(url);
		return this;
	}

	@Override
	public Window setHtmlString(String htmlString) {
		browser.loadHTML(htmlString);
		return this;
	}

	@Override
	public Window executeJscriptWithoutReturn(String javaScript) {
		browser.executeJavaScript(javaScript);
		return this;
	}

	@Override
	public Object executeJscript(String javaScript) {
		JSValue jsValue = browser.executeJavaScriptAndReturnValue(javaScript);
		return jsValue.asObject();
	}

	@Override
	public Window addImplant(Implant... implants) {
		return this;
	}
	
	@Override
	public Window windowMoveable(boolean moveable) {
		return this;
	}
	
	@Override
	public Window windowResizeable(boolean resizeable) {
		this.setResizable(resizeable);
		return this;
	}

}
