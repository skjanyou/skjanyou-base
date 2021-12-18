package com.skjanyou.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {
	@SafeVarargs
	public static<T> List<T> of( T... data ) {
		List<T> result = new ArrayList<>();
		if( data == null ) {
			return result;
		}
		for (T item : data) {
			result.add(item);
		}
		
		return result;
	}
}
