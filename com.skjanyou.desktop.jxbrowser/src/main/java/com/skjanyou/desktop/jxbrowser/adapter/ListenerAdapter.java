package com.skjanyou.desktop.jxbrowser.adapter;

import com.teamdev.jxbrowser.chromium.UploadProgressListener;
import com.teamdev.jxbrowser.chromium.events.ConsoleEvent;
import com.teamdev.jxbrowser.chromium.events.ConsoleListener;
import com.teamdev.jxbrowser.chromium.events.DisposeEvent;
import com.teamdev.jxbrowser.chromium.events.DisposeListener;
import com.teamdev.jxbrowser.chromium.events.FailLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FrameLoadEvent;
import com.teamdev.jxbrowser.chromium.events.LoadEvent;
import com.teamdev.jxbrowser.chromium.events.LoadListener;
import com.teamdev.jxbrowser.chromium.events.ProvisionalLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.RenderEvent;
import com.teamdev.jxbrowser.chromium.events.RenderListener;
import com.teamdev.jxbrowser.chromium.events.ScriptContextEvent;
import com.teamdev.jxbrowser.chromium.events.ScriptContextListener;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.StatusEvent;
import com.teamdev.jxbrowser.chromium.events.StatusListener;
import com.teamdev.jxbrowser.chromium.events.TitleEvent;
import com.teamdev.jxbrowser.chromium.events.TitleListener;

public abstract class ListenerAdapter implements LoadListener,ConsoleListener,DisposeListener,RenderListener,ScriptContextListener,StatusListener,TitleListener,UploadProgressListener{

	@Override
	public void onDocumentLoadedInFrame(FrameLoadEvent arg0) {
		
	}

	@Override
	public void onDocumentLoadedInMainFrame(LoadEvent arg0) {
		
	}

	@Override
	public void onFailLoadingFrame(FailLoadingEvent arg0) {
		
	}

	@Override
	public void onFinishLoadingFrame(FinishLoadingEvent arg0) {
		
	}

	@Override
	public void onProvisionalLoadingFrame(ProvisionalLoadingEvent arg0) {
		
	}

	@Override
	public void onStartLoadingFrame(StartLoadingEvent arg0) {
		
	}

	@Override
	public void onProgressChanged(int arg0, int arg1) {
		
	}

	@Override
	public void onTitleChange(TitleEvent arg0) {
		
	}

	@Override
	public void onStatusChange(StatusEvent arg0) {
		
	}

	@Override
	public void onScriptContextCreated(ScriptContextEvent arg0) {
		
	}

	@Override
	public void onScriptContextDestroyed(ScriptContextEvent arg0) {
		
	}

	@Override
	public void onRenderCreated(RenderEvent arg0) {
		
	}

	@Override
	public void onRenderGone(RenderEvent arg0) {
		
	}

	@Override
	public void onRenderResponsive(RenderEvent arg0) {
		
	}

	@Override
	public void onRenderUnresponsive(RenderEvent arg0) {
		
	}

	@Override
	public void onDisposed(DisposeEvent arg0) {
		
	}

	@Override
	public void onMessage(ConsoleEvent arg0) {
		
	}

}
