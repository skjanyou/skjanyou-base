package com.skjanyou.server.simplehttpserver.http;

import com.skjanyou.server.api.constant.ServerConst;
import com.skjanyou.server.api.inter.Headers;
import com.skjanyou.util.ByteUtil;
import com.skjanyou.util.DateUtil;

public class ResponseBuilder {
	public static byte[] getResponseBytes( HttpResponse response ){
		byte[] result = null;
		StringBuilder sb = new StringBuilder();
        String statusCode = response.responseLine().statusCode().getCode() + " " + response.responseLine().statusCode().getName();
        String protocol = response.responseLine().protocol().protocol() + "/" + response.responseLine().protocol().version();
        String statusLine = protocol + " " + statusCode;
        // 写状态头
        sb.append(statusLine).append(ServerConst.CRLF);
        
        byte[] bodyContent = response.responseBody().getBodyContent();
        if( bodyContent == null ){
        	bodyContent = new byte[0];
        }
        Headers responseHeaders = response.headers();
        responseHeaders.put("Content-Length", String.valueOf(bodyContent.length));
        if(responseHeaders.get("Content-Type") == null){
        	responseHeaders.put("Content-Type", "text/plain;charset=UTF-8");
        }
        
        // Test
        responseHeaders.put("Date", DateUtil.getFormatTime());
        responseHeaders.put("Server", "skjanyou simplehttpserver");
        
        // 写返回头
        String responseHeaderString = response.headers().toHttpHeaderString();
        sb.append(responseHeaderString).append(ServerConst.CRLF);
        // 写正文
		byte[] header = sb.toString().getBytes();
        
        result = ByteUtil.byteMerger( header, bodyContent);
		return result;
	}
}
