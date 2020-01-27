package com.skjanyou.test;

import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.start.ApplicationStart;

@Configure(
	name = "test的props"
)
public class TestStart {
	public static void main(String[] args) {
		ApplicationStart.start(TestStart.class);
	}
}
