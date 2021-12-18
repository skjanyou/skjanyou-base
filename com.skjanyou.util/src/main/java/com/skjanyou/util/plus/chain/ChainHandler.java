package com.skjanyou.util.plus.chain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.skjanyou.util.plus.chain.impl.DefaultChainProcess;

public class ChainHandler<T> {
	private List<Chain<T>> chains;
	
	public ChainHandler() {
		this.chains = new ArrayList<>();
	}
	public ChainHandler( List<Chain<T>> chains ) {
		this.chains = chains;
		this.chains.sort(Comparator.comparingInt(Chain::getOrder));
	}

	public void addChain(Chain<T> chain) {
		this.chains.add(chain);
		this.chains.sort(Comparator.comparingInt(Chain::getOrder));
	}

	public void doChainProcess( T data ) {
		// 在这里发起链式调用,这个chainProcess要自己创建
		ChainProcess<T> cp = new DefaultChainProcess<>( this.chains );
		cp.doChain(data);
	}
}
