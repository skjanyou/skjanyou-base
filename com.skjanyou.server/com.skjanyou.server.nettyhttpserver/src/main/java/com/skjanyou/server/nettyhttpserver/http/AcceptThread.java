package com.skjanyou.server.nettyhttpserver.http;

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
	private NettyHttpServer httpServer;
	public AcceptThread( NettyHttpServer httpServer,Socket socket ){
		this.httpServer = httpServer;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		if(socket == null){
			throw new NullPointerException("Socket套接字为空!");
		}

		
	}
	
	
	
	@Override
	public int compareTo(AcceptThread o) {
		return this.priority - o.priority;
	}		
}
