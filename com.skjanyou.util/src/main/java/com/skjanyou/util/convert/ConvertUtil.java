package com.skjanyou.util.convert;

import java.util.ArrayList;
import java.util.List;

import com.skjanyou.util.convert.DefaultConvert.AnotherObjectConvertToString;
import com.skjanyou.util.convert.DefaultConvert.StringConvertToBoolean;
import com.skjanyou.util.convert.DefaultConvert.StringConvertToClass;
import com.skjanyou.util.convert.DefaultConvert.StringConvertToDouble;
import com.skjanyou.util.convert.DefaultConvert.StringConvertToFloat;
import com.skjanyou.util.convert.DefaultConvert.StringConvertToInteger;
import com.skjanyou.util.convert.DefaultConvert.StringConvertToList;
import com.skjanyou.util.convert.DefaultConvert.StringConvertToString;
import com.skjanyou.util.convert.DefaultConvert.StringConvertToMap;

public class ConvertUtil {
	private static List<ConvertProvider<?,?>> convertList = new ArrayList<>();
	static {
		ConvertUtil.regist(new StringConvertToString());
		ConvertUtil.regist(new StringConvertToInteger());
		ConvertUtil.regist(new StringConvertToBoolean());
		ConvertUtil.regist(new StringConvertToFloat());
		ConvertUtil.regist(new StringConvertToDouble());
		ConvertUtil.regist(new StringConvertToClass());
		ConvertUtil.regist(new StringConvertToMap());
		ConvertUtil.regist(new StringConvertToList());
		ConvertUtil.regist(new AnotherObjectConvertToString());
	}
	
	public static void regist( ConvertProvider<?, ?> convertProvider ){
		convertList.add(convertProvider);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object convert( Object distObject, Class<?> targetClass ){
		for (ConvertProvider convertProvider : convertList) {
			if( convertProvider.isMatch(distObject.getClass(), targetClass) ){
				return convertProvider.converTo(distObject,distObject.getClass(),targetClass);
			}
		}
		// 没有合适的转换器,直接返回默认原对象
		return distObject;
	}
}
