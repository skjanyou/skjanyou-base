package com.skjanyou.server.simplehttpserver.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.skjanyou.util.CommUtil;

public class SocketUtil {
	private static String localhost = "127.0.0.1";
	public static boolean checkPortIsBind( int port ){
		boolean result = false;
		Socket socket = null;
		try {
			socket = new Socket(localhost, port);
		} catch (IOException e) {
			result = true;
		} finally {
			CommUtil.close(socket);
		}
		return result;
	}


	public static byte[] sendMessage( String ip, int port, byte[] sendDatas ) throws IOException{
		byte[] revDatas = null;
		Socket socket = null;
		InputStream input = null;
		OutputStream output = null;
		ByteArrayOutputStream baos = null;
		int len = -1; byte[] buff = new byte[ 4 * 1024];
		try {
			socket = new Socket(ip,port);
			output = socket.getOutputStream();
			input = socket.getInputStream();
			baos = new ByteArrayOutputStream();
			// ����������������
			output.write(sendDatas);
			output.flush();
			
			// ���շ�������������
			while( ( len = input.read(buff) ) != -1 ){
				baos.write(buff, 0, len);
			}
			
			baos.flush();
			// ���յ�������
			revDatas = baos.toByteArray();
			
		} catch (IOException e) {
			throw e;
		} finally {
			CommUtil.close(baos);
			CommUtil.close(output);
			CommUtil.close(input);
			CommUtil.close(socket);
		}

		return revDatas;
	}
	
	public static String inputStreamToString( InputStream inputStream ) throws IOException {
		byte[] buff = inputStreamToDataByte(inputStream);
		return new String(buff);
	}
	
	public static byte[] inputStreamToDataByte( InputStream inputStream ) throws IOException {
		int count = 0;
		// 1.先让流完成第一次准备
		while( count == 0 ) {
			count = inputStream.available();
		}
		
		byte[] bt = new byte[0];
		do {
			int readCount = 0;
			byte[] buff = new byte[count];
			while( readCount < count ) {
				readCount = inputStream.read(buff, 0, count - readCount);
			}
			// 暂存原数组,扩容后再写入
			byte[] tempArr = bt;
			bt = new byte[ bt.length + buff.length ];
			System.arraycopy(tempArr, 0, bt, 0, tempArr.length);
			System.arraycopy(buff, 0, bt, tempArr.length, buff.length);
			try {
				// 睡眠几毫秒等待客户端写入数据
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while( ( count = inputStream.available() ) != 0 );
		return bt;
	}
	
	
	
	public static void decodeDataFrame( byte[] e ) {
		int i = 0;
		int j; 
		int FIN = e[i] >> 7;
		int OPCODE = e[i++] & 15;
		int MASK = e[i] >> 7;
		int PAYLOADLENGTH = e[i++] & 0x7f;
		int length = 0;
		byte[] maskingKey = new byte[] {e[i++],e[i++],e[i++],e[i++]};
		byte[] s = new byte[PAYLOADLENGTH];
	
		
		//处理特殊长度126和127
		if( PAYLOADLENGTH == 126 ) {
			length = (e[i++] << 8) + e[i++];
		}
		if( PAYLOADLENGTH == 127 ) {
			length = (e[i++] << 24) + (e[i++] << 16)+(e[i++] << 8) + e[i++];
		}
		
		if( MASK != 0) {
			for( j=0; j < PAYLOADLENGTH;j++ ) {
				int t = e[i+j] ^ maskingKey[j%4];
				s[j] = (byte) t;
			}
		}else {
			System.arraycopy(s, 0, e, 0, PAYLOADLENGTH);
		}
			
		String result = new String(s);
		System.out.println(result);
	}
}	
