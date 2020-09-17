package com.skjanyou.db.mybatis;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.skjanyou.db.mybatis.bean.User;
import com.skjanyou.db.mybatis.core.SqlSession;
import com.skjanyou.db.mybatis.mapper.UserMapper.DeleteUserMapper;
import com.skjanyou.db.mybatis.test.MybatisTest;

public class DeleteUserTest extends MybatisTest {
	public static Test suite(){
        return new TestSuite( DeleteUserTest.class );
    }
	
	
	// 情况1,通过bean删除单条数据  
	public void testdeleteUser( ){

		DeleteUserMapper userMapper = SqlSession.getMapper(DeleteUserMapper.class,defaultDataSourceManager);
		
		User user = new User();
		user.setUser_id("test_1");
		
		int count = userMapper.deleteUser(user);
		assertNotSame(count, 0);
	};
	// 情况2,通过Map删除单条数据
	public void testDeleteUserByMap( ){
		DeleteUserMapper userMapper = SqlSession.getMapper(DeleteUserMapper.class,defaultDataSourceManager);
		
		Map<String,Object> map = new HashMap<>();
		map.put("user_id", "test_2");
		
		int count = userMapper.deleteUserByMap(map);
		assertNotSame(count, 0);
	};
	// 情况3,通过SqlParameter更新单条数据
	public void testDeleteUserBySqlParameter(  ){
		DeleteUserMapper userMapper = SqlSession.getMapper(DeleteUserMapper.class,defaultDataSourceManager);
		
		String user_id = "test_3";
		
		int count = userMapper.deleteUserBySqlParameter(user_id);
		assertNotSame(count, 0);
	};		
	
	
}
