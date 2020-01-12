package com.skjanyou.db.mybatis.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class StringUtil {
	public static String converFirstUpperCase(String str){
		return String.valueOf(str.charAt(0)).toUpperCase() + str.substring(1);
	}
	
	public static Map<String,Integer> getWird( String sql ){
		int length = sql.length();
		int index = 0;int size = 1;
		int startFlag = 0,endFlag = 0;
		Map<String,Integer> fieldMap = new LinkedHashMap<>();
		while( index < length ){
			char c = sql.charAt(index);
			if( c == '#' ){
				if( startFlag <= endFlag ){
					startFlag = index;
				}else{
					endFlag = index;
					String subString = sql.substring(startFlag + 1,endFlag);
					fieldMap.put(subString, size++);
				}
			}
			index++;
		}		
		return fieldMap;
	}
}
