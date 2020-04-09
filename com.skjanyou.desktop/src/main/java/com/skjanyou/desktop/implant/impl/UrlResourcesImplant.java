package com.skjanyou.desktop.implant.impl;

import java.io.IOException;
import java.net.URL;

import com.skjanyou.desktop.implant.Implant;
import com.skjanyou.util.StreamUtil;

public class UrlResourcesImplant implements Implant {
	private String script;
	public UrlResourcesImplant( URL url ){
		try {
			this.script = StreamUtil.convertToString(url.openStream(), "utf-8", true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}
	
	@Override
	public String getImplantScript() {
		return this.script;
	}

	@Override
	public String getRunnableScript() {
		return this.script;
	}

}
