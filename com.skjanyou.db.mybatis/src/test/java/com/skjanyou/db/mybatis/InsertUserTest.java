package com.skjanyou.db.mybatis;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.skjanyou.db.mybatis.bean.User;
import com.skjanyou.db.mybatis.core.SqlSession;
import com.skjanyou.db.mybatis.mapper.UserMapper.InsertUserMapper;
import com.skjanyou.db.mybatis.test.MybatisTest;

public class InsertUserTest extends MybatisTest  {
	public static Test suite(){
        return new TestSuite( InsertUserTest.class );
    }
	
	// 情况1,通过bean插入单条记录
	public void testinsertUser(){
		InsertUserMapper userMapper = SqlSession.getMapper(InsertUserMapper.class,defaultDataSourceManager);
		
		User user = new User();
		user.setUser_id("test_1");
		user.setUser_name("测试1");
		user.setUser_pass("123456");
		int count = userMapper.insertUser(user);
		assertEquals(count, 1);
	}
	
	// 情况2,通过Map插入数据
	public void testinsertUserByMap(){
		InsertUserMapper userMapper = SqlSession.getMapper(InsertUserMapper.class,defaultDataSourceManager);
		
		Map<String,Object> map = new HashMap<>();
		map.put("user_id", "test_2");
		map.put("user_name", "测试2");
		map.put("user_pass", "1234567");
		int count = userMapper.insertUserByMap(map);
		assertEquals(count, 1);
	}
	
	// 情况3,通过SqlParameter插入数据		
	public void testinsertUserBySqlParameter(){
		InsertUserMapper userMapper = SqlSession.getMapper(InsertUserMapper.class,defaultDataSourceManager);
		
		String user_id = "test_3";
		String user_name = "测试3";
		String user_pass = "12345678";
		int count = userMapper.insertUserBySqlParameter(user_id,user_name,user_pass);
		assertEquals(count, 1);
	}
}
