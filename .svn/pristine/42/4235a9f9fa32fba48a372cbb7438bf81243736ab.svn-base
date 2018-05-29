package com.skjanyou.vfs.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {
	private static final int DEFAULT_BUFFER_SIZE = 8192;

	public static void io(InputStream in, OutputStream out, boolean closeIn, boolean closeOut) throws IOException {
		int bufferSize = DEFAULT_BUFFER_SIZE;
		byte[] buffer = new byte[bufferSize];
		int amount;

		try {
			while ((amount = in.read(buffer)) >= 0) {
				out.write(buffer, 0, amount);
			}

			out.flush();
		} finally {
			if (closeIn) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}

			if (closeOut) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
