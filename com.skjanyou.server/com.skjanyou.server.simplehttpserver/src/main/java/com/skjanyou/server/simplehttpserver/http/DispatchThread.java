package com.skjanyou.server.simplehttpserver.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.skjanyou.server.api.bean.ServerConfig;

/**
 * 分发线程
 * @author skjanyou
 * 时间 : 2019年12月9日
 * 作用 : 监听
 */
public class DispatchThread extends Thread implements Runnable {
	private static ExecutorService pool;
	private HttpServer httpServer;
	private ServerSocket serverSocket;
	private boolean isRunning;
	public DispatchThread( HttpServer httpServer ){
		this.httpServer = httpServer;
		pool = new ThreadPoolExecutor(1, 2, 1000, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
		this.isRunning = true;
	}
	@Override
	public void run() {
		ServerConfig config = httpServer.getConfig();
		try {
			serverSocket = new ServerSocket(config.getPort());
			serverSocket.setSoTimeout((int) config.getTimeout());
		} catch (IOException e) {
			throw new RuntimeException("启动服务失败",e);
		}
		while(isRunning){
			try {
				Socket socket = serverSocket.accept();
				Runnable runnable = new AcceptThread(httpServer,socket);
				pool.execute(runnable);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
