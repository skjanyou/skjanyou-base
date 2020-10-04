package com.skjanyou.log.test;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.start.LogStart;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.start.start.SkjanyouApplicationStart;

import junit.framework.TestCase;

public class LogTests extends TestCase {
	@Override
	public void runBare() throws Throwable {
		SkjanyouApplicationStart.start(LogStart.class,new String[] { "start" });
		super.runBare();
	}
	
	public void testInfo(  ) {
		Logger logger = LogUtil.getLogger(LogTests.class);
		logger.info("sdfsdaf","sfdasdfsf");
	}
}
