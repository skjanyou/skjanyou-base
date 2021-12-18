package com.skjanyou.util.plus.chain;

public interface Chain<T> {
	/** **/
	public int getOrder();
	/** **/
	public void doChain( ChainProcess<T> chainProcess, T data );
}
