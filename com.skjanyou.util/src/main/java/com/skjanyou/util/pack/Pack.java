package com.skjanyou.util.pack;

import java.io.File;
import java.io.Serializable;

public class Pack implements Serializable{
	private static final long serialVersionUID = -3842200059878543589L;
	private File packFile;
	private File[] srcFiles;
	private String name;
	private String author;
	private String jdkVersion;
	public File getPackFile() {
		return packFile;
	}
	public void setPackFile(File packFile) {
		this.packFile = packFile;
	}
	public File[] getSrcFiles() {
		return srcFiles;
	}
	public void setSrcFiles(File[] srcFiles) {
		this.srcFiles = srcFiles;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getJdkVersion() {
		return jdkVersion;
	}
	public void setJdkVersion(String jdkVersion) {
		this.jdkVersion = jdkVersion;
	}
	
}
