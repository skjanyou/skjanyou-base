package com.skjanyou.db.mybatis.inter.impl;

import com.skjanyou.db.mybatis.bean.ProcessItem;
import com.skjanyou.db.mybatis.inter.SqlExceptionProcess;
import com.skjanyou.db.mybatis.inter.SqlProcess;

public final class DefaultSqlProcess {
	public static class SelectSqlProcess implements SqlProcess,SqlExceptionProcess {
		@Override
		public Object process(ProcessItem pi) {
			return null;
		}
	}

	public static class InsertSqlProcess implements SqlProcess,SqlExceptionProcess {
		@Override
		public Object process(ProcessItem pi) {
			return null;
		}
	}
	
	public static class UpdateSqlProcess implements SqlProcess,SqlExceptionProcess {
		@Override
		public Object process(ProcessItem pi) {
			return null;
		}
	}
	
	public static class DeleteSqlProcess implements SqlProcess,SqlExceptionProcess {
		@Override
		public Object process(ProcessItem pi) {
			return null;
		}
	}
}
