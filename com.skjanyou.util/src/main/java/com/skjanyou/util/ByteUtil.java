package com.skjanyou.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteUtil {
	public static byte[] convertToBytes( InputStream is ,boolean isClose ){ 
		if( is == null ){
			throw new NullPointerException("输入流不能为空!");
		}
		byte[] result = null;
		
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			int len = -1; byte[] buff = new byte[ 4096 ];
			while( ( len = is.read(buff) ) != -1 ){
				baos.write(buff, 0, len);
			}
			baos.flush();
			result = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if( isClose ){
				CommUtil.close(is);
			}
		}
		
		return result;
	}
	
    public static byte[] byteMerger( byte[] byteArray1, byte[] byteArray2 ){  
        byte[] byteArray3 = new byte[ byteArray1.length + byteArray2.length];  
        System.arraycopy(byteArray1, 0, byteArray3, 0, byteArray1.length);  
        System.arraycopy(byteArray2, 0, byteArray3, byteArray1.length, byteArray2.length);  
        return byteArray3;  
    }	
}
