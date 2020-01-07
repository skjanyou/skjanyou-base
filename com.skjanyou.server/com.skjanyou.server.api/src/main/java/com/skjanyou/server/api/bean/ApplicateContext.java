package com.skjanyou.server.api.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.ServerHandler;

public class ApplicateContext {
	private static ApplicateContext $this = new ApplicateContext();
	private static List<Filter> filterList = Collections.synchronizedList(new ArrayList<>());
	private static ServerHandler serverHandler = null;
	
	private ApplicateContext(){}
	
	/** 注册过滤器 **/
	public static ApplicateContext registFilter( Filter filter ){
		filterList.add(filter);
		Collections.sort(filterList,new Comparator<Filter>() {
			@Override
			public int compare(Filter f1, Filter f2) {
				return f1.priority() - f2.priority();
			}
		});
		return $this;
	}
	
	/** 获取过滤器 **/
	public static List<Filter> getRegistedFilter(){
		return filterList;
	}
	
	public static ApplicateContext setServerHandler( ServerHandler sh ){
		serverHandler = sh;
		return $this;
	}
	
	public static ServerHandler getServerHandler(){
		return serverHandler;
	}
}
