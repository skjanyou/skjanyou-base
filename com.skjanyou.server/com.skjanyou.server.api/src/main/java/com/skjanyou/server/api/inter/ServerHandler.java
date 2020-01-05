package com.skjanyou.server.api.inter;

public interface ServerHandler {
	public ServerHandler init() throws Exception;
	public boolean handler( Request request,Response response );
}
