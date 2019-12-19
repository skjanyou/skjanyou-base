package com.skjanyou.db.mybatis.inter;

import com.skjanyou.db.mybatis.bean.ProcessItem;

public interface SqlProcess {
	public Object process( ProcessItem pi );
}
