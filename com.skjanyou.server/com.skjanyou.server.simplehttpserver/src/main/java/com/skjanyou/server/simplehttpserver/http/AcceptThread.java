package com.skjanyou.server.simplehttpserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.UUID;

import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.core.HttpRequest;
import com.skjanyou.server.core.HttpResponse;
import com.skjanyou.server.core.RequestBuilder;
import com.skjanyou.server.core.ResponseBuilder;
import com.skjanyou.server.core.ServerContext;
import com.skjanyou.util.CommUtil;
import com.skjanyou.util.StringUtil;

/**
 * 接收线程
 * @author skjanyou
 * 时间 : 2019年12月9日
 * 作用 :
 */
public class AcceptThread extends Thread implements Runnable,Comparable<AcceptThread> {
	private Socket socket;
	private int priority;
	private HttpServer httpServer;
	public AcceptThread( HttpServer httpServer,Socket socket ){
		this.httpServer = httpServer;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		if(socket == null){
			throw new NullPointerException("Socket套接字为空!");
		}
		HttpResponse response = new HttpResponse();
		HttpRequest request = null;
		InputStream is = null;
		OutputStream os = null;
		ServerContext.get().putContext("socket", socket);
		ServerContext.get().putContext("close", true);
		try {
			String ip = socket.getInetAddress().getHostAddress();
			is = socket.getInputStream();
			os = socket.getOutputStream();
			// 防止readLine阻塞
			socket.setSoTimeout(3000);
			socket.setKeepAlive(true);
			// 关闭Nagle算法
			// socket.setTcpNoDelay(true);
			// 构建请求对象
			request = RequestBuilder.solve(is);
			// 保存包装流
			ServerContext.get().putContext("reader", is);
			ServerContext.get().putContext("writer", os);
            // 获取请求头中Cookie字段,来获取SessionId
            String cookie = request.headers().get("cookie");
            String sessionId = null;
            if( !StringUtil.isBlank(cookie) ) {
            	if( cookie.contains("skjanyou_token_id") ) {
            		String[] cookieArr = cookie.split(";");
            		for (String string : cookieArr) {
            			if( string.contains("skjanyou_token_id") ) {
            				sessionId = string.trim().split("skjanyou_token_id=")[1];
            				break;
            			}
            		}
            	} else {
            		sessionId = UUID.randomUUID().toString();
            	}
            }else {
            	sessionId = UUID.randomUUID().toString();
            }
            response.getHttpHeaders().put("Set-Cookie", "skjanyou_token_id=" + sessionId);
            // 添加IP地址
            request.headers().put("remote-ip", ip);
            
            List<Filter> filterList = httpServer.getFilters();
            
            boolean allPass = true;
        	for (Filter filter : filterList) {
        		boolean isContinue = filter.doFilter(request, response);
        		if(!isContinue){ 
        			allPass = false;
        			break ;
        		}
        	}
            	
            if( allPass ){
            	httpServer.getHandler().handler(request, response);
            }
            
            byte[] responseBytes = ResponseBuilder.getResponseBytes(response);
            os.write(responseBytes);
            os.flush();
		} catch( SocketTimeoutException ste ) {
			// 超时问题,不做处理
		} catch (Exception e) {
			e.printStackTrace();
			if( os != null ) {
				try {
					os.write(e.getMessage().getBytes());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					os.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			if( (boolean) ServerContext.get().getContext("close") ) {
				CommUtil.close(os);
				
				CommUtil.close(is);			
				
				CommUtil.close(socket);
			}
		}
		
	}
	
	
	
	@Override
	public int compareTo(AcceptThread o) {
		return this.priority - o.priority;
	}		
}
