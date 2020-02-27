package com.skjanyou.log.start;

import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.start.ApplicationStart;

@Configure(
	name = "log配置Props",
	scanPath = "com.skjanyou.log"
)
public class LogStart {

	public static void main(String[] args) {
		ApplicationStart.start(LogStart.class);
	}

}
