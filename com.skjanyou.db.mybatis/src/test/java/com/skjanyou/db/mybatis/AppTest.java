package com.skjanyou.db.mybatis;

import java.util.List;

import com.skjanyou.db.mybatis.bean.User;
import com.skjanyou.db.mybatis.core.SqlSession;
import com.skjanyou.db.mybatis.test.MybatisTest;
import com.skjanyou.db.plugin.DBPlugin;
import com.skjanyou.db.util.DataSourceManager;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends MybatisTest {

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
    	long time1 = System.currentTimeMillis();
    	User selectUser = SqlSession.executeSelectSql(sql, userBean, User.class,defaultDataSourceManager);
    	long time2 = System.currentTimeMillis();
    	System.out.println( time2 - time1 );
    	System.out.println(selectUser.getUser_id());
        assertNotNull(selectUser);
        long time3 = System.currentTimeMillis();
    	User selectUser2 = SqlSession.executeSelectSql(sql, userBean, User.class,defaultDataSourceManager);
    	long time4 = System.currentTimeMillis();
    	System.out.println( time4 - time3 );
    	System.out.println(selectUser2.getUser_id());
        assertNotNull(selectUser2);        
    }
    
    // 插入用户信息
    public void testInsertUser(){
    	
    	String sql = "insert into user ( user_id,user_name,user_pass ) values ( #user_id#,#user_name#,#user_pass# )";
    	User userBean = new User();
    	userBean.setUser_id("skjanyou1");
    	userBean.setUser_name("skjanyou1");
    	userBean.setUser_pass("123456789");
    	int count = SqlSession.executeInsertSql(sql, userBean,defaultDataSourceManager);
    	System.out.println(count);   	
    }
    
    // 更新用户信息
    public void testUpdateUser(){
    	String sql = "update user set user_name = #user_name# where user_id = #user_id#";
    	User userBean = new User();
    	userBean.setUser_id("skjanyou1");
    	userBean.setUser_name("测试用户名");
    	int count = SqlSession.executeUpdateSql(sql, userBean,defaultDataSourceManager);
    	System.out.println(count);
    }

    // 更新用户信息
    public void testDeleteUser(){
    	String sql = "delete from user where user_id = #user_id#";
    	User userBean = new User();
    	userBean.setUser_id("skjanyou1");
    	int count = SqlSession.executeDeleteSql(sql, userBean,defaultDataSourceManager);
    	System.out.println(count);
    }
    
    // 查询列表
    public void testQueryUser(){
    	String sql = "select * from user";
    	User userBean = new User();
    	List<User> list = SqlSession.executeSelectListSql(sql, userBean, User.class,defaultDataSourceManager);
    	for (User user : list) {
			System.out.println(user.getUser_name());
		}
    }
}
