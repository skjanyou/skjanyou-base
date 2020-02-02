package com.skjanyou.util.convert;

public interface ConvertProvider< D,T > {
	public boolean isMatch( Class<?> distClass,Class<?> targetClass );
	public T converTo( D dist );
}
