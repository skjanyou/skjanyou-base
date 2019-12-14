package com.skjanyou.server.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.skjanyou.server.inter.Filter;
import com.skjanyou.server.inter.ServerHandler;

public class ApplicateContext {
	private static ApplicateContext $this = new ApplicateContext();
	private static List<Filter> filterList = Collections.synchronizedList(new ArrayList<>());
	private static ServerHandler serverHandler = null;
	
	private ApplicateContext(){}
	
	/** 注册过滤器 **/
	public static ApplicateContext registFilter( Filter filter ){
		filterList.add(filter);
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
