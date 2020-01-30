package com.skjanyou.mvc.core.convert;

public final class DefaultConvert {
	static {
		Converts.regist(new StringConvertToString());
		Converts.regist(new StringConvertToInteger());
		Converts.regist(new StringConvertToBoolean());
		Converts.regist(new StringConvertToFloat());
		Converts.regist(new StringConvertToDouble());
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
	}
}
