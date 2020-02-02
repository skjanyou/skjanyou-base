package com.skjanyou.mvc.filter;

import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skjanyou.mvc.util.UrlUtil;
import com.skjanyou.server.api.constant.StatusCode;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;
import com.skjanyou.server.simplehttpserver.http.HttpHeaders;
import com.skjanyou.server.simplehttpserver.http.HttpProtocolLv1;
import com.skjanyou.server.simplehttpserver.http.HttpResponse;
import com.skjanyou.server.simplehttpserver.http.HttpResponse.HttpResponseBody;
import com.skjanyou.server.simplehttpserver.http.HttpResponse.HttpResponseLine;


public class ForwardFileter implements Filter {
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
	public int priority() {
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
	        String html = UrlUtil.fechchHtml(conn.getInputStream());
	        httpResponseBody.setBodyContent(html);
		}
		return false;
	}
	@Override
	public Filter destroy() {
		return null;
	}
}