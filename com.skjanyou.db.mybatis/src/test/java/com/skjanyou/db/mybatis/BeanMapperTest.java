package com.skjanyou.db.mybatis;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.skjanyou.db.mybatis.bean.User;
import com.skjanyou.db.mybatis.core.SqlSession;
import com.skjanyou.db.mybatis.mapper.TestUserMapper;
import com.skjanyou.db.mybatis.test.MybatisTest;
import com.skjanyou.db.mybatis.util.SqlUtil;

public class BeanMapperTest extends MybatisTest {
	public static Test suite(){
        return new TestSuite( BeanMapperTest.class );
    }
	
	public void testGenSql() {
		String sql = SqlUtil.generateInsertSql(User.class);
		System.out.println(sql);
	}
	
	// 情况1,通过bean删除单条数据  
	public void testBeanMapper( ){

		TestUserMapper userMapper = SqlSession.getMapper(TestUserMapper.class);
		
		User user = new User();
		user.setUser_id("skjanyou");
		
		User newUser = userMapper.selectOne(user);
		assertNotNull(newUser);
	};
	
	
}
