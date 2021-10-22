package com.skjanyou.vfs.impl.file;

import com.skjanyou.vfs.FileObject;
import com.skjanyou.vfs.FileSystemProvider;

/**
 * 常规的文件对象提供器
 * @author skjanyou
 * 时间 : 2021-10-21
 * 作用 :
 */
public class NormalFileSystemProvider implements FileSystemProvider {
    public static final String FILE_PROTOCOL = "file:";
    
	@Override
	public boolean isMatch(String resource) {
        return resource.toLowerCase().startsWith(FILE_PROTOCOL);
	}

	@Override
	public String getSchema() {
		return FILE_PROTOCOL;
	}

	@Override
	public FileObject resolver(String resource) {
		return null;
	}

}
