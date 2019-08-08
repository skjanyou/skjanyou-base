package com.skjanyou.util.proxy.fun;

public interface ProxyHandle {
	public Object before(Object... objs);
	public Object after(Object... objs);
}
