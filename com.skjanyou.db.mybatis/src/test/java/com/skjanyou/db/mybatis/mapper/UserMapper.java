package com.skjanyou.db.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.skjanyou.db.mybatis.anno.DDL.Delete;
import com.skjanyou.db.mybatis.anno.DDL.Insert;
import com.skjanyou.db.mybatis.anno.DDL.Select;
import com.skjanyou.db.mybatis.anno.DDL.SqlParameter;
import com.skjanyou.db.mybatis.anno.DDL.Update;
import com.skjanyou.db.mybatis.anno.Mapper;
import com.skjanyou.db.mybatis.bean.User;

@Mapper
public interface UserMapper {
	
	@Mapper
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

	@Mapper
	public static interface InsertUserMapper{
		// 情况1,通过bean插入单条记录
		@Insert("insert into user ( user_id,user_name,user_pass ) values ( #user_id#,#user_name#,#user_pass# )")
		public int insertUser( User user );
		// 情况2,通过SqlParameter插入数据
		@Insert("insert into user ( user_id,user_name,user_pass ) values ( #user_id#,#user_name#,#user_pass# )")
		public int insertUserByMap( Map<String,Object> map );
		// 情况3,通过SqlParameter插入数据		
		@Insert("insert into user ( user_id,user_name,user_pass ) values ( #user_id#,#user_name#,#user_pass# )")
		public int insertUserBySqlParameter( @SqlParameter("user_id") String user_id,@SqlParameter("user_name") String user_name,@SqlParameter("user_pass") String user_pass );		
	}
	
	@Mapper
	public static interface UpdateUserMapper{
		// 情况1,通过bean更新单条数据  
		@Update("update user set user_name = #user_name#,user_pass = #user_pass# where user_id = #user_id#")
		public int updateUser( User user );
		// 情况2,通过Map更新单条数据
		@Update("update user set user_name = #user_name#,user_pass = #user_pass# where user_id = #user_id#")
		public int updateUserByMap( Map<String,Object> map );
		// 情况3,通过SqlParameter更新单条数据
		@Update("update user set user_name = #user_name#,user_pass = #user_pass# where user_id = #user_id#")
		public int updateUserBySqlParameter( @SqlParameter("user_id") String user_id , @SqlParameter("user_name") String user_name,@SqlParameter("user_pass") String user_pass );		
	}
	
	@Mapper
	public static interface DeleteUserMapper{
		// 情况1,通过bean删除单条数据  
		@Delete("delete from user where user_id = #user_id#")
		public int deleteUser( User user );
		// 情况2,通过Map删除单条数据
		@Delete("delete from user where user_id = #user_id#")
		public int deleteUserByMap( Map<String,Object> map );
		// 情况3,通过SqlParameter更新单条数据
		@Delete("delete from user where user_id = #user_id#")
		public int deleteUserBySqlParameter( @SqlParameter("user_id") String user_id );		
	}	
}
