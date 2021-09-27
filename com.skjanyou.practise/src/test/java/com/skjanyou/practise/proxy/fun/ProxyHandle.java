package com.skjanyou.practise.proxy.fun;

public interface ProxyHandle {
	public Object before(Object... objs);
	public Object after(Object... objs);
}
