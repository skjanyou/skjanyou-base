package com.skjanyou.server.api.constant;

public enum StatusCode {
	Ok(200,"OK"),Not_Found(404,"Not_Found"),Error(500,"Server_Error"),Handshake(101,"Web Socket Protocol Handshake");
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
