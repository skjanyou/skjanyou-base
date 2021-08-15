package com.skjanyou.mvc.handler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.skjanyou.mvc.anno.Mvc.HttpParameter;
import com.skjanyou.mvc.anno.Mvc.HttpPostReuqestBody;
import com.skjanyou.mvc.anno.Mvc.HttpRequestHeader;
import com.skjanyou.mvc.anno.Mvc.HttpResponseHeader;
import com.skjanyou.mvc.bean.Context;
import com.skjanyou.server.api.exception.ServerException;
import com.skjanyou.server.core.HttpHeaders;
import com.skjanyou.server.core.HttpRequest;
import com.skjanyou.server.core.HttpRequest.HttpRequestLine;
import com.skjanyou.server.core.HttpResponse;
import com.skjanyou.util.StringUtil;
import com.skjanyou.util.convert.ConvertUtil;

public class AutowireMvcHandler extends MvcHandler {

	public AutowireMvcHandler(String scanPath) {
		super(scanPath);
	}

	protected Object[] doGetParameter( Context context,HttpRequest request,HttpResponse response )  {
		Method method = context.getTargetMethod();
		// 请求行
		HttpRequestLine requestLine = request.getHttpRequestLine();
		// 请求头
		HttpHeaders httpRequestHeaders = request.getHttpHeaders();
		// 响应头
		HttpHeaders httpResponseHeaders = response.getHttpHeaders();
		// 查询参数
		Map<String,Object> params = request.requestLine().queryParams();
		// POST请求参数正文,不一定有值,后续要通过是否为POST判断
		Map<String,Object> requestBodyMap = request.getHttpRequestbody().getRequestBodyMap();		
		// 填充请求参数
		List<Object> linkList = new LinkedList<>();
		Parameter[] parameters = method.getParameters();
		for (Parameter parameter : parameters) {
			// 参数为HttpRequest 
			if( HttpRequest.class.isAssignableFrom(parameter.getType()) ){
				linkList.add(request);
				continue;
			}
			// 参数为HttpResponse
			if( HttpResponse.class.isAssignableFrom(parameter.getType()) ){
				linkList.add(response);
				continue;
			}
			
			// Post请求的正文内容
			HttpPostReuqestBody httpPostReuqestBody = parameter.getAnnotation(HttpPostReuqestBody.class);
			if( httpPostReuqestBody != null ){
				// 非POST请求
				if( !"POST".equalsIgnoreCase(requestLine.method()) ){
					throw new ServerException("@HttpPostReuqestBody注解必须为POST请求");
				}
				
				// 为空的时候,说明该参数使用整个Map作为传参,此时要判断类型是否一致
				if( StringUtil.isBlank(httpPostReuqestBody.value()) ){
					if( Map.class.isAssignableFrom(parameter.getType()) ){
						Type type = parameter.getParameterizedType();
						
						ParameterizedType p = (ParameterizedType)type;
						Class<?> c1 = (Class<?>) p.getActualTypeArguments()[0];  
						Class<?> c2 = (Class<?>) p.getActualTypeArguments()[1];
						if( String.class == c1 && Object.class == c2 ){
							linkList.add(requestBodyMap);
						}else{
							throw new ServerException("httpPostReuqestBody未指定key时,类型应为Map<String,Object>");
						}
					}
				}else{
					// 不为空时,说明为检索的key
					String key = httpPostReuqestBody.value();
					Object value = requestBodyMap.get(key);
					if( value != null ){
						value = ConvertUtil.convert(value, parameter.getType());
					}else{
						// 必输的情况下,获取到为空时报错
						if( httpPostReuqestBody.required() ){
							throw new ServerException("POST请求正文内容[" + key + "]未传");
						}
					}
					linkList.add(value);
				}
				continue;
			}
			
			// 查询参数
			HttpParameter httpParameter = parameter.getAnnotation(HttpParameter.class);
			if( httpParameter != null ){
				String p = httpParameter.value();
				Object pObject = params.get(p);
				if( pObject == null && httpParameter.required() ){
					throw new ServerException("请求参数[" + p + "]未传");
				}
				if( pObject != null ){
					pObject = ConvertUtil.convert(pObject, parameter.getType());
				}
				linkList.add(pObject);
				continue;
			}
			
			// 请求头信息
			HttpRequestHeader httpRequestHeader = parameter.getAnnotation(HttpRequestHeader.class);
			if( httpRequestHeader != null ) {
				String headerName = httpRequestHeader.value();
				// 如果没有传值,则直接拿所有的http头
				if( StringUtil.isBlank(headerName) ) {
					if( !HttpHeaders.class.isAssignableFrom(parameter.getType()) ) {
						throw new ServerException("使用@HttpRequestHeader注解,参数类型类型必须为HttpHeaders");
					}					
					linkList.add(httpRequestHeaders);
					continue;
				}else{
					String headerValue = httpRequestHeaders.get(headerName);
					if( httpRequestHeader.required() && StringUtil.isBlank(headerValue) ) {
						throw new ServerException("请求头缺少参数[" + httpRequestHeader.value() + "].");
					}
					linkList.add(headerValue);
					continue;
				}
			}
			
			// 响应头信息
			HttpResponseHeader httpResponseHeader = parameter.getAnnotation(HttpResponseHeader.class);
			if( httpResponseHeader != null ) {
				// 如果没有传值,则直接拿所有的http头
				if( !HttpHeaders.class.isAssignableFrom(parameter.getType()) ) {
					throw new ServerException("使用@HttpResponseHander注解,参数类型类型必须为HttpHeaders");
				}
				linkList.add(httpResponseHeaders);
				continue;
			}			
			
			linkList.add(null);
		}
		
		return linkList.toArray(new Object[]{});
	}
	
}
