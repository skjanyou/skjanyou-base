package com.skjanyou.db.mybatis.inter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.skjanyou.db.mybatis.anno.DDL.Delete;
import com.skjanyou.db.mybatis.anno.DDL.Insert;
import com.skjanyou.db.mybatis.anno.DDL.Select;
import com.skjanyou.db.mybatis.anno.DDL.Update;
import com.skjanyou.db.mybatis.bean.ProcessItem;
import com.skjanyou.db.mybatis.inter.impl.DefaultSqlProcess;

public class SqlProcessHandler {
	private static SqlProcessHandler $this = new SqlProcessHandler();
	private static Map<Class<? extends Annotation>,SqlProcess> map = new HashMap<>();
	
	static {
		// TODO 这里手动绑定貌似有点毛病,注解上面的配置该怎么办？ 
		$this.on(Select.class, new DefaultSqlProcess.SelectSqlProcess());
		$this.on(Insert.class, new DefaultSqlProcess.InsertSqlProcess());
		$this.on(Update.class, new DefaultSqlProcess.UpdateSqlProcess());
		$this.on(Delete.class, new DefaultSqlProcess.DeleteSqlProcess());
	}
	
	private SqlProcessHandler(){}
	
	public static SqlProcessHandler get(){ return $this; }

	public SqlProcessHandler on ( Class<? extends Annotation> clazz,SqlProcess process ) {
		if( map.containsKey(clazz) ){
			throw new RuntimeException("注解" + clazz.getName() + "已存在");
		}
		map.put(clazz, process);
		return this;
	}
	
	public Object process( Class<?> mapperClass, Object proxy, Method method,Object[] args ) throws Throwable{
		Annotation[] annos = method.getAnnotations();
		for (Annotation annotation : annos) {
			Class<?> cls = annotation.annotationType();
			SqlProcess process = map.get(cls);
			if( process != null ){
				ProcessItem pi = new ProcessItem();
				return process.process(pi);
			}
		}
		
		return method.invoke(proxy, args);
	}
}
