package com.skjanyou.server.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.server.api.constant.ServerConst;
import com.skjanyou.server.util.SocketUtil;
import com.skjanyou.util.StringUtil;

public class RequestBuilder {
	private static Logger logger = LogUtil.getLogger(RequestBuilder.class);
	
	
	public static HttpRequest solve( InputStream is ) throws Exception {
		byte[] httpRequestReportByte = SocketUtil.inputStreamToDataByte(is);
		logger.debug(new String(httpRequestReportByte));
		return solve(httpRequestReportByte);
	}
	
	public static HttpRequest solve( byte[] byteArr ) throws Exception {
		HttpRequest request = new HttpRequest();
		
		InputStream input = new ByteArrayInputStream(byteArr);
		InputStreamReader isr = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(isr);
		
		// 1.获取第一行,获取请求类型和uri,构建RequestFeatures
        String firstLine = br.readLine();	
        request.requestLine().convertToRequestLine(firstLine);
        // 2.从第二行开始,全部为请求头
        StringBuffer sb = new StringBuffer();
        String headerMsg = "";
        while(( headerMsg = br.readLine() ) != null && headerMsg.length() > 0){
            sb.append(headerMsg).append(ServerConst.CRLF);
        }
        String requestInfo = sb.toString().trim();        
        request.headers().converToHeaders(requestInfo);
        // 3.Post请求要通过Content-Length获取请求体内容
        if( "POST".equalsIgnoreCase(request.requestLine().method()) ){
        	String contentlLengthString = request.headers().get("content-length");
        	if( !StringUtil.isBlank(contentlLengthString) ){
        		int controlLength = Integer.parseInt(contentlLengthString);
        		char[] buffChar = new char[controlLength];
        		br.read(buffChar,0,controlLength);
        		String body = new String(buffChar);
        		
        		request.requestBody().convertToRequestbody(body);
        	}
        }
		
		
		return request;
	}
	
}
