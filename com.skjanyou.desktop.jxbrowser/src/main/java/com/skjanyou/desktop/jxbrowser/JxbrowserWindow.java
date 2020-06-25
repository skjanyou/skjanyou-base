package com.skjanyou.desktop.jxbrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.skjanyou.desktop.implant.Implant;
import com.skjanyou.desktop.implant.JsFunctionManager;
import com.skjanyou.desktop.jxbrowser.adapter.ListenerAdapter;
import com.skjanyou.desktop.window.Window;
import com.skjanyou.util.StreamUtil;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.FailLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FrameLoadEvent;
import com.teamdev.jxbrowser.chromium.events.LoadEvent;
import com.teamdev.jxbrowser.chromium.events.ProvisionalLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class JxbrowserWindow extends JFrame implements Window {
	private static final long serialVersionUID = -8862411064445056786L;
	private JxbrowserWindow $this = this;
	private Browser browser = null;
	private BrowserView browserView = null;
	private String remoteDebuggingURL = null;
	private JFrame debuggerJFrame = null;
	private Browser debuggerBrowser = null;
	private BrowserView debuggerView = null;
	private List<Implant> implantList = null;
	
	public JxbrowserWindow(){
		this.browser = new Browser();
		this.browserView = new BrowserView(browser);
		this.add(browserView,BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setWidth(700);
		this.setHeight(500);
        this.setLocationRelativeTo(null);
        this.implantList = new ArrayList<>();
        
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
        
		this.browser.addLoadListener(new ListenerAdapter() {

			@Override
			public void onDocumentLoadedInFrame(FrameLoadEvent arg0) {
				System.out.println("onDocumentLoadedInFrame");
			}

			@Override
			public void onDocumentLoadedInMainFrame(LoadEvent arg0) {
				String runnableScript = "";
				for( Implant implant : implantList){
					runnableScript = implant.getRunnableScript();
					$this.executeJscript(runnableScript);
				}
				System.out.println("onDocumentLoadedInMainFrame");
			}

			@Override
			public void onFailLoadingFrame(FailLoadingEvent arg0) {
				System.out.println("onFailLoadingFrame");
			}

			@Override
			public void onFinishLoadingFrame(FinishLoadingEvent arg0) {
				System.out.println("onFinishLoadingFrame");
				// Java方法与Js函数绑定
				JSValue window = browser.executeJavaScriptAndReturnValue("window");
				window.asObject().setProperty("Skjanyou", JsFunctionManager.INSTANCE);
			}

			@Override
			public void onProvisionalLoadingFrame( ProvisionalLoadingEvent arg0) {
				System.out.println("onProvisionalLoadingFrame");
			}

			@Override
			public void onStartLoadingFrame(StartLoadingEvent arg0) {
				System.out.println("onStartLoadingFrame");
			}
			
		});        
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
	public synchronized Window addImplant(Implant... implants) {
		for (Implant implant : implants) {
			implantList.add(implant);
		}
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
	@Override
	public Window setWindowTitle(String title) {
		this.setTitle(title);
		return this;
	}
	@Override
	public Window setWindowIcon(String image) {
		InputStream is = null;
		try {
			is = StreamUtil.getInputStreamIgnoreLocation(image);
			if( is != null ){
				this.setIconImage(ImageIO.read(is));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return this;
	}

}
