package com.skjanyou.server.api.bean;

import lombok.Data;

@Data
public class ServerConfig {
	private String ip;
	private int port;
	private long timeout;
}
