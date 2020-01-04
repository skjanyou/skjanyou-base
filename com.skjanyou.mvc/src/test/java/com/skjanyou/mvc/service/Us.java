package com.skjanyou.mvc.service;

import com.skjanyou.mvc.anno.Mvc.Service;

@Service("userService")
public class Us implements UserService {

	@Override
	public String queryUser() {
		System.out.println("this is Us !");
		return "2";
	}

}
