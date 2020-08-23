package com.skjanyou.util.pack;

import java.io.File;
import java.io.FileInputStream;

import com.skjanyou.util.StreamUtil;

public class PackUtil {
	
	public static void encode( Pack pack,boolean autoCloseStream ) throws Exception {
		File packFile = pack.getPackFile();
		
		// 1.读取文件
		File[] files = pack.getSrcFiles();
		byte[] totalByteArr = new byte[0];
		
		// 2.计算文件段
		byte[] fileArr = null; int totalLen = 0; int fileLen = 0;
		for (File file : files) {
			fileArr = StreamUtil.getDataByStream(new FileInputStream(file), true);
			totalLen = totalByteArr.length; 
			fileLen = fileArr.length;
			totalByteArr = new byte[ totalLen + fileLen ];
			System.arraycopy(fileArr, 0, totalByteArr, totalLen, fileArr.length);
		}
		
		// 3.生成数据描述段
		
	};
	
	public static void decode() throws Exception {
		
	};
}
