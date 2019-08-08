package com.skjanyou.util;

import com.skjanyou.util.proxy.fun.ProxyHandle;

public class TestProxy implements ProxyHandle {

	@Override
	public Object before(Object... objs) {
		System.out.println("before");
		return null;
	}

	@Override
	public Object after(Object... objs) {
		System.out.println("after");
		return null;
	}

}
