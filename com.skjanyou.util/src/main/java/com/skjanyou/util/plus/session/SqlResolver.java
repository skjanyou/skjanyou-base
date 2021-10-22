package com.skjanyou.util.plus.session;

public interface SqlResolver {
	public<T> String doResolver( String sql , T input );
}
