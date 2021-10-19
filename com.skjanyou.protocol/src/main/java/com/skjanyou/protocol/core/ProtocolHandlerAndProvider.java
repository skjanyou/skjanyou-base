package com.skjanyou.protocol.core;

public abstract class ProtocolHandlerAndProvider implements ProtocolHandler, ProtocolProvider {
	@Override
	public ProtocolHandler getHandler() {
		return this;
	}
}
