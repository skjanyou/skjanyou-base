package com.skjanyou.db.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
}
