package com.skjanyou.protocol;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import com.skjanyou.protocol.core.ProtocolManager;

public class ProtocolTest {

	public static void main(String[] args) throws Exception {
		ProtocolManager.init();
		
		URL url = new URL("classpath://1.properties");
		InputStream input = url.openStream();
		System.out.println(input);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1; byte[] buff = new byte[4*1024];
		while( ( i = input.read(buff)) != -1 ) {
			baos.write(buff,0,i);
		}
		
		
		byte[] resultArr = baos.toByteArray();
		String words = new String(resultArr);
		System.out.println(words);
	}

}
