package com.skjanyou.server.impl.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import com.skjanyou.server.bean.ApplicateContext;
import com.skjanyou.server.constant.ServerConst;
import com.skjanyou.server.inter.Filter;
import com.skjanyou.server.inter.Headers;
import com.skjanyou.server.inter.Request;
import com.skjanyou.server.inter.Response;
import com.skjanyou.server.inter.ServerHandler;
import com.skjanyou.util.CommUtil;

/**
 * 接收线程
 * @author skjanyou
 * 时间 : 2019年12月9日
 * 作用 :
 */
public class AcceptThread extends Thread implements Runnable,Comparable<AcceptThread> {
	private Socket socket;
	private int priority;
	private ServerHandler handler = ApplicateContext.getServerHandler();
	public AcceptThread( Socket socket ){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		if(socket == null){
			throw new NullPointerException("Socket套接字为空!");
		}
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;		
		try {
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			Request request = resolveRequest( br );
			
            os = socket.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);
            
            Response response = createResponse( bw );
            
            List<Filter> filterList = ApplicateContext.getRegistedFilter();
            
            boolean allPass = true;
            for (Filter filter : filterList) {
            	boolean isContinue = filter.doFilter(request, response);
            	if(!isContinue){ 
            		allPass = false;
            		break ;
            	}
			}
            if( allPass ){
            	handler.handler(request, response);
            }
            
            
            String statusCode = response.responseLine().statusCode().getCode() + " " + response.responseLine().statusCode().getName();
            String protocol = response.responseLine().protocol().protocol() + "/" + response.responseLine().protocol().version();
            String statusLine = protocol + " " + statusCode;
            // 写状态头
            bw.write(statusLine);
            
            byte[] bodyContent = response.responseBody().getBodyContent();
            Headers responseHeaders = response.headers();
            responseHeaders.put("Content-Length", String.valueOf(bodyContent.length));
            if(responseHeaders.get("Content-Type") == null){
            	responseHeaders.put("Content-Type", "text/plain;charset=UTF-8");
            }
            // 写返回头
            String responseHeaderString = response.headers().toHttpHeaderString();
            bw.write(responseHeaderString);
            bw.write(ServerConst.CRLF);
            
            // 写正文
            bw.write(new String(bodyContent));
            bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommUtil.close(bw);
			CommUtil.close(osw);
			CommUtil.close(os);
			
			CommUtil.close(br);
			CommUtil.close(isr);
			CommUtil.close(is);			
		}
		

		
	}
	
	protected Request resolveRequest( BufferedReader br ) {
		Request request = new HttpRequest();
		try {
            StringBuilder sb = new StringBuilder();
            // 1.获取第一行,获取请求类型和uri,构建RequestFeatures
            String firstLine = br.readLine();	
            request.requestLine().convertToRequestLine(firstLine);
            // 2.从第二行开始,全部为请求头
            String headerMsg = "";
            while(( headerMsg = br.readLine() ) != null && headerMsg.length() > 0){
                sb.append(headerMsg).append(ServerConst.CRLF);
            }
            String requestInfo = sb.toString().trim();        
            System.out.println(requestInfo);	
            request.headers().converToHeaders(requestInfo);
            // 3.Post请求要通过Control-length获取请求体内容
            
            
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("读取数据出错!",e);
		} 
		
		return request;
	}
	
	protected Response createResponse( BufferedWriter bw ){
		Response response = new HttpResponse();
		 	
		return response;
	}
	
	@Override
	public int compareTo(AcceptThread o) {
		return this.priority - o.priority;
	}		
}
