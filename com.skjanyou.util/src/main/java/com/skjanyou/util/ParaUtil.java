package com.skjanyou.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**从配置文件中取参数**/
public class ParaUtil {
	private static String file = "src/main/webapp/resources/account.properties";
	public static String get(String key){
		String result = null;
		FileInputStream fis = null;
		Properties properties = new Properties();
		try {
			fis = new FileInputStream(new File(file));
			properties.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�����ļ�������߲�����");
		}finally{
			try {
				fis.close();
			} catch (IOException e) {
			}
		}
		result = properties.get(key).toString();
		return result;
	}
	
	public static void main(String[] args) {
		String username = get("username");
		System.out.println(username);
	}
}
