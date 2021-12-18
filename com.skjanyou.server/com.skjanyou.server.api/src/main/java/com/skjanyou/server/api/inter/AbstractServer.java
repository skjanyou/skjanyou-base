package com.skjanyou.server.api.inter;

import java.util.ArrayList;
import java.util.List;

import com.skjanyou.server.api.bean.ServerConfig;

public abstract class AbstractServer implements Server {
	protected ServerConfig config;
	protected List<Filter> filters;	
	protected FilterHandler filterHandler;
	protected ServerHandler handler;
	protected boolean isAlive;
	public AbstractServer(){
		this.filters = new ArrayList<>();
		this.filterHandler = new FilterHandler();
	}
	
	@Override
	public Server addFilter(Filter filter) {
		this.filters.add(filter);
		this.filterHandler.addChain(filter);
		return this;
	}

	@Override
	public FilterHandler getFilterHandler() {
		return this.filterHandler;
	}

	@Override
	public Server handler(ServerHandler handler) {
		this.handler = handler;
		return this;
	}

	@Override
	public Server setConfig(ServerConfig config) {
		this.config = config;
		return this;
	}
	
	@Override
	public boolean isAlive() {
		return this.isAlive;
	}

}
