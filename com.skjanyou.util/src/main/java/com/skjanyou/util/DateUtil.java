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
}
