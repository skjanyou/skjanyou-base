package com.skjanyou.mvc.filter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skjanyou.server.api.constant.StatusCode;
import com.skjanyou.server.api.inter.AbstractHttpFilter;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.core.HttpHeaders;
import com.skjanyou.server.core.HttpProtocolLv1;
import com.skjanyou.server.core.HttpResponse;
import com.skjanyou.server.core.HttpResponse.HttpResponseBody;
import com.skjanyou.server.core.HttpResponse.HttpResponseLine;


public class ForwardFileter extends AbstractHttpFilter {
	private String ip;
	private int port;
	private String contextPath;
	private String forwardUrl;
	
	public ForwardFileter ( String ip,int port,String contextPath ) {
		this.ip = ip;
		this.port = port;
		this.contextPath = contextPath;
	}
	@Override
	public int getOrder() {
		return 1;
	}
	@Override
	public Filter init() {
		forwardUrl = MessageFormat.format("http://{0}:{1}/{2}", ip, port + "", contextPath);
		return this;
	}
	@Override
	public boolean doFilter(Request request, Response response) throws Exception {
		if( response instanceof HttpResponse ){
			String url = request.requestLine().url();
			URL forwarUrl = new URL(forwardUrl + url);
			HttpResponse httpResponse = (HttpResponse) response;
			HttpHeaders httpHeaders = httpResponse.getHttpHeaders();
			HttpResponseBody httpResponseBody = httpResponse.getHttpResponseBody();
			HttpResponseLine httpResponseLine = httpResponse.getHttpResponseLine();
			httpResponseLine.setProtocol(new HttpProtocolLv1());
			httpResponseLine.setStatusCode(StatusCode.Ok);
			
			URLConnection conn = forwarUrl.openConnection();
	        Map<String,List<String>> headers = conn.getHeaderFields();
	        Set<String> keys = headers.keySet();
	        for( String key : keys ){
	            String val = conn.getHeaderField(key);
	            httpHeaders.put(key, val);
	        }			
	        InputStream is = conn.getInputStream();
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        int len = -1;byte[] buff = new byte[2048];
	        while( ( len = is.read(buff) ) != -1 ){
	        	baos.write(buff, 0, len);
	        }
	        byte[] bodyContent = baos.toByteArray();
	        httpResponseBody.setBodyContent(bodyContent);
		}
		return false;
	}
}