package com.skjanyou.desktop.swt;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import com.skjanyou.desktop.implant.Filter;
import com.skjanyou.desktop.implant.Implant;
import com.skjanyou.desktop.window.Window;
import com.skjanyou.util.StreamUtil;


public class AbstractBrowserWindow implements Window {
	private Browser browser;
	private Shell shell;
	private List<Implant> implantList = new ArrayList<>();
	
	public AbstractBrowserWindow(  ){
		shell = SwtResourcesManager.createSubWindow();		
		shell.setLayout(new FillLayout());
		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			SwtResourcesManager.exit();
		}
		browser.addProgressListener(new ProgressListener() {
			@Override
			public void completed(ProgressEvent arg0) {
				for (Implant implant : implantList) {
					String script = implant.getRunnableScript();
					browser.execute( script );
				}
			}
			@Override
			public void changed(ProgressEvent arg0) {}
		});
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
	public void showWindow() {
		shell.setVisible(true);
	}

	@Override
	public void hideWindow() {
		shell.setVisible(false);
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

	@Override
	public Window addImplant(Implant... implants) {
		for (Implant implant : implants) {
			implantList.add(implant);
		}
		return this;
	}

	@Override
	public Window windowMoveable(boolean moveable) {
		// TODO 原始Api不支持
		return this;
	}

	@Override
	public Window windowResizeable(boolean resizeable) {
		// TODO 原始Api不支持
		return this;
	}

	@Override
	public Window setWindowTitle(String title) {
		shell.setText(title);
		return this;
	}

	@Override
	public Window setWindowIcon(String image) {
		InputStream is = null;
		try {
			is = StreamUtil.getInputStreamIgnoreLocation(image);
			if( is != null ){
				shell.setImage(new Image(shell.getDisplay(), new ImageData(is)));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		
		return this;
	}

	@Override
	public Window addFilter(Filter... filters) {
		return this;
	}
	
	@Override
	public Window setFilters(List<Filter> filters) {
		return this;
	}	
}
