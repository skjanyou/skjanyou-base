package com.skjanyou.server.impl.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.skjanyou.server.constant.ServerConst;
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
	private ServerHandler handler = new HttpServerHandler();
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
			Request request = createRequest( br );
			
            os = socket.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);
            
            Response response = createResponse( bw );
            
            handler.handler(request, response);
            
            
            String statusCode = response.responsefeaFeatures().statusCode().getCode() + "";
            String protocol = response.responsefeaFeatures().protocol().protocol() + "/" + response.responsefeaFeatures().protocol().version();
            bw.write(protocol + statusCode);
            // 写返回头
            String responseHeaderString = response.headers().toHttpHeaderString();
            bw.write(responseHeaderString);
            
            // 写正文
            byte[] bodyContent = response.responseBody().getBodyContent();
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
	
	protected Request createRequest( BufferedReader br ) {
		Request request = new HttpRequest();
		try {
            StringBuilder sb = new StringBuilder();
            // 1.获取第一行,获取请求类型和uri,构建RequestFeatures
            String firstLine = br.readLine();	
            request.requestFeatures().convertToRequestFeatures(firstLine);
            // 2.从第二行开始,全部为请求头
            String headerMsg = "";
            while(( headerMsg = br.readLine() ) != null && headerMsg.length() > 0){
                sb.append(headerMsg).append(ServerConst.LINEFEED);
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
