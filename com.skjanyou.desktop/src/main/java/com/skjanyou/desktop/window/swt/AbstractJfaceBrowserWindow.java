package com.skjanyou.desktop.window.swt;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// TODO 这个类未完成
public class AbstractJfaceBrowserWindow extends ApplicationWindow {
	private Browser browser;
	private Shell shell;

	
	public AbstractJfaceBrowserWindow(Shell parentShell) {
		super(parentShell);
		shell = parentShell;
		setBlockOnOpen(true);
	}
	
	public AbstractJfaceBrowserWindow() {
		super(null);
		setBlockOnOpen(true);
	}


	@Override
	protected Control createContents(Composite parent) {
		parent.setLayout(new FillLayout());
		try {
			browser = new Browser(parent, SWT.NONE);
		} catch (SWTError e) {
			Display.getCurrent().dispose();
		}		
		return parent;
	}
	
	public AbstractJfaceBrowserWindow setUrl( String url ){
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				browser.setUrl(url);
			}
		});				
		return this;
	}
	
	public AbstractJfaceBrowserWindow setWindowText( String text ){
		shell.setText(text);
		return this;
	}
	
	public void executeWithoutReturn( String script ){
		browser.execute(script);
	}
	
	public Object executeHasReturn( String script ){
		return browser.evaluate(script);
	}
	
	public void setWidth( float width ){
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Rectangle rectangle = shell.getBounds();
				rectangle.width = (int) width;
				shell.setBounds(rectangle);
			}
		});			

	}
	
	public void setHeight( float height ){
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Rectangle rectangle = shell.getBounds();
				rectangle.height = (int) height;
				shell.setBounds(rectangle);	
			}
		});		
	}
	
	public void show(){
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				shell.setVisible(true);
			}
		});
	}
	
	public void hide(){
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				shell.setVisible(false);
			}
		});
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		shell = newShell;
		shell.setVisible(false);
		System.out.println(shell);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}
