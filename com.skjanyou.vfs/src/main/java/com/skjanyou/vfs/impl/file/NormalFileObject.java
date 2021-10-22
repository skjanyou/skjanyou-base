package com.skjanyou.vfs.impl.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.skjanyou.vfs.AbstractFileObject;
import com.skjanyou.vfs.FileObject;

public class NormalFileObject extends AbstractFileObject {
	private static final long serialVersionUID = -2145975206961929183L;
    private List<FileObject> children;
    private File file = null;

    public NormalFileObject( String resource ) {
    	this(new File(resource));
    }
    
    public NormalFileObject( File file ) {
    	this.file = file;
    }

	@Override
	public URL getURL() throws MalformedURLException {
		return this.file.toURL();
	}

	@Override
	public String getAbsolutePath() {
		return this.file.getAbsolutePath();
	}

	@Override
	public String getPath() {
		return this.file.getPath();
	}

	@Override
	public String getFileName() {
		return this.file.getName();
	}

	@Override
	public String getExtName() {
		return this.file.getName().substring(this.file.getName().lastIndexOf("\\."));
	}

	@Override
	public boolean isFolder() {
		return this.file.isDirectory();
	}

	@Override
	public boolean isInPackage() {
		return false;
	}

	@Override
	public boolean isExist() {
		return this.file.exists();
	}

	@Override
	public long getLastModifiedTime() {
		return this.file.lastModified();
	}

	@Override
	public long getSize() {
		return this.file.getTotalSpace();
	}

	@Override
	public InputStream getInputStream() {
		try {
			return new FileInputStream(this.file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public OutputStream getOutputStream() {
		try {
			return new FileOutputStream(this.file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public FileObject getParent() {
		return new NormalFileObject(this.file.getParent());
	}

	@Override
	public void setParent(FileObject fileObject) {
		
	}

	@Override
	public List<FileObject> getChildren() {
		if( children != null && children.size() != 0 ) {
			return children;
		}
		List<File> subFileList = Arrays.asList(this.file.listFiles());
		children = subFileList.stream().map(file->{
			if( file !=null && file.exists() ) {
				return new NormalFileObject(file);
			}
			return null;
		}).filter(file->file != null).collect(Collectors.toList());
		
		return children;
	}



}
