package com.skjanyou.db.mybatis.inter.impl;

import com.skjanyou.db.mybatis.anno.DDL.Delete;
import com.skjanyou.db.mybatis.anno.DDL.Insert;
import com.skjanyou.db.mybatis.anno.DDL.Select;
import com.skjanyou.db.mybatis.anno.DDL.Update;
import com.skjanyou.db.mybatis.bean.ProcessItem;
import com.skjanyou.db.mybatis.inter.AnnotationHandler;
import com.skjanyou.db.mybatis.inter.SqlExceptionProcess;
import com.skjanyou.db.mybatis.inter.SqlProcess;
import com.skjanyou.db.mybatis.util.BeanUtil;

public class DefaultAnnotationDispatchHandler  {

	public static class SelectAnnotationHandler implements AnnotationHandler<Select>{
		@Override
		public Object handler(ProcessItem<Select> item) {
			Object result = null;
			Select select = item.getAnno();
			Class<? extends SqlProcess<Select>> sqlProcessClass = select.handler();
			Class<? extends SqlExceptionProcess> sqlExceptionProcessClass = select.exception();
			
			SqlProcess<Select> selectProcess = BeanUtil.getBean(sqlProcessClass);
			SqlExceptionProcess exceptionProcess = BeanUtil.getBean(sqlExceptionProcessClass);
			
			
			try{
				result = selectProcess.process(item);
			}catch(Exception e){
				exceptionProcess.handlerException(e);
			}
			return result;
		}
	}
	
	public static class InsertAnnotationHander implements AnnotationHandler<Insert>{
		@Override
		public Object handler(ProcessItem<Insert> item) {
			Object result = null;
			Insert insert = item.getAnno();
			Class<? extends SqlProcess<Insert>> sqlProcessClass = insert.handler();
			Class<? extends SqlExceptionProcess> sqlExceptionProcessClass = insert.exception();
			
			SqlProcess<Insert> insertProcess = BeanUtil.getBean(sqlProcessClass);
			SqlExceptionProcess exceptionProcess = BeanUtil.getBean(sqlExceptionProcessClass);
			
			
			try{
				result = insertProcess.process(item);
			}catch(Exception e){
				exceptionProcess.handlerException(e);
			}
			return result;
		}
	}
	
	public static class UpdateAnnotationHander implements AnnotationHandler<Update>{
		@Override
		public Object handler(ProcessItem<Update> item) {
			Object result = null;
			Update update = item.getAnno();
			Class<? extends SqlProcess<Update>> sqlProcessClass = update.handler();
			Class<? extends SqlExceptionProcess> sqlExceptionProcessClass = update.exception();
			
			SqlProcess<Update> updateProcess = BeanUtil.getBean(sqlProcessClass);
			SqlExceptionProcess exceptionProcess = BeanUtil.getBean(sqlExceptionProcessClass);
			
			
			try{
				result = updateProcess.process(item);
			}catch(Exception e){
				exceptionProcess.handlerException(e);
			}
			return result;
		}
	}
	
	public static class DeleteAnnotationHandler implements AnnotationHandler<Delete>{
		@Override
		public Object handler(ProcessItem<Delete> item) {
			Object result = null;
			Delete delete = item.getAnno();
			Class<? extends SqlProcess<Delete>> sqlProcessClass = delete.handler();
			Class<? extends SqlExceptionProcess> sqlExceptionProcessClass = delete.exception();
			
			SqlProcess<Delete> deleteProcess = BeanUtil.getBean(sqlProcessClass);
			SqlExceptionProcess exceptionProcess = BeanUtil.getBean(sqlExceptionProcessClass);
			
			
			try{
				result = deleteProcess.process(item);
			}catch(Exception e){
				exceptionProcess.handlerException(e);
			}
			return result;
		}
	}

}
