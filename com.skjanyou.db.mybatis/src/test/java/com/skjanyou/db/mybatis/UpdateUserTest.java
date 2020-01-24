package com.skjanyou.db.mybatis;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.skjanyou.db.mybatis.bean.User;
import com.skjanyou.db.mybatis.mapper.UserMapper.UpdateUserMapper;
import com.skjanyou.db.mybatis.test.MybatisTest;
import com.skjanyou.start.ioc.BeanContainer;

public class UpdateUserTest extends MybatisTest {
	public static Test suite(){
        return new TestSuite( UpdateUserTest.class );
    }
	
	// 情况1,通过bean更新单条数据  
	public void testupdateUser(  ) {
		UpdateUserMapper userMapper = BeanContainer.getBean(UpdateUserMapper.class);
		
		User user = new User();
		user.setUser_id("skjanyou");
		user.setUser_name("a super man1!");
		user.setUser_pass("hahah1");
		
		int count = userMapper.updateUser(user);
		assertEquals(count, 1);
	}
	// 情况2,通过Map更新单条数据
	public void testupdateUserByMap(  ){
		UpdateUserMapper userMapper = BeanContainer.getBean(UpdateUserMapper.class);
		
		Map<String,Object> map = new HashMap<>();
		map.put("user_id","skjanyou");
		map.put("user_name","a super man2!");
		map.put("user_pass","hahah2");
		
		int count = userMapper.updateUserByMap(map);
		assertEquals(count, 1);		
	}
	// 情况3,通过SqlParameter更新单条数据
	public void testupdateUserBySqlParameter(  ){
		UpdateUserMapper userMapper = BeanContainer.getBean(UpdateUserMapper.class);
		
		String user_id = "skjanyou";
		String user_name = "a super man3!";
		String user_pass = "hahah3";
		
		int count = userMapper.updateUserBySqlParameter(user_id, user_name,user_pass);
		assertEquals(count, 1);		
	}
}
