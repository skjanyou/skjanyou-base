package com.skjanyou.desktop.shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class BrowserShellWindow implements Window {

	private ShellWindow window = new ShellWindow();
	
	public BrowserShellWindow() {
		Display.getDefault().syncExec(window);
	}
	
	@Override
	public Window setWidth(float width) {
		return this;
	}

	@Override
	public Window setHeight(float height) {
		return this;
	}

	@Override
	public Window show() {
		window.show();
		return this;
	}

	@Override
	public Window hide() {
		window.hide();
		return this;
	}

	@Override
	public Window closeWindow() {
		return null;
	}

	
	private class ShellWindow extends Thread {
		private Display display;
		private Browser browser;
		private Shell shell;
		private boolean isRunning;
		
		public ShellWindow(){
			display = new Display();
			shell = new Shell(display);
			shell.setLayout(new FillLayout());
			try {
				browser = new Browser(shell, SWT.NONE);
			} catch (SWTError e) {
				display.dispose();
			}
			isRunning = true;
			browser.setUrl("https://www.baidu.com"); 
		}
		
		public void setUrl( String url ){
			browser.setUrl(url);
		}
		
		public void executeWithoutReturn( String script ){
			browser.execute(script);
		}
		
		public Object executeHasReturn( String script ){
			return browser.evaluate(script);
		}
		
		public void show(){
			shell.setVisible(true);
		}
		
		public void hide(){
			shell.setVisible(false);
		}
		
		@Override
		public void run() {
			shell.open();
			while (!shell.isDisposed() && isRunning) {
				if (!display.readAndDispatch()) display.sleep();
				
			}			
		}
		
	}

}
