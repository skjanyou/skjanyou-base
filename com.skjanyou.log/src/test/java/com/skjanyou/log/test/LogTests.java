package com.skjanyou.log.test;

import junit.framework.TestCase;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;

public class LogTests extends TestCase {
	@Override
	public void runBare() throws Throwable {
		super.runBare();
	}
	
	public void testInfo(  ) {
		Logger logger = LogUtil.getLogger(LogTests.class);
		logger.info("sdfsdaf","sfdasdfsf");
	}
}
