package com.skjanyou.mvc.core.convert;

public interface ConvertProvider< D,T > {
	public boolean isMatch( Class<?> distClass,Class<?> targetClass );
	public T converTo( D dist );
}
