package com.skjanyou.db.mybatis.pagehelper.page;

import com.skjanyou.util.StringUtil;

public class OraclePageHelper implements PageHelper {
	private static final String WRAPPER_WITHOUTBEGIN = 
			"select * from ( " + 
					"select a.*,ROWNUM rownum_ from ( _sql_ ) a" + 
			 ") where rownum_ < _page_end_";
	private static final String WRAPPER_WITHOUTEND = 
			"select * from ( " + 
					"select a.*,ROWNUM rownum_ from ( _sql_ ) a" + 
			") where rownum_ >= _page_begin_";	
	private static final String WRAPPER_ALL = 
			"select * from ( " + 
					"select a.*,ROWNUM rownum_ from ( _sql_ ) a" + 
			") where rownum_ >= _page_begin_ and rownum_ < _page_end_";	
	
	@Override
	public String doWrapper(String sql) {
		String begin = context.get().get(PageHelper.KEY_PAGE_BEGIN);
		String end = context.get().get(PageHelper.KEY_PAGE_END);
		
		if( !StringUtil.isBlank(begin) && !StringUtil.isBlank(end) ) {
			return WRAPPER_ALL.replace(PageHelper.KEY_SQL, sql)
					.replace(PageHelper.KEY_PAGE_BEGIN,begin)
					.replace(PageHelper.KEY_PAGE_END, end);
		}else if( !StringUtil.isBlank(begin) && StringUtil.isBlank(end) ) {
			return WRAPPER_WITHOUTEND.replace(PageHelper.KEY_SQL, sql)
					.replace(PageHelper.KEY_PAGE_BEGIN,begin);
		}else if( StringUtil.isBlank(begin) && !StringUtil.isBlank(end) ) {
			return WRAPPER_WITHOUTBEGIN.replace(PageHelper.KEY_SQL, sql)
					.replace(PageHelper.KEY_PAGE_END,end);
		}else{
			return sql;
		}
	}

}
