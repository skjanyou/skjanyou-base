package com.skjanyou.server.api.inter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.skjanyou.server.api.bean.ServerConfig;

public abstract class AbstractServer implements Server{
	protected ServerConfig config;
	protected List<Filter> filters;	
	protected ServerHandler handler;
	protected boolean isAlive;
	public AbstractServer(){
		filters = new ArrayList<>();
	}
	
	@Override
	public Server addFilter(Filter filter) {
		this.filters.add(filter);
		Collections.sort(this.filters,new Comparator<Filter>() {
			@Override
			public int compare(Filter filter1, Filter filter2) {
				return filter1.priority() - filter2.priority();
			}
		});		
		return this;
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
