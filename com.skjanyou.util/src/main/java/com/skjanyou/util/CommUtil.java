package com.skjanyou.util;

import java.io.Closeable;
import java.io.IOException;

public class CommUtil {
	public static void close (Closeable entity){
		if(entity != null){
			try {
				entity.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
