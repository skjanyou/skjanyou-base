package com.skjanyou.desktop.window.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import com.skjanyou.desktop.window.Window;


public class AbstractBrowserWindow implements Window {
	private Browser browser;
	private Shell shell;
	
	public AbstractBrowserWindow(  ){
		shell = SwtResourcesManager.createSubWindow();		
		shell.setLayout(new FillLayout());
		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			SwtResourcesManager.exit();
		}
		shell.open();
	}
	
	@Override
	public Window setWidth(float width) {
		Rectangle rectangle = shell.getBounds();
		rectangle.width = (int) width;
		shell.setBounds(rectangle);
		return this;
	}

	@Override
	public Window setHeight(float height) {
		Rectangle rectangle = shell.getBounds();
		rectangle.height = (int) height;
		shell.setBounds(rectangle);	
		return this;
	}

	@Override
	public Window show() {
		shell.setVisible(true);
		return this;
	}

	@Override
	public Window hide() {
		shell.setVisible(false);
		return this;
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
		browser.setUrl(url);
		return this;
	}
	

	@Override
	public Window setHtmlString(String htmlString) {
		// TODO 原始Api不支持
		return this;
	}

	@Override
	public Window executeJscriptWithoutReturn(String javaScript) {
		browser.execute(javaScript);
		return this;
	}

	@Override
	public Object executeJscript(String javaScript) {
		return browser.evaluate(javaScript);
	}
	
}
