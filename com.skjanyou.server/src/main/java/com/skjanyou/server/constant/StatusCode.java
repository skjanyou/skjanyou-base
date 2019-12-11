package com.skjanyou.server.constant;

public enum StatusCode {
	Ok(200,"OK"),Not_Found(404,"Not_Found");
	int code; 
	String name;
	StatusCode( int code,String name ){ this.code = code;this.name = name; }
	public int getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
}
