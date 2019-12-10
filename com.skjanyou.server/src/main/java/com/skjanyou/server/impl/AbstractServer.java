package com.skjanyou.server.impl;

import com.skjanyou.server.bean.ServerConfig;
import com.skjanyou.server.inter.Server;

public abstract class AbstractServer implements Server {
	protected ServerConfig config;
	public AbstractServer( ServerConfig config ){ 
		this.config = config;
		this.init();
	}
	public abstract Server init();
}
