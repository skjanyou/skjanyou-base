package com.skjanyou.desktop.window.swt;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class SwtResourcesManager {
	private static Display defaultDisplay = Display.getDefault();
	private static Shell defaultShell = new Shell(defaultDisplay);
	private static Queue<Shell> shellQueue = new LinkedBlockingDeque<>();
	private static boolean isRunning = true;
	
	private static class SubWindowCreateEvent extends Event {
		private Shell shell;
		public void setShell( Shell shell ){
			this.shell = shell;
		}
		public Shell getShell(){
			return this.shell;
		}
	}
	
	static {
		defaultShell.addListener(SWT.None, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if( event instanceof SubWindowCreateEvent ){
					SubWindowCreateEvent e = (SubWindowCreateEvent) event;
					Shell subShell = new Shell(); 
					e.setShell(subShell);
				}
			}
		});
	}
	
	public static Display getDefaultDisplay(){
		return defaultDisplay;
	}
	
	public static Shell getDefaultShell(){
		return defaultShell;
	}
	
	public static Shell createSubWindow(){
		SubWindowCreateEvent event = new SubWindowCreateEvent();
		defaultShell.getListeners(SWT.None)[0].handleEvent(event);
		Shell shell = event.getShell();
		shellQueue.add(shell);
		return shell;
	}
	
	public static void keep(){
		while (!defaultShell.isDisposed() && isRunning ) {
			if (!defaultDisplay.readAndDispatch()) { 
				defaultDisplay.sleep();
			}
		}
	}
	
	public static void exit(){
		Shell shell = null;
		// 将所有创建的shell都关闭
		while( ( shell = shellQueue.poll() ) != null ){
			shell.dispose();
		}
		// 再终止主线程
		isRunning = false;
	}
}
