package com.skjanyou.server.nettyhttpserver.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.server.api.inter.Filter;
import com.skjanyou.server.core.HttpRequest;
import com.skjanyou.server.core.HttpResponse;
import com.skjanyou.server.core.RequestBuilder;
import com.skjanyou.server.core.ResponseBuilder;
import com.skjanyou.server.core.ServerContext;
import com.skjanyou.util.CommUtil;
import com.skjanyou.util.StringUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import io.netty.util.ReferenceCountUtil;

public class NioShortServerHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = LogUtil.getLogger(getClass());
	private NettyHttpServer nettyHttpServer;

	private Charset charset;

	private int msgLength = -1;

	private byte[] receivedBytes;

	boolean hasReceived = false;

	private ExecutorService bizThreadPool;

	public NioShortServerHandler(URL protocolUrl, ExecutorService bizThreadPool, NettyHttpServer nettyHttpServer) {
		this.nettyHttpServer = nettyHttpServer;
		this.bizThreadPool = bizThreadPool;
		this.charset = Charset.forName("UTF-8");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 已经接收到足够的数据了，不再处理
		if (hasReceived) {
			ReferenceCountUtil.release(msg);
			return;
		}
		
        if( msg instanceof ByteBuf){
        	// 读数据
        	ByteBuf buf = (ByteBuf) msg;
        	byte[] tempBytes = new byte[buf.readableBytes()];
        	buf.readBytes(tempBytes, 0, tempBytes.length);
        	logger.debug("收到数据，[{", tempBytes.length ,"}]字节，内容[{",new String(tempBytes, charset),"}]");
        	
        	// 缓存数据
        	if (receivedBytes == null) {
        		receivedBytes = tempBytes;
        	} else {
        		byte[] newBytes = new byte[receivedBytes.length + tempBytes.length];
        		System.arraycopy(receivedBytes, 0, newBytes, 0, receivedBytes.length);
        		System.arraycopy(tempBytes, 0, newBytes, receivedBytes.length, tempBytes.length);
        		receivedBytes = newBytes;
        	}
        	
        	ReferenceCountUtil.release(msg);
        	
        	// 如果已经收到足够数据了
    		hasReceived = true; // 置接收完成标识，后面即使有数据来了也不收了
    		ctx.pipeline().remove("readTimeOut"); // 收够了，移除超时处理
    		this.msgLength = receivedBytes.length;
    		// 获取报文体内容
    		byte[] msgBytes = new byte[msgLength];
    		System.arraycopy(receivedBytes, 0, msgBytes, 0, msgLength);
    		bizThreadPool.execute(new Task(ctx, msgBytes));
        }
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
		// 读取请求超时
		if (e instanceof ReadTimeoutException) {

		} else if (e instanceof WriteTimeoutException) {

		} else {

		}
		ctx.close();
	}

	private class Task implements Runnable {
		private ChannelHandlerContext ctx;
		private byte[] msgBytes;

		public Task(ChannelHandlerContext ctx, byte[] msgBytes) {
			this.ctx = ctx;
			this.msgBytes = msgBytes;
		}

		@Override
		public void run() {
			InputStream is = new ByteArrayInputStream(this.msgBytes);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
			String clientIP = "unknow";
			if( insocket != null ) {
				clientIP = insocket.getAddress().getHostAddress();
			}
			doDispatcher(is,os,clientIP);
			byte[] resultByteArray = os.toByteArray();
			
			ByteBuf byteMsg = UnpooledByteBufAllocator.DEFAULT.buffer();
			byteMsg.writeBytes(resultByteArray);
			ctx.writeAndFlush(byteMsg);
		}
	}


	private void doDispatcher( InputStream is ,OutputStream os,String ip ) {
		HttpResponse response = new HttpResponse();
		HttpRequest request = null;
		ServerContext.get().putContext("close", true);
		try {
			// 防止readLine阻塞
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

			List<Filter> filterList = this.nettyHttpServer.getFilters();

			boolean allPass = true;
			for (Filter filter : filterList) {
				boolean isContinue = filter.doFilter(request, response);
				if(!isContinue){ 
					allPass = false;
					break ;
				}
			}

			if( allPass ){
				this.nettyHttpServer.getHandler().handler(request, response);
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
			}
		}		
	}
}
