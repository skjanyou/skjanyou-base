package com.skjanyou.mvc.controller;

import com.skjanyou.mvc.anno.Mvc.Controller;
import com.skjanyou.mvc.anno.Mvc.Mapping;
import com.skjanyou.mvc.anno.Mvc.Service;
import com.skjanyou.mvc.service.UserService;

@Controller("user")
public class UserController {
	@Service("abstractUserService")
	private UserService userService;
	
	@Mapping("/detail/{userId}")
	public Object queryUser(  ){
		String userId = userService.queryUser();
		System.out.println("userId : " + userId);
		return "a user";
	}
}
