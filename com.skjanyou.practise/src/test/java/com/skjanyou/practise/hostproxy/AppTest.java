package com.skjanyou.practise.hostproxy;

import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class AppTest {
	public static void main(String[] args) throws Exception {
		Socket.setSocketImplFactory(new SocketImplFactory() {
			@Override
			public SocketImpl createSocketImpl() {
				System.out.println("createSocketImpl");
				return new TestSocketSocketImpl();
			}
		});
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet("http://www.baidu.com");
		HttpResponse response = httpClient.execute(get);
		System.out.println(EntityUtils.toString(response.getEntity()));	
	}
}
