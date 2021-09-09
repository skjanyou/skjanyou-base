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
	/** 检验数据库链接是否有效的SQL **/
	private String validationSQL;
	/** 是否在获取链接前验证有效性 **/
	private Boolean testOnBorrow;	
	
	public DatabaseInfo(String className, String url, String user,
			String password, int size, long mills, String validationSQL,Boolean testOnBorrow) {
		super();
		this.className = className;
		this.url = url;
		this.user = user;
		this.password = password;
		this.size = size;
		this.mills = mills;
		this.validationSQL = validationSQL;
		this.testOnBorrow = testOnBorrow;
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
	public String getValidationSQL() {
		return validationSQL;
	}
	public void setValidationSQL(String validationSQL) {
		this.validationSQL = validationSQL;
	}
	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
}
