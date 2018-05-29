package com.skjanyou.vfs.impl.jar;

import java.io.File;

import com.skjanyou.vfs.FileObject;
import com.skjanyou.vfs.FileSystemProvider;

public class JarFileSystemProvider implements FileSystemProvider {
    public static final String JAR = ".jar";
    public static final String JAR_PROTOCOL = "jar:";
    
	@Override
	public boolean isMatch(String resource) {
        String lowerCase = resource.toLowerCase();
        //jar包比较特殊：无法简单根据resource判断，存在D:/lib/a.jar这种用法，和扩展协议oss://file1/a.jar无法从语法上区分。
        return lowerCase.startsWith(JAR_PROTOCOL) || (lowerCase.endsWith(JAR) && new File(resource).exists());
	}

	@Override
	public String getSchema() {
		return JAR_PROTOCOL;
	}

	@Override
	public FileObject resolver(String resource) {
		return null;
	}

}
