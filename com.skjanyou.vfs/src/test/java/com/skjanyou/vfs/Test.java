package com.skjanyou.vfs;

import junit.framework.TestCase;


public class Test extends TestCase {

	public void testJarFileSystem(){

	}
	
	public static void main(String[] args) {
		String path = "D:/工作空间/MvnWorkspace/AnimeWorkspace/com.skjanyou.desktop.desktop/plugins/com.skjanyou.anime.spider-tencent-1.0.0.0_Alpha.jar";
		FileObject file = VFS.get(path);
		System.out.println(file);
		
		 
	}
}
