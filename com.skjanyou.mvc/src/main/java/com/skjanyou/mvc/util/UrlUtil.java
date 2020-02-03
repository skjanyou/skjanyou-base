package com.skjanyou.mvc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.skjanyou.util.CommUtil;

public class UrlUtil {
	
	public static String fechchHtml( InputStream is ) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(is,"utf-8");
			//为字符输入流添加缓冲
			br = new BufferedReader(isr);
			String data = null;//读取数据
			
			while ( ( data = br.readLine() ) != null ){//循环读取数据
				sb.append(data);
			}		
		} catch (IOException e) {
			throw e;
		} finally {
			CommUtil.close(br);
			CommUtil.close(isr);
			CommUtil.close(is);
		}
		return sb.toString();		
	}
	
	public static String fetchHtml( URL url ) throws IOException{
		String result = null;
		InputStream is = null;
		try {
			is = url.openStream();
			result = fechchHtml(is);	
		} catch (IOException e) {
			throw e;
		} finally {
			CommUtil.close(is);
		}
		return result;
	}
}
