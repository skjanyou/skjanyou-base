package com.skjanyou.log.test;

import junit.framework.TestCase;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.start.LogStart;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.start.start.ApplicationStart;

public class LogTests extends TestCase {
	@Override
	public void runBare() throws Throwable {
		ApplicationStart.start(LogStart.class);
		super.runBare();
	}
	
	public void testInfo(  ) {
		Logger logger = LogUtil.getLogger(LogTests.class);
		logger.info("sdfsdaf","sfdasdfsf");
	}
}
