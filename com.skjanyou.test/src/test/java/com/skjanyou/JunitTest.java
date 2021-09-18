package com.skjanyou;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.skjanyou.test.extension.SkjanyouStartTest;

@SkjanyouStartTest( property = { "skjanyou.configfile=classpath:Junit5.properties" } )
public class JunitTest {
	@Test
	@DisplayName("第一次测试")
	public void firstTest() {
		System.out.println("hello world");
	}
}
