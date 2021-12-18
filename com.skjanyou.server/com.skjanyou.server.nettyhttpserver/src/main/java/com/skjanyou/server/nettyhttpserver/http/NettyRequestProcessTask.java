package com.skjanyou.server.nettyhttpserver.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.server.api.bean.HttpEntity;
import com.skjanyou.server.api.constant.StatusCode;
import com.skjanyou.server.api.inter.FilterHandler;
import com.skjanyou.server.core.HttpRequest;
import com.skjanyou.server.core.HttpResponse;
import com.skjanyou.server.core.RequestBuilder;
import com.skjanyou.server.core.ServerContext;
import com.skjanyou.util.CommUtil;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class NettyRequestProcessTask implements Runnable {
	private Logger logger = LogUtil.getLogger(NettyRequestProcessTask.class);
	private ChannelHandlerContext ctx;
	private byte[] msgBytes;
	private NettyHttpServer nettyHttpServer;

	public NettyRequestProcessTask( NettyHttpServer nettyHttpServer, ChannelHandlerContext ctx, byte[] msgBytes ) {
		this.nettyHttpServer = nettyHttpServer;
		this.ctx = ctx;
		this.msgBytes = msgBytes;
	}

	@Override
	public void run() {
		InputStream is = new ByteArrayInputStream(this.msgBytes);
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String clientIP = "unknow";
		if( insocket != null ) {
			clientIP = insocket.getAddress().getHostAddress();
		}
		HttpResponse response = doDispatcher(is,clientIP);
		StatusCode code = response.getHttpResponseLine().statusCode();
		byte[] body = response.getHttpResponseBody() == null ? new byte[0] : 
			response.getHttpResponseBody().getBodyContent() == null ? new byte[0] : response.getHttpResponseBody().getBodyContent();
			HttpResponseStatus status = code == StatusCode.Ok ? HttpResponseStatus.OK : HttpResponseStatus.INTERNAL_SERVER_ERROR;

			FullHttpResponse fullResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(body));

			ctx.writeAndFlush(fullResponse).addListener(ChannelFutureListener.CLOSE);
	}


	private HttpResponse doDispatcher( InputStream is , String ip ) {
		HttpResponse response = new HttpResponse();
		response.getHttpResponseLine().setStatusCode(StatusCode.Ok);
		HttpRequest request = null;
		ServerContext.get().putContext("close", true);
		try {
			// 构建请求对象
			request = RequestBuilder.solve(is);
			// 保存包装流
			ServerContext.get().putContext("reader", is);
			// 添加IP地址
			request.headers().put("remote-ip", ip);

			FilterHandler filterHandler = this.nettyHttpServer.getFilterHandler();
			HttpEntity httpEntity = new HttpEntity(request,response);
			filterHandler.doChainProcess(httpEntity);
			this.nettyHttpServer.getHandler().handler(request, response);

			return response;
		} catch( SocketTimeoutException ste ) {
			// 超时问题,不做处理
		} catch (Exception e) {
			logger.error("请求解析出错",e);
			if( response.getHttpResponseLine().statusCode() == null ) {
				response.getHttpResponseLine().setStatusCode(StatusCode.Error);
			}
		} finally {
			if( (boolean) ServerContext.get().getContext("close") ) {
				CommUtil.close(is);			
			}
		}		
		return response;
	}



}
