package com.skjanyou.desktop.implant;

import java.text.MessageFormat;

public abstract class DefaultImplant implements Implant{
	protected String resultScript = "(function( {0} )()";
	
	@Override
	public String getRunnableScript(){
		return MessageFormat.format( resultScript, getImplantScript());
	}
	
}
