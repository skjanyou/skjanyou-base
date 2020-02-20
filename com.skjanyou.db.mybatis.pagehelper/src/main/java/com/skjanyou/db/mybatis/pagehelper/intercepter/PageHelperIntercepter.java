package com.skjanyou.db.mybatis.pagehelper.intercepter;

import com.skjanyou.db.mybatis.anno.DDL.Select;
import com.skjanyou.db.mybatis.bean.IntercepterSqlInfo;
import com.skjanyou.db.mybatis.inter.SqlProcessIntercepter;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;

public class PageHelperIntercepter implements SqlProcessIntercepter<Select>{
	private Logger logger = LogUtil.getLogger(PageHelperIntercepter.class);
	@Override
	public void beforeIntercepter(IntercepterSqlInfo intercepterSqlInfo) {
		logger.debug("原始SQL:" + intercepterSqlInfo.getSql());
	}

	@Override
	public void afterIntercepter(Object result) {
		
	}

}
