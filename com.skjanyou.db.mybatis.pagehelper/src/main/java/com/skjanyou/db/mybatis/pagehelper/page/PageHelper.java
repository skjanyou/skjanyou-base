package com.skjanyou.db.mybatis.pagehelper.page;

import java.util.Map;

public interface PageHelper {
	ThreadLocal<Map<String,String>> context = new ThreadLocal<>();
	public static int page_begin = 0;
	public static int page_end = 20;
	public static final String KEY_SQL = "_sql_";
	public static final String KEY_PAGE_BEGIN = "_page_begin_";
	public static final String KEY_PAGE_END = "_page_end_";
	public String doWrapper( String sql );
}
