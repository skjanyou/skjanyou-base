package com.skjanyou.util.plus.chain.impl;

import java.util.List;

import com.skjanyou.util.plus.chain.Chain;
import com.skjanyou.util.plus.chain.ChainProcess;

public class DefaultChainProcess<T> implements ChainProcess<T> {
	private List<Chain<T>> chains;
	private int index ;
	public DefaultChainProcess( List<Chain<T>> chains ) {
		this.chains = chains;
		this.index = 0;
	}
	
	protected Chain<T> getNext(){
		return this.chains == null ? null : this.chains.size() > index ? this.chains.get(index++) : null;
	}
	
	@Override
	public void doChain( T data ) {
		Chain<T> next = getNext();
		if( next != null ) {
			next.doChain(this, data);
		}
	}

}
