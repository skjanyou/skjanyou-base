package com.skjanyou.util.convert;

import com.alibaba.fastjson.JSON;
import com.skjanyou.util.ClassUtil;

public final class DefaultConvert {
	static {
		ConvertUtil.regist(new StringConvertToString());
		ConvertUtil.regist(new StringConvertToInteger());
		ConvertUtil.regist(new StringConvertToBoolean());
		ConvertUtil.regist(new StringConvertToFloat());
		ConvertUtil.regist(new StringConvertToDouble());
		ConvertUtil.regist(new AnotherObjectConvertToString());
	}
	
	
	/** String类型->String类型 **/
	public static class StringConvertToString implements ConvertProvider<String, String> {

		@Override
		public boolean isMatch(Class<?> distClass, Class<?> targetClass) {
			return String.class == distClass && String.class == targetClass;
		}

		@Override
		public String converTo(String dist) {
			return dist;
		}

		@Override
		public int order() {
			return 100;
		}

	}	
	/** String类型->Integer类型 **/
	public static class StringConvertToInteger implements ConvertProvider<String, Integer> {
		@Override
		public boolean isMatch(Class<?> distClass, Class<?> targetClass) {
			return String.class == distClass && ( Integer.class == targetClass || int.class == targetClass );
		}

		@Override
		public Integer converTo(String dist) {
			return Integer.parseInt(dist);
		}

		@Override
		public int order() {
			return 100;
		}
	}	
	
	/** String类型->Boolean类型 **/
	public static class StringConvertToBoolean implements ConvertProvider<String,Boolean> {
		@Override
		public boolean isMatch(Class<?> distClass, Class<?> targetClass) {
			return String.class == distClass && ( Boolean.class == targetClass || boolean.class == targetClass );
		}
		
		@Override
		public Boolean converTo(String dist) {
			return Boolean.parseBoolean(dist);
		}

		@Override
		public int order() {
			return 100;
		}
	}
	
	/** String类型->float类型 **/
	public static class StringConvertToFloat implements ConvertProvider<String, Float> {
		@Override
		public boolean isMatch(Class<?> distClass, Class<?> targetClass) {
			return String.class == distClass && ( Float.class == targetClass || float.class == targetClass );
		}
		@Override
		public Float converTo(String dist) {
			return Float.parseFloat(dist);
		}
		@Override
		public int order() {
			return 100;
		}
	}
	
	/** String类型->double类型 **/
	public static class StringConvertToDouble implements ConvertProvider<String,Double> {
		@Override
		public boolean isMatch(Class<?> distClass, Class<?> targetClass) {
			return String.class == distClass && ( Double.class == targetClass || double.class == targetClass );
		}
		@Override
		public Double converTo(String dist) {
			return Double.parseDouble(dist);
		}
		@Override
		public int order() {
			return 100;
		}
	}
	
	public static class AnotherObjectConvertToString implements ConvertProvider<Object,String> {

		@Override
		public boolean isMatch(Class<?> distClass, Class<?> targetClass) {
			return !ClassUtil.isSimpleDataType(distClass);
		}

		@Override
		public String converTo(Object dist) {
			if( dist == null ){ return ""; }
			String result = null;
			try{
				result = JSON.toJSONString(dist);
			} catch( Exception e ){
				result = dist.toString();
			}
			return result;
		}

		@Override
		public int order() {
			// 前面所有的转换器都没有使用的情况下,尝试使用这个进行转换
			return Integer.MAX_VALUE;
		}
		
	}
}
