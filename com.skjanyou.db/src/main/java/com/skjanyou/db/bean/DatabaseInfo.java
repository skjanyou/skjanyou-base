package com.skjanyou.db.bean;


public class DatabaseInfo {
	/** jdbc驱动 */
	private String className;
	/** 连接url */
	private String url;
	/** 登录用户 */
	private String user;
	/** 用户密码 */
	private String password;
	/** 连接数量 */
	private int size;
	/** 超时时间 */
	private long mills;
	public DatabaseInfo(String className, String url, String user,
			String password, int size, long mills) {
		super();
		this.className = className;
		this.url = url;
		this.user = user;
		this.password = password;
		this.size = size;
		this.mills = mills;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public long getMills() {
		return mills;
	}
	public void setMills(long mills) {
		this.mills = mills;
	}
	
}
