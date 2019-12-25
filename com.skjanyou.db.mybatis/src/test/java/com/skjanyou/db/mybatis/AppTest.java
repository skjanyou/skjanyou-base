package com.skjanyou.db.mybatis;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.skjanyou.db.mybatis.core.SqlSession;
import com.skjanyou.db.mybatis.util.DBUtil;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    public AppTest( String testName ){
        super( testName );
    	String className = "com.mysql.cj.jdbc.Driver";
    	String url = "jdbc:mysql://127.0.0.1:3306/blog?zeroDateTimeBehavior=CONVERT_TO_NULL&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
    	String user = "root";
    	String password = "123456";
    	DBUtil.init(className, url, user, password);
    }

    public static Test suite(){
        return new TestSuite( AppTest.class );
    }
    
    // 查询用户信息
    public void testSelectUser(){
    	
    	String sql = "select * from user where user_id = #user_id# and user_name = #user_name# and user_pass = #user_pass#";
    	User userBean = new User();
    	userBean.setUser_id("skjanyou");
    	userBean.setUser_name("skjanyou");
    	userBean.setUser_pass("12345678");
    	User selectUser = SqlSession.executeSelectSql(sql, userBean, User.class);
    	System.out.println(selectUser.getUser_id());
        assertNotNull(selectUser);
    }
    
    // 插入用户信息
    public void testInsertUser(){
    	
    	String sql = "insert into user ( user_id,user_name,user_pass ) values ( #user_id#,#user_name#,#user_pass# )";
    	User userBean = new User();
    	userBean.setUser_id("skjanyou1");
    	userBean.setUser_name("skjanyou1");
    	userBean.setUser_pass("123456789");
    	int count = SqlSession.executeInsertSql(sql, userBean);
    	System.out.println(count);   	
    }
    
    // 更新用户信息
    public void testUpdateUser(){
    	String sql = "update user set user_name = #user_name# where user_id = #user_id#";
    	User userBean = new User();
    	userBean.setUser_id("skjanyou1");
    	userBean.setUser_name("测试用户名");
    	int count = SqlSession.executeUpdateSql(sql, userBean);
    	System.out.println(count);
    }

    // 更新用户信息
    public void testDeleteUser(){
    	String sql = "delete from user where user_id = #user_id#";
    	User userBean = new User();
    	userBean.setUser_id("skjanyou1");
    	int count = SqlSession.executeDeleteSql(sql, userBean);
    	System.out.println(count);
    }
    
    // 查询列表
    public void testQueryUser(){
    	String sql = "select * from user";
    	User userBean = new User();
    	List<User> list = SqlSession.executeSelectListSql(sql, userBean, User.class);
    	for (User user : list) {
			System.out.println(user.getUser_name());
		}
    }
}
