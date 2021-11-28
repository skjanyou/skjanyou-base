package com.skjanyou.practise;

import org.apache.shiro.codec.Base64;

public class Base64Test {

	public static void main(String[] args) {
		byte[] byteArr = new byte[] { -19, -65, 117, 82, 39, 69, 81, 25, 91, 74, 51, -25, 87, 93, 79, -123};
		String base64 = Base64.encodeToString(byteArr);
		byte[] aa = Base64.encode(byteArr);
		String base642 = Base64.encodeToString(aa);
		System.out.println(base64);
		System.out.println(base642);
	}

}
