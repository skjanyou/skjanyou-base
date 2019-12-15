package com.skjanyou.server;

import com.skjanyou.server.impl.http.HttpHeaders;
import com.skjanyou.server.inter.Headers;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest  extends TestCase
{
	public AppTest( String testName ){
		super( testName );
	}

	public static Test suite(){
		return new TestSuite( AppTest.class );
	}

	public void testSplitHeader(){
		String headerString = "Host : localhost:2333";
		Headers header = new HttpHeaders();
		header.converToHeaders(headerString);
		System.out.println(header.get("Host"));
		assertEquals("localhost:2333", header.get("Host"));
	}
	
}
