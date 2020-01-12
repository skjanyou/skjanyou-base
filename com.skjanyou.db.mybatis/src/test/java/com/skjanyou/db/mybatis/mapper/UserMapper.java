package com.skjanyou.db.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.skjanyou.db.mybatis.anno.DDL.Select;
import com.skjanyou.db.mybatis.anno.DDL.SqlParameter;
import com.skjanyou.db.mybatis.bean.User;

public interface UserMapper {
	
	public static interface SelectUserMapper{
		// 情况1,由一个对象查一条数据
		@Select("select * from user where user_id = #user_id# and user_name = #user_name# and user_pass = #user_pass#")
		public User select( User user );
		// 情况2,不输入条件查所有
		@Select("select * from user")
		public List<User> query( );
		// 情况3,使用Map查询一条数据
		@Select("select * from user where user_id = #user_id# and user_name = #user_name# and user_pass = #user_pass#")
		public User selectByMap( Map<String,Object> map );
		// 情况4,使用Map查询所有
		@Select("select * from user where user_id like #user_id#")
		public List<User> selectListByMap( Map<String,Object> map );		
		// 情况5,使用注解SqlParameter查询一条数据
		@Select("select * from user where user_id = #user_id# and user_name = #user_name# and user_pass = #user_pass#")
		public User selectBySqlParameter( @SqlParameter("user_id") String user_id,@SqlParameter("user_name") String user_name,@SqlParameter("user_pass") String user_pass );
		// 情况5,使用注解SqlParameter查询一条数据
		@Select("select * from user where user_id like #user_id#")
		public List<User> selectListBySqlParameter( @SqlParameter("user_id") String user_id );		
	}

}
