package com.skjanyou.server.simplehttpserver;

import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.server.api.inter.Server;

public abstract class AbstractServer implements Server {
	protected ServerConfig config;
	public AbstractServer( ServerConfig config ){ 
		this.config = config;
		this.init();
	}
	public abstract Server init();
}
