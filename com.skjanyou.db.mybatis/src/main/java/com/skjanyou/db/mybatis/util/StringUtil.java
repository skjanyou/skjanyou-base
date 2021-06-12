package com.skjanyou.db.mybatis.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class StringUtil {
	public static String converFirstUpperCase(String str){
		return String.valueOf(str.charAt(0)).toUpperCase() + str.substring(1);
	}
	
	private static Map<String,Integer> getWirdByString( String sql, char cc){
		int length = sql.length();
		int index = 0;int size = 1;
		int startFlag = 0;int endFlag = 0;
		Map<String, Integer> fieldMap = new LinkedHashMap<>();
		while (index < length)
		{
			char c = sql.charAt(index);
			if (c == cc) {
				if (startFlag <= endFlag)
				{
					startFlag = index;
				}
				else
				{
					endFlag = index;
					String subString = sql.substring(startFlag + 1, endFlag);
					fieldMap.put(subString, Integer.valueOf(size++));
				}
			}
			index++;
		}
		return fieldMap;
	}    
    
	public static Map<String,Integer> getReplaceWird(String sql){
		return getWirdByString(sql,'$');
	}
	
	public static Map<String, Integer> getWird(String sql)
	{
		return getWirdByString(sql,'#');
	}  
}
