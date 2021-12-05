package com.skjanyou.server.nettyhttpserver.util;

import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.internal.StringUtil;

public class NettyUtil {

	public static byte[] getRequestBytes( FullHttpRequest fullRequest ) {
		String result = getRequestString(fullRequest);
		return result.getBytes();
	}
	
	public static String getRequestString( FullHttpRequest fullRequest ) {
		StringBuilder result = new StringBuilder();
		appendInitialLine( result, fullRequest );
		appendHeaders( result, fullRequest.headers() );
		if( fullRequest.method() == HttpMethod.POST ) {
			result.append(StringUtil.NEWLINE);
		}else {
			removeLastNewLine(result);
		}
		
		ByteBuf content = fullRequest.content();
		byte[] reqContent = new byte[content.readableBytes()];
		content.readBytes(reqContent);

		String body = new String(reqContent);		
		result.append(body);
		
		return result.toString();
	}
	
    private static void appendInitialLine(StringBuilder buf, HttpRequest req) {
        buf.append(req.method());
        buf.append(' ');
        buf.append(req.uri());
        buf.append(' ');
        buf.append(req.protocolVersion());
        buf.append(StringUtil.NEWLINE);
    }
    
    private static void appendHeaders(StringBuilder buf, HttpHeaders headers) {
        for (Map.Entry<String, String> e: headers) {
            buf.append(e.getKey());
            buf.append(": ");
            buf.append(e.getValue());
            buf.append(StringUtil.NEWLINE);
        }
    }

    private static void removeLastNewLine(StringBuilder buf) {
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
    }    
    
}
