package com.skjanyou.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtil {

	public static<T> String toJSONString( T bean ) {
		String result = "";
		if( bean == null ) {
			return result;
		}

		Class<?> tClass = bean.getClass();
		// 如果为数组
		if( tClass.isArray() ) {
			result += "[";
			Object[] array = (Object[]) bean;
			for (Object object : array) {
				result += toJSONString(object) + " ,";
			}
			if( result.endsWith(",") ) {
				result = result.substring(0, result.length() - 1);
			}
			result += "]";
		};
		// 如果为Collection
		if( Collection.class.isAssignableFrom(tClass) ) {
			result += "["; 
			Collection<?> coll = (Collection<?>) bean;
			for (Object object : coll) {
				result += toJSONString(object) + " ,";
			}
			if( result.endsWith(",") ) {
				result = result.substring(0, result.length() - 1);
			}
			result += "]";
		}

		// 如果为Map
		if( Map.class.isAssignableFrom(tClass) ) {
			Map<Object,Object> map = (Map<Object, Object>) bean;
			Iterator<Entry<Object,Object>> it = map.entrySet().iterator();
			result += "{";
			while( it.hasNext() ) {
				Entry<Object,Object> next = it.next();
				Object key = next.getKey();
				Object value = next.getValue();
				result += "\"" + key.toString() + "\" : " + toJSONString(value) + " ,";
			}
			if( result.endsWith(",") ) {
				result = result.substring(0, result.length() - 1);
			}
			result += "}";
		}

		if( !tClass.isArray() && !Collection.class.isAssignableFrom(tClass) && !Map.class.isAssignableFrom(tClass) ) {
			result = "\"" + bean.toString() + "\"";
		}

		return result;
	}

	public static<T> T toJavaObject( String jsonString,Class<T> targetClass ) {
		return null;
	}

	/**
	 * 
	 *   版权声明：本文为CSDN博主「Jerry Xxx」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
	 *   原文链接：https://blog.csdn.net/weixin_42540829/article/details/85713274	
     * <B>方法名称：</B>校验是否是有效JSON数据<BR>
     * <B>概要说明：</B>由于JAVA正则表达式没法递归，不能一个表达式进行匹配，只能用JAVA进行递归
         *  字符串传来后进行匹配，普通类型数据仅匹配格式不捕获，将可能的JSON类型（[] {}）进行捕获，
        *   递归进行校验，共设置四个捕获组，为了保证逗号分隔的格式是严格正确的，没有想到好的方法简化正则表达式
         *   只能把数据分成两类，一类带逗号一类不带分别进行匹配.由于捕获组仅能匹配最后一个捕获结果，所以需要手动 进行字符串截取进行递归验证。
     * 
         * 严格按照JSON官网给出的数据格式 双引号引起来的字符串 数字 JSONOBJECT JSONARRAY 波尔值和JSONNull
         * 在[]{}以及逗号前后可以有任意空字符。 <BR>
     * 
     * @param value 数据
     * @return boolean 是/不是
     */
	public static boolean isJSON(String value) {
		try {
			boolean result = false;
			String jsonRegexp = "^(?:(?:\\s*\\[\\s*(?:(?:"
					+ "(?:\"[^\"]*?\")|(?:true|false|null)|(?:[+-]?\\d+(?:\\.?\\d+)?(?:[eE][+-]?\\d+)?)|(?<json1>(?:\\[.*?\\])|(?:\\{.*?\\})))\\s*,\\s*)*(?:"
					+ "(?:\"[^\"]*?\")|(?:true|false|null)|(?:[+-]?\\d+(?:\\.?\\d+)?(?:[eE][+-]?\\d+)?)|(?<json2>(?:\\[.*?\\])|(?:\\{.*?\\})))\\s*\\]\\s*)"
					+ "|(?:\\s*\\{\\s*"
					+ "(?:\"[^\"]*?\"\\s*:\\s*(?:(?:\"[^\"]*?\")|(?:true|false|null)|(?:[+-]?\\d+(?:\\.?\\d+)?(?:[eE][+-]?\\d+)?)|(?<json3>(?:\\[.*?\\])|(?:\\{.*?\\})))\\s*,\\s*)*"
					+ "(?:\"[^\"]*?\"\\s*:\\s*(?:(?:\"[^\"]*?\")|(?:true|false|null)|(?:[+-]?\\d+(?:\\.?\\d+)?(?:[eE][+-]?\\d+)?)|(?<json4>(?:\\[.*?\\])|(?:\\{.*?\\}))))\\s*\\}\\s*))$";

			Pattern jsonPattern = Pattern.compile(jsonRegexp);

			Matcher jsonMatcher = jsonPattern.matcher(value);

			if (jsonMatcher.matches()) {
				result = true;
				for (int i = 4; i >= 1; i--) {
					if (!StringUtil.isBlank(jsonMatcher.group("json" + i))) {
						result = isJSON(jsonMatcher.group("json" + i));
						if (!result) {
							break;
						}
						if (i == 3 || i == 1) {
							result = isJSON(value.substring(0, jsonMatcher.start("json" + i))
									+ (i == 3 ? "\"JSON\"}" : "\"JSON\"]"));
							if (!result) {
								break;
							}
						}
					}
				}

			}
			return result;
		} catch (Exception e) {
			return false;
		}
	}	
	
	public static void main(String[] args) {
		Map<String,Object> m1 = new HashMap<String, Object>();
		m1.put("test", "sdfsdf");

		String[] arr = new String[] { "a", "b", "c" };

		List<String> list = new ArrayList<>();

		list.add("aa");list.add("bb");list.add("cc");

		m1.put("array", arr);m1.put("list", list);

		String jsonString = toJSONString(m1);

		System.out.println(jsonString);
	}

}
