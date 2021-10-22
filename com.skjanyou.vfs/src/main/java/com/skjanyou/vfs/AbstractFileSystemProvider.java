package com.skjanyou.vfs;

public abstract class AbstractFileSystemProvider implements FileSystemProvider {
	protected String PROTOCOL = null;
	public AbstractFileSystemProvider() {
		
	}
	public AbstractFileSystemProvider( String protocol ){
		this.PROTOCOL = protocol;
	}
	
	@Override
	public boolean isMatch(String resource) {
        return resource.toLowerCase().startsWith(this.PROTOCOL);
	}

	@Override
	public String getSchema() {
		return this.PROTOCOL;
	}

}
