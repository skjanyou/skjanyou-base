package com.skjanyou.db.mybatis.bean;

import java.io.Serializable;

public class Page<T> implements Serializable{
	private static final long serialVersionUID = -6622562309885183443L;
	private int start;
	private int size;
	private T condition;
	
	public Page(int start, int size, T condition) {
		this.start = start;
		this.size = size;
		this.condition = condition;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public T getCondition() {
		return condition;
	}
	public void setCondition(T condition) {
		this.condition = condition;
	}
	
}
