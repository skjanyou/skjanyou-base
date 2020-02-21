package com.skjanyou.server.simplehttpserver.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import com.skjanyou.server.api.bean.ApplicateContext;
import com.skjanyou.server.api.constant.ServerConst;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.api.inter.ServerHandler;
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
		HttpResponse response = new HttpResponse();
		HttpRequest request = new HttpRequest();
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		OutputStream os = null;
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			// 防止readLine阻塞
			socket.setSoTimeout(3000);
            StringBuilder sb = new StringBuilder();
            // 1.获取第一行,获取请求类型和uri,构建RequestFeatures
            String firstLine = br.readLine();	
            if( firstLine == null ){
                os.write(new byte[0]);
                os.flush();
                return ;
            }
            request.requestLine().convertToRequestLine(firstLine);
            // 2.从第二行开始,全部为请求头
            String headerMsg = "";
            while(( headerMsg = br.readLine() ) != null && headerMsg.length() > 0){
                sb.append(headerMsg).append(ServerConst.CRLF);
            }
            String requestInfo = sb.toString().trim();        
            request.headers().converToHeaders(requestInfo);
            // 3.Post请求要通过Control-length获取请求体内容
            
            
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
            
            byte[] responseBytes = ResponseBuilder.getResponseBytes(response);
            os.write(responseBytes);
            os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommUtil.close(os);
			
			CommUtil.close(br);
			CommUtil.close(isr);
			CommUtil.close(is);			
		}
		

		
	}
	
	
	
	@Override
	public int compareTo(AcceptThread o) {
		return this.priority - o.priority;
	}		
}
