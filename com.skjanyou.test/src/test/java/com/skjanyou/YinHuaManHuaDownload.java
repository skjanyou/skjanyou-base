package com.skjanyou;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class YinHuaManHuaDownload {

	public static void main(String[] args) throws Exception {
		String url = "http://ysgz4.net/arst9_873323.html";
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		System.out.println(EntityUtils.toString(httpResponse.getEntity()));
		
		
	}

}
