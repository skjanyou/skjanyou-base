package com.skjanyou.db.mybatis.inter.impl;

import com.skjanyou.db.mybatis.anno.DDL.BatchInsert;
import com.skjanyou.db.mybatis.anno.DDL.Delete;
import com.skjanyou.db.mybatis.anno.DDL.Insert;
import com.skjanyou.db.mybatis.anno.DDL.Select;
import com.skjanyou.db.mybatis.anno.DDL.Update;
import com.skjanyou.db.mybatis.bean.ProcessItem;
import com.skjanyou.db.mybatis.inter.SqlExceptionProcess;
import com.skjanyou.db.mybatis.inter.SqlProcess;

public final class DefaultSqlProcess {
	public static class SelectSqlProcess implements SqlProcess<Select>,SqlExceptionProcess {
		@Override
		public Object process(ProcessItem<Select> item) {
			return null;
		}

		@Override
		public void handlerException(Throwable throwable) {
			
		}
	}

	public static class InsertSqlProcess implements SqlProcess<Insert>,SqlExceptionProcess {
		@Override
		public Object process(ProcessItem<Insert> item) {
			return null;
		}

		@Override
		public void handlerException(Throwable throwable) {
			
		}
	}
	
	public static class BatchInsertSqlProcess implements SqlProcess<BatchInsert>,SqlExceptionProcess {
		@Override
		public Object process(ProcessItem<BatchInsert> item) {
					
			return null;
		}

		@Override
		public void handlerException(Throwable throwable) {
			
		}
	}	
	
	public static class UpdateSqlProcess implements SqlProcess<Update>,SqlExceptionProcess {
		@Override
		public Object process(ProcessItem<Update> item) {
			return null;
		}

		@Override
		public void handlerException(Throwable throwable) {
			
		}
	}
	
	public static class DeleteSqlProcess implements SqlProcess<Delete>,SqlExceptionProcess {
		@Override
		public Object process(ProcessItem<Delete> pi) {
			return null;
		}

		@Override
		public void handlerException(Throwable throwable) {
			
		}
	}
}
