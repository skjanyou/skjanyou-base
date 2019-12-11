package com.skjanyou.server.impl.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.skjanyou.server.inter.Request;
import com.skjanyou.server.inter.Response;
import com.skjanyou.util.CommUtil;

/**
 * 接收线程
 * @author skjanyou
 * 时间 : 2019年12月9日
 * 作用 :
 */
public class AcceptThread extends Thread implements Runnable,Comparable<AcceptThread> {
	private Socket socket;
	private int priority;
	public AcceptThread( Socket socket ){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		if(socket == null){
			throw new NullPointerException("Socket套接字为空!");
		}
		
		Request request = createRequest();
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
            
            os = socket.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);
            
            bw.write("hellow");
            bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("读取数据出错!",e);
		} finally {

		}
		
	}
	
	protected Request createRequest(  ) {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
            StringBuilder sb = new StringBuilder();
            // 第一行,获取请求类型和uri
            String msg = br.readLine();		
            String[] parm = msg.split("\\s");
            String method = parm[0];
            String uri = parm[1];
            sb.append(msg).append("\r\n");
            while(( msg = br.readLine() ) != null && msg.length() > 0){
                sb.append(msg).append("\r\n");
            }
            //接收客户端的请求信息
            String requestInfo = sb.toString().trim();        
            System.out.println(requestInfo);			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("读取数据出错!",e);
		} finally {
			CommUtil.close(br);
			CommUtil.close(isr);
			CommUtil.close(is);			
		}

		
		return null;
	}
	
	protected Response createResponse(){
		return null;
	}
	
	@Override
	public int compareTo(AcceptThread o) {
		return 0;
	}		
}
