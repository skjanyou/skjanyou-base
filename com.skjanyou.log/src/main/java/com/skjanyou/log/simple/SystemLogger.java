package com.skjanyou.log.simple;

import java.io.PrintStream;

import com.skjanyou.log.core.Logger;

public class SystemLogger implements Logger {
	private String className ;
	SystemLogger( String className ){
		this.className = className;
	}
	@Override
	public Logger trace(Object... msg) {
		return printLine(System.out,msg);
	}

	@Override
	public Logger debug(Object... msg) {
		return printLine(System.out,msg);
	}

	@Override
	public Logger info(Object... msg) {
		return printLine(System.out,msg);
	}

	@Override
	public Logger warn(Object... msg) {
		return printLine(System.out,msg);
	}

	@Override
	public Logger error(Object... msg) {
		return printLine(System.err,msg);
	}

	@Override
	public Logger fatal(Object... msg) {
		return printLine(System.out,msg);
	}
	
	public Logger printLine( PrintStream out,Object... msg ){
		StringBuilder sb = new StringBuilder();
		sb.append(this.className).append(":[");
		for (Object object : msg) {
			if( object instanceof Throwable){
				sb.append("\r\n");
				Throwable t = (Throwable) object;
				sb.append("    ").append(t).append("\r\n");
				StackTraceElement[] ste = t.getStackTrace();
				for (StackTraceElement stackTraceElement : ste) {
					sb.append("    ").append(stackTraceElement.toString()).append("\r\n");
				}
			}else{
				sb.append(object);
			}
		}
		sb.append("]");
		out.println(sb.toString());
		return this;
	}

}
