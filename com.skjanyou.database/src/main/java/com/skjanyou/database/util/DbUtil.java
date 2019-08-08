package com.skjanyou.database.util;

import java.io.Reader;
import java.util.Collection;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.skjanyou.database.bean.DBConfig;

public class DbUtil {
	private static SqlSession session;
	private static SqlSessionFactory sessionFactory;
	private static DBConfig dbConfig = new DBConfig();
	
	public static DBConfig getConfig(){
		return dbConfig;
	}
	public static void init(){
		if(session == null){
			try {
				// 使用MyBatis提供的Resources类加载mybatis的配置文件
				Reader reader = Resources.getResourceAsReader("mybatis.cfg.xml");
				// 构建sqlSession的工厂
				sessionFactory = new SqlSessionFactoryBuilder().build(reader);
				
				Configuration config = sessionFactory.getConfiguration();
				//注册单个别名
				//config.getTypeAliasRegistry().registerAlias("Goods",Goods.class);
				//将这个目录下面的接口都注册到别名列表中
				dbConfig.getTypeAliasRegistryList().forEach( aliasRegistry -> {
					//config.getTypeAliasRegistry().registerAliases("com.skjanyou.database");
					config.getTypeAliasRegistry().registerAliases(aliasRegistry);
				});
				//sql mapper
				MapperRegistry mr = config.getMapperRegistry();
				//mr.addMappers("com.skjanyou.database", GoodsMapper.class);
				//扫描指定包下面的mapper
				dbConfig.getMapperRegistry().forEach( mapperRegistry -> {
					//mr.addMappers("com.skjanyou.database");
					mr.addMappers(mapperRegistry);
				});;
				
				
				
				//sessionFactory.getConfiguration().addMappers("com.skjanyou.database.GoodsMapper", GoodsMapper.class);
				session = sessionFactory.openSession(dbConfig.isAutoCommit());
			} catch (Exception e) {
				throw new RuntimeException("获取数据库配置失败 !",e);
			}
		}		
	}
	public static SqlSession getSession(){
		if(session == null){
			init();
		}
		return session;
	}
	
	public static<T> T getMapper(Class<T> clazz){
		return session.getMapper(clazz);
	}
	
	public static void commit(){
		if(session != null ){
			session.commit();
		}
	}
	
	public static void rollbakc(){
		if(session != null){
			session.rollback();
		}
	}
	
	public static<T> String getSql(String mapperId,T parameterObject){
		Configuration config = sessionFactory.getConfiguration();
		return config.getMappedStatement(mapperId).getBoundSql(parameterObject).getSql();
	}
	
	public static Collection<String> getMappedStatementNames(){
		Collection<String> c = sessionFactory.getConfiguration().getMappedStatementNames();
		return c;
	}
}
