package com.skjanyou.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static Date getCurrentTime(){
		return new Date();
	}

	public static String getFormatTime() {
		String format = "yyyy-MM-dd HH:mm:ss";
		Date date = getCurrentTime();
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static Date parse( String date , String format ) {
		return null;
	}
	
	public static String format( Date date, String format ) {
		return null;
	}
	
	public static String year( Date date ) {
		return null;
	}
	
	public static String month( Date date ) {
		return null;
	}
	
	public static String offset( String date , int type , int num ) {
		return null;
	}
	
	//昨天
	public static String yesterday() {
		return null;
	}
	//明天
	public static String tomorrow() {
		return null;
	}
	//上周
	public static String lastWeek() {
		return null;
	}
	//下周
	public static String nextWeek() {
		return null;
	}
	//上个月
	public static String lastMonth() {
		return null;
	}
	//下个月
	public static String nextMonth() {
		return null;
	}
}
