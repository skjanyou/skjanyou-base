package com.skjanyou.server.simplehttpserver.http;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.skjanyou.server.api.constant.ServerConst;
import com.skjanyou.server.api.inter.Headers;
import com.skjanyou.util.StringUtil;

public class HttpHeaders implements Headers {
	private Map<String,String> header = new HashMap<>();
	
	@Override
	public String get(String key) {
		return header.get(key);
	}

	@Override
	public Headers put(String key, String value) {
		header.put(key, value);
		return this;
	}

	@Override
	public Headers remove(String key) {
		header.remove(key);
		return this;
	}

	@Override
	public int size() {
		return header.size();
	}

	@Override
	public String toHttpHeaderString() {
		String result = "";
		Iterator<Entry<String, String>> it = this.header.entrySet().iterator();
		while( it.hasNext() ){
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			String header = MessageFormat.format("{0} : {1}{2}", key, value, ServerConst.CRLF );
			result += header;
		}
		
		return result;
	}
	
	public Headers converToHeaders( String headers ) {
		// 1.判空
		if( headers == null ){throw new NullPointerException("头部信息不能为空!");}
		// 2. 拆分成单行
		String[] lineHeaders = headers.split(ServerConst.CRLF);
		// 3.读取单行数据至map中
		for( String lineHeader : lineHeaders ){
			String[] headerArr = lineHeader.split(":");
			String key = null;
			String value = null;
			if( headerArr.length == 2 ){
				this.header.put(StringUtil.trim(headerArr[0]), headerArr[1]);
			}else if( headerArr.length > 2 ){
				int idx = lineHeader.indexOf(":");
				key = lineHeader.substring(0, idx);
				value = lineHeader.substring(idx + 1);
			}else{
				continue;
			}
			this.header.put(StringUtil.trim(key), StringUtil.trim(value));
		}
		
		return this;
	}
}
