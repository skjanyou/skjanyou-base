package com.skjanyou;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.skjanyou.test.extension.SkjanyouStartTest;

@SkjanyouStartTest
public class JunitTest {
	@Test
	@DisplayName("第一次测试")
	public void firstTest() {
		System.out.println("hello world");
	}
}
