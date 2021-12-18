package com.skjanyou.server.api.inter;

import com.skjanyou.server.api.bean.HttpEntity;
import com.skjanyou.util.plus.chain.ChainProcess;

public abstract class AbstractHttpFilter implements Filter {

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void doChain(ChainProcess<HttpEntity> chainProcess, HttpEntity data) {
		try {
			boolean next = this.doFilter(data.getRequest(), data.getResponse());
			if(next) {
				chainProcess.doChain(data);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Filter init() {
		return this;
	}

	@Override
	public Filter destroy() {
		return this;
	}

}
