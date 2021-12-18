package com.skjanyou.server.api.bean;

import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.util.plus.chain.ChainProcess;

public class FilterAdapter implements Filter {

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void doChain(ChainProcess<HttpEntity> chainProcess, HttpEntity data) {
	}

	@Override
	public Filter init() {
		return this;
	}

	@Override
	public boolean doFilter(Request request, Response response) throws Exception {
		return false;
	}

	@Override
	public Filter destroy() {
		return this;
	}

}
