package com.skjanyou.util;

import java.security.SecureRandom;

public class StringUtil {
	private StringUtil(){}
	
	private static String[] items = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N"
			,"O","P","R","S","T","U","V","W","X","Y","Z","0","1","2","3","4","5","6"
			,"7","8","9"};
	
	/**
	 * 指定字符串长度,获取随机字符串
	 * @param num 字符串长度
	 * @return	字符串
	 */
	public static String randomString(Integer num){
		String result = "";
		int index; //num是字符长度，index是生成字符串的索引
		if(num == null){
			num = new SecureRandom().nextInt(items.length);
		}
		
		for(int i = 0;i < num;i++){
			index = new SecureRandom().nextInt(items.length);
			result += items[index];
		}
		return result;
	}
	
	/**
	 * 获取随机长度、随机字符串
	 * @return	字符串
	 */	
	public static String randomString(){
		return randomString(null);
	}
	
	//字符串
	
	
}
