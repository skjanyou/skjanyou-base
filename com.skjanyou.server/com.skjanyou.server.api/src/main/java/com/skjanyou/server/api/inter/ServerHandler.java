package com.skjanyou.server.api.inter;

import com.skjanyou.server.api.exception.ServerException;

public interface ServerHandler {
	public ServerHandler init() throws ServerException;
	public boolean handler( Request request,Response response ) throws ServerException;
}
