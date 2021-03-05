package com.skjanyou.mvc;

import java.lang.reflect.Field;

import com.skjanyou.mvc.controller.UserController;
import com.skjanyou.mvc.handler.AutowireMvcHandler;
import com.skjanyou.server.api.inter.ServerHandler;

/**
 * Unit test for simple App.
 */
public class AppTest 	 {


    public void testMvcHandler() throws Exception{
    	ServerHandler handler = new AutowireMvcHandler("com.skjanyou.mvc");
    	handler.init();
    }
    
    public static void main(String[] args) {
		UserController uc = new UserController();
		Field[] fileds = uc.getClass().getDeclaredFields();
		for (Field field : fileds) {
			System.out.println(field);
		}
	}
}
