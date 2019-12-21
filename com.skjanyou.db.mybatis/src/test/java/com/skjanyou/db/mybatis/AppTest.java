package com.skjanyou.db.mybatis;

import com.skjanyou.db.mybatis.util.DBUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    public AppTest( String testName ){
        super( testName );
    }

    public static Test suite(){
        return new TestSuite( AppTest.class );
    }

    public void testMybatis(){
    	String className = "com.mysql.cj.jdbc.Driver";
    	String url = "jdbc:mysql://127.0.0.1:3306/blog?zeroDateTimeBehavior=CONVERT_TO_NULL&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
    	String user = "root";
    	String password = "123456";
    	DBUtil.init(className, url, user, password);
    	
    	String sql = "select * from user where user_id = #user_id# and user_name = #user_name# and user_pass = #user_pass#";
    	User userBean = new User();
    	userBean.setUser_id("skjanyou");
    	userBean.setUser_name("skjanyou");
    	userBean.setUser_pass("12345678");
    	User selectUser = DBUtil.get().executeSelectSql(sql, userBean, User.class);
    	System.out.println(selectUser.getUser_id());
        assertNotNull(selectUser);
    }
}
