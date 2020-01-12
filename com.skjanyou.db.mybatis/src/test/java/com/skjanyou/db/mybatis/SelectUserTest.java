package com.skjanyou.db.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.skjanyou.db.bean.DatabaseInfo;
import com.skjanyou.db.mybatis.bean.User;
import com.skjanyou.db.mybatis.core.SqlSession;
import com.skjanyou.db.mybatis.mapper.UserMapper;
import com.skjanyou.db.mybatis.mapper.UserMapper.SelectUserMapper;
import com.skjanyou.db.mybatis.util.DbUtil;

public class SelectUserTest extends TestCase {
    
	public static Test suite(){
        return new TestSuite( SelectUserTest.class );
    }
    
	public SelectUserTest( String testName ){
        super( testName );
    	String className = "com.mysql.cj.jdbc.Driver";
    	String url = "jdbc:mysql://127.0.0.1:3306/blog?zeroDateTimeBehavior=CONVERT_TO_NULL&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
    	String user = "root";
    	String password = "123456";
    	DatabaseInfo info = new DatabaseInfo(className,url,user,password,20,2000);
    	DbUtil.init(info);		
	}


    // 情况1,由一个对象查一条数据
    public void testSelectUser(){
    	SelectUserMapper userMapper = SqlSession.getMapper(UserMapper.SelectUserMapper.class);
    	
    	User userBean = new User();
    	userBean.setUser_id("skjanyou");
    	userBean.setUser_name("skjanyou");
    	userBean.setUser_pass("12345678");
    	
    	User selectUser = userMapper.select(userBean);
        assertNotNull(selectUser);
        System.out.println(selectUser);
    }
    
    // 情况2,不输入条件查所有
    public void testQueryUser(){
    	SelectUserMapper userMapper = SqlSession.getMapper(SelectUserMapper.class);
    	List<User> list = userMapper.query();
    	assertNotNull(list);
    }        
    
    // 情况3,使用Map查询一条数据
    public void testSelectUserByMap(){
    	SelectUserMapper userMapper = SqlSession.getMapper(UserMapper.SelectUserMapper.class);
    	Map<String,Object> map = new HashMap<>();
    	map.put("user_id","skjanyou1");
    	map.put("user_name","skjanyou1");
    	map.put("user_pass","123456789");
    	User user = userMapper.selectByMap(map);
    	assertNotNull(user);
    	System.out.println(user);
    }
    
    // 情况4,使用Map查询所有
    public void testselectListByMap(){
    	SelectUserMapper userMapper = SqlSession.getMapper(UserMapper.SelectUserMapper.class);
    	Map<String,Object> map = new HashMap<>();
    	map.put("user_id","skjanyou%");
    	List<User> userList = userMapper.selectListByMap(map);
    	assertNotNull(userList);
    	System.out.println(userList);
    }


    // 情况5,使用注解SqlParameter查询一条数据
    public void testselectBySqlParameter(){
    	SelectUserMapper userMapper = SqlSession.getMapper(UserMapper.SelectUserMapper.class);
    	
    	String user_id = "skjanyou";
    	String user_name = "skjanyou";
    	String user_pass = "12345678";
    	
    	User selectUser = userMapper.selectBySqlParameter(user_id,user_name,user_pass);
        assertNotNull(selectUser);
        System.out.println(selectUser);
    }   

    // 情况6,使用注解SqlParameter查询全部数据
    public void testselectListBySqlParameter(){
    	SelectUserMapper userMapper = SqlSession.getMapper(UserMapper.SelectUserMapper.class);
    	String user_id = "skjanyou%";
    	List<User> selectUsers = userMapper.selectListBySqlParameter(user_id);
        assertNotNull(selectUsers);
        System.out.println(selectUsers);
    }       
}
