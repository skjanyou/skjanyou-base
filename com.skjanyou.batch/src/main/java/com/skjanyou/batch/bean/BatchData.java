package com.skjanyou.batch.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BatchData<T> {
	private List<T> list = new ArrayList<>();
	private int currentIndex  = -1;
	public BatchData() {}
	public BatchData( List<T> list ) {
		if( list == null ) { throw new IllegalArgumentException(" list is null"); }
		this.list = list;
	}
	
	public int getSize() {
		return this.list.size();
	}
	
	public BatchData<T> add( T item ){
		this.list.add(item);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public BatchData<T> addAll( T... items ){
		this.list.addAll(Arrays.asList(items));
		return this;
	}
	
	public boolean hasNext() {
		return list == null ? false : ++currentIndex >= list.size() ? false : true;
	}
	
	public T next() {
		return list.get(currentIndex);
	}
	
	public int getIndex() {
		return currentIndex;
	}
}
