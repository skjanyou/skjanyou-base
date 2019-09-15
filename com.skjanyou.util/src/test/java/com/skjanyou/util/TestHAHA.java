package com.skjanyou.util;

import com.skjanyou.util.proxy.fun.ProxyHandle;

public @interface TestHAHA {
	Class<? extends ProxyHandle> value() default ProxyHandle.class;
}
