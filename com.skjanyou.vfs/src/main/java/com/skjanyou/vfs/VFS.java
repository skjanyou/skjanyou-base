package com.skjanyou.vfs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.skjanyou.vfs.impl.jar.JarFileSystemProvider;

public final class VFS {
	private static Map<String,FileSystemProvider> providers = new HashMap<String,FileSystemProvider>();
	private static VFS vfs = new VFS();
	
	private VFS(){
		addFileSystemProvider(new JarFileSystemProvider());
	}
	
	public static VFS addFileSystemProvider(FileSystemProvider fsp){
		System.out.println(fsp);
		System.out.println(providers);
		System.out.println(fsp.getSchema());
		providers.put(fsp.getSchema(), fsp);
		return vfs;
	}
	
	public static FileObject get(String path){
		FileObject result = null;
		Collection<FileSystemProvider> ps  = providers.values();
		for(FileSystemProvider fsp : ps){
			if(fsp.isMatch(path)){
				result = fsp.resolver(path);
				break;
			}
		}
		
		return result;
	}
}
