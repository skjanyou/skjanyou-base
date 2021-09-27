package com.skjanyou.practise.test;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpTest {

	public static void main(String[] args) throws Exception {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		
		HttpGet httpGet = new HttpGet("http://127.0.0.1:1234");
		
		CloseableHttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		String html = EntityUtils.toString(entity);
		System.out.println(html);
		
	}

}