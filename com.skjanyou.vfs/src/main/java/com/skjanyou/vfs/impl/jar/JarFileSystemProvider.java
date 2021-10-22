package com.skjanyou.vfs.impl.jar;

import java.io.File;

import com.skjanyou.vfs.AbstractFileSystemProvider;
import com.skjanyou.vfs.FileObject;

public class JarFileSystemProvider extends AbstractFileSystemProvider {
	public static final String JAR = ".jar";
	public static final String JAR_PROTOCOL = "jar:";
	
    public JarFileSystemProvider() {
		super(JAR_PROTOCOL);
	}
    
	@Override
	public boolean isMatch(String resource) {
        String lowerCase = resource.toLowerCase();
        // jar包有两种:  1. jar:xxxxx.jar或者jar://xxx.jar 这种通过前缀判断  2.D:/xx/xx.jar 这种通过后缀判断
        return super.isMatch(resource) || (lowerCase.endsWith(JAR) && new File(resource).exists());
	}

	@Override
	public String getSchema() {
		return JAR_PROTOCOL;
	}

	@Override
	public FileObject resolver(String resource) {
		return new JarFileObject(resource);
	}

}
