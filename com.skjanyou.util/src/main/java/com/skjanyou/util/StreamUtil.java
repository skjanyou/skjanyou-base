package com.skjanyou.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class StreamUtil {
	/** 输入流转字符串 **/
	public static String convertToString( InputStream inputstream, String charset, boolean isClose )
	throws IOException{
		String result = null;
		
		if( inputstream == null ){
			throw new NullPointerException("输入流不能为空!");
		}

		ByteArrayOutputStream baos = null;
		try{
			baos = new ByteArrayOutputStream(inputstream.available());
			int len = -1;
			byte[] buff = new byte[ 4*1024 ];
			
			while( (len = inputstream.read(buff)) != -1 ){
				baos.write(buff, 0, len);
			}
			baos.flush();
			
			result = baos.toString(charset);
		} catch( IOException e){
			throw e;
		} finally {
			CommUtil.close(baos);
			if(isClose){
				CommUtil.close(inputstream);
			}
		}
		
		return result;
	}
	
	/** 获得文件流 **/
	public static InputStream getFileInputStream( File file ) throws FileNotFoundException{
		if( file == null ){ throw new NullPointerException("文件为空!");}
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw e;
		}
		return is;
	}
	
	public static InputStream getInputStreamIgnoreLocation( String file ) throws FileNotFoundException{
		InputStream is = null;
		if( file.startsWith("classpath:") ){
			String resources = file.substring(10);
//			if( !resources.startsWith("/")){ resources = "/" + resources; }
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resources);
		}else{
			try{
				is = new FileInputStream(file);
			} catch ( FileNotFoundException e ){
				throw e;
			}
		}	
		return is;
	}
	
}
