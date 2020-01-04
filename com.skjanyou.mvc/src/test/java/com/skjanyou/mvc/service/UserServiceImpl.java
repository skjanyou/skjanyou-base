package com.skjanyou.mvc.service;

import com.skjanyou.mvc.anno.Mvc.Service;


@Service("abstractUserService")
public class UserServiceImpl implements UserService {
	@Service("userService")
	private UserService us;
		
	@Override
	public String queryUser() {
		System.out.println("this is abstractUserService!");
		return us.queryUser();
	}

}
