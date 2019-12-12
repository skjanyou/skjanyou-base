package com.skjanyou.server.impl.http;

import java.io.OutputStream;

import com.skjanyou.server.constant.StatusCode;
import com.skjanyou.server.inter.Headers;
import com.skjanyou.server.inter.Protocol;
import com.skjanyou.server.inter.Response;

public class HttpResponse implements Response {
	private Headers headers = new HttpHeaders();
	private ResponseBody responseBody = new HttpResponseBody();
	private ResponseLine responseLine = new HttpResponseLine();
	
	public static class HttpResponseBody implements ResponseBody {

		@Override
		public byte[] getBodyContent() {
			return "{\"code\":200,\"notice\":0,\"follow\":0,\"forward\":0,\"msg\":0,\"comment\":0,\"pushMsg\":null,\"friend\":{\"snsCount\":0,\"count\":0,\"celebrityCount\":0},\"lastPrivateMsg\":null,\"event\":0,\"newProgramCount\":0,\"createDJRadioCount\":0,\"newTheme\":true}".getBytes();
		}

		@Override
		public ResponseBody writeToStream(OutputStream os) {
			return null;
		}
	} 
	
	public static class HttpResponseLine implements ResponseLine {

		@Override
		public StatusCode statusCode() {
			return StatusCode.Ok;
		}

		@Override
		public Protocol protocol() {
			return new Protocol() {
				
				@Override
				public String version() {
					return "1.1";
				}
				
				@Override
				public String protocol() {
					return "HTTP";
				}
			};
		}
	}
	
	@Override
	public ResponseBody responseBody() {
		return this.responseBody;
	}

	@Override
	public ResponseLine responseLine() {
		return this.responseLine;
	}

	@Override
	public Headers headers() {
		return this.headers;
	}

}
