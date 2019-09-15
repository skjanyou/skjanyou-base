package com.skjanyou.util;

import java.util.Map;

import com.skjanyou.util.proxy.annotation.Proxy;

@Proxy(TestProxy.class)
public class Test {
	private String aaa = "aaaa";
	public String getAaa() {
		return aaa;
	}
	@Proxy(TestProxy.class)
	public void setAaa(String aaa) {
		this.aaa = aaa;
	}
	public static void main(String[] args) {
//		Test t = new Test();
//		t.setAaa("aaaaaaaaaaaaaaaa");
//		Map<String,Object> map = BeanUtil.bean2Map(t);
//		System.out.println(map);
		
//		Test t2 = BeanUtil.createProxyBean(Test.class, Proxy.class);
//		t2.setAaa("sdfsdfdsf");
		
		Test t3 = BeanUtil.createProxyBean(Test.class, Proxy.class);
		t3.setAaa("sdfsdfdsf");
		
	}
}
