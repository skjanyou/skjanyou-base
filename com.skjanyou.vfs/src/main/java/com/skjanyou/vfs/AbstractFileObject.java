package com.skjanyou.vfs;

import java.io.File;
import java.io.IOException;

public abstract class AbstractFileObject implements FileObject {
	private static final long serialVersionUID = 252120099940024501L;



	@Override
	public File toFile() throws IOException {
		try {
			return new File(getURL().toURI());
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public FileObject findChildByName(String fileName) {
		return getChildren().stream().filter(fileObject->{
			if( fileObject.getFileName().equals(fileName) ) {
				return true;
			}
			return false;
		}).findFirst().get();
	}

	@Override
	public FileObject findChildByPath(String path) {
		return getChildren().stream().filter(fileObject->{
			if( fileObject.getAbsolutePath().equals(path) ) {
				return true;
			}
			return false;
		}).findFirst().get();
	}

}
