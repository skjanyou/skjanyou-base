package com.skjanyou.server.api.inter;

import java.util.List;

import com.skjanyou.server.api.bean.HttpEntity;
import com.skjanyou.util.plus.chain.ChainHandler;

public class FilterHandler extends ChainHandler<HttpEntity> {

	public FilterHandler() {
		super();
	}
	
	public FilterHandler(List<Filter> filters ) {
		super();
		for (Filter filter : filters) {
			addChain(filter);
		}
	}
	
}
