package com.skjanyou.db.mybatis.mapper;

import com.skjanyou.db.mybatis.anno.Mapper;
import com.skjanyou.db.mybatis.bean.User;
import com.skjanyou.db.mybatis.core.BeanMapper;

@Mapper
public interface TestUserMapper extends BeanMapper<User> {

}
