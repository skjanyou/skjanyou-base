package com.skjanyou.desktop.implant.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.skjanyou.desktop.implant.DefaultImplant;
import com.skjanyou.util.CommUtil;

public class ScriptFileImplant extends DefaultImplant {
	private File scriptFile;
	public ScriptFileImplant( File file ){
		this.scriptFile = file;
	}
	

	@Override
	public String getImplantScript() {
		StringBuilder sb = new StringBuilder();
		FileReader reader = null;
		BufferedReader br = null;
		String line = null;
		try {
			reader = new FileReader(scriptFile);
			br = new BufferedReader(reader);
			while( ( line = br.readLine() ) != null ){
				sb.append(line).append("\r\n");
			}
			
		} catch ( IOException e) {
			e.printStackTrace();
		} finally {
			CommUtil.close(br);
			CommUtil.close(reader);
		}
		
		return sb.toString();
	}

}
