package com.skjanyou.mvc.handler;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.List;

import com.skjanyou.annotation.api.Web.ResponseBody;
import com.skjanyou.annotation.api.enumclass.ResponseType;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.mvc.anno.Mvc.HandlerException.ExceptionHandler;
import com.skjanyou.mvc.bean.Context;
import com.skjanyou.server.api.constant.StatusCode;
import com.skjanyou.server.api.exception.ServerException;
import com.skjanyou.server.core.HttpHeaders;
import com.skjanyou.server.core.HttpProtocolLv1;
import com.skjanyou.server.core.HttpRequest;
import com.skjanyou.server.core.HttpResponse;
import com.skjanyou.server.core.HttpResponse.HttpResponseBody;
import com.skjanyou.server.core.HttpResponse.HttpResponseLine;
import com.skjanyou.util.convert.ConvertUtil;

public abstract class MvcHandler extends MappingHandler {
	private Logger logger = LogUtil.getLogger(MvcHandler.class);
	protected static final ResponseType defaultContentType = ResponseType.JSON;
	public MvcHandler(String scanPath) {
		super(scanPath);
	}
	
	@Override
	public void handlerException( Exception e ,HttpRequest request, HttpResponse response ) {
		logger.error(e);
		// 响应行
		response.getHttpResponseLine().setStatusCode(StatusCode.Error);
		// 响应体
		response.getHttpResponseBody().setBodyContent("服务器内部错误");
		// 响应头
		response.getHttpHeaders().put("Content-Type", ResponseType.HTML.getValue());
	}
	
	@Override
	public void handler(HttpRequest request, HttpResponse response) throws ServerException{
		// 请求url
		String url = request.requestLine().url().split("\\?")[0]; 
		// HTTP协议版本
		response.getHttpResponseLine().setProtocol(new HttpProtocolLv1());
		
		Context context = mappings.get(url);
		if( context != null ){
			doHandler( context,request,response );
		}else{
			doNotFound(context, request, response);
		}
	}	
	
	// 处理请求
	@SuppressWarnings("unchecked")
	protected void doHandler( Context context,HttpRequest request,HttpResponse response ) throws ServerException {
		Method method = context.getTargetMethod();
		Object object = context.getTargetObj();
		ExceptionHandler handler = context.getHandler();
		List<Class<? extends Exception>> classList = context.getExceptionList();
		// 响应行
		HttpResponseLine responseLine = response.getHttpResponseLine();
		// 响应体
		HttpResponseBody responseBody = response.getHttpResponseBody();
		// 响应头
		HttpHeaders httpHeaders = response.getHttpHeaders();
		
		Object result = "";
		String contentType = defaultContentType.getValue();
		Object[] paras = doGetParameter(context, request, response);
		try {
			// 调用方法
			logger.debug("invoke method{" + method + "},argus{" + paras + "}");
			result = method.invoke(object,paras);
		} catch (Exception e) {
			logger.error(e);
			if( handler == null || ( classList != null && !classList.contains(e.getClass())) ) {
				throw new ServerException("方法调用失败:" + method,e);
			}
			result = handler.handler(e.getCause(), object, method, paras);
		}
		if( result != null ){
			ResponseBody rb = method.getAnnotation(ResponseBody.class);
			if( rb != null ){
				contentType = rb.type().getValue();
			}
		}
		
		logger.debug("return result :[ " + result + " ]");
		httpHeaders.put("Content-Type", contentType);
		responseLine.setStatusCode(StatusCode.Ok);
		if( result instanceof File ) {
			httpHeaders.put("Content-Type", "application/octet-stream");
			File resultFile = (File) result;
			String fileName = resultFile.getName();
			try {
				byte[] bytes = fileName.getBytes();
				fileName = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码  
				httpHeaders.put("Content-disposition", String.format("attachment; filename=\"%s\"", fileName)); // 文件名外的双引号处理firefox的空格截断问题
			} catch (UnsupportedEncodingException e) {
			} 
			
			responseBody.setBodyContent( resultFile );
		}else if( result instanceof byte[] ) {
			httpHeaders.put("Content-Type", "application/octet-stream");
			responseBody.setBodyContent((byte[]) result );
		}else {
			result = ConvertUtil.convert(result, String.class);
			responseBody.setBodyContent(result.toString());		
		}
	}
	
	// 找不到的情况
	protected void doNotFound( Context context,HttpRequest request,HttpResponse response ) throws ServerException {
		response.getHttpResponseLine().setStatusCode(StatusCode.Not_Found);
		response.getHttpResponseBody().setBodyContent("<html><head><title>找不到页面</title></head><body><pre>找不到页面</pre></body></html>");
		response.getHttpHeaders().put("Content-Type", ResponseType.HTML.getValue());
	}

	protected abstract Object[] doGetParameter( Context context,HttpRequest request,HttpResponse response )  ;
	
	
}
