package com.skjanyou.server.api.bean;

import com.skjanyou.server.api.inter.Request;
import com.skjanyou.server.api.inter.Response;

public class HttpEntity {
	private Response response;
	private Request request;
	
	public HttpEntity( Request request,Response response ) {
		super();
		this.request = request;
		this.response = response;
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
}
