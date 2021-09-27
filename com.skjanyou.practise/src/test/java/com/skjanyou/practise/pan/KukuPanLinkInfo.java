package com.skjanyou.practise.pan;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
	
public class KukuPanLinkInfo {
	public static void main(String[] args) throws Exception {
		String url = "https://pan.kuku.me/qaq/Others/coser%E5%9B%BE/";
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		String html = EntityUtils.toString(httpResponse.getEntity());
		System.out.println(html);
		Document document = Jsoup.parse(html);
		document.select("");
	}
}
