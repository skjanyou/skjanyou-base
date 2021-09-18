package com.skjanyou.server.simplehttpserver.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.server.api.bean.ServerConfig;
import com.skjanyou.util.CommUtil;

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
	private Logger logger = LogUtil.getLogger(DispatchThread.class);
	public DispatchThread( HttpServer httpServer ){
		this.httpServer = httpServer;
		pool = new ThreadPoolExecutor(15, 20, 1000, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(),new ThreadFactory() {
			int i = 0;
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("Http访问线程" + i++);
				return t;
			}
		},new ThreadPoolExecutor.AbortPolicy());
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
			} catch( SocketTimeoutException ste ) {
				// java socket中,调用accept会使用SoTimeout的数值作为超时时间,超过这个时间还没有链接进来的话
				// 就会抛出SocketTimeoutException异常,所以这里不用管这个异常,开始下一次循环即可
			} catch (IOException e) {
				if( this.isRunning ) {
					logger.error(e);
				}
			}
		}
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
		if( !this.isRunning ) {
			CommUtil.close(this.serverSocket);
		}
	}
}
