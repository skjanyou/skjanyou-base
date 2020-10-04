package com.skjanyou.log.start;

import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.start.SkjanyouApplicationStart;

@Configure(
	name = "log配置Props",
	scanPath = "com.skjanyou.log"
)
public class LogStart {

	public static void main(String[] args) {
		SkjanyouApplicationStart.start(LogStart.class,args);
	}

}
