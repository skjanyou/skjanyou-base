package com.skjanyou.practise.hostproxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

public class TestSocketSocketImpl extends SocketImpl{
	private Delegator delegator;
	public TestSocketSocketImpl() {
		delegator = new Delegator(this, SocketImpl.class, "java.net.SocksSocketImpl");
	}
	
	private Socket getSocket() {
		try {
			final Field field = SocketImpl.class.getDeclaredField("socket");
			field.setAccessible(true);
			return (Socket) field.get(this);
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
	@Override
	public void setOption(int pOptID, Object pValue) throws SocketException {
		delegator.invoke("setOption", Integer.valueOf(pOptID), pValue);
	}

	@Override
	public Object getOption(int pOptID) throws SocketException {
		return delegator.invoke("getOption", Integer.valueOf(pOptID));
	}

	@Override
	protected void create(boolean stream) throws IOException {
		delegator.invoke("create", Boolean.valueOf(stream));
	}

	@Override
	protected void connect(String host, int port) throws IOException {
		System.out.println("host:" + host);
		delegator.invoke("connect", host, Integer.valueOf(port));
	}

	@Override
	protected void connect(InetAddress pAddress, int port) throws IOException {
		System.out.println("host:" + pAddress);
		delegator.invoke("connect", address, Integer.valueOf(port));
	}

	@Override
	protected void connect(SocketAddress pAddress, int pTimeout) throws IOException {
		System.out.println("host:" + pAddress);
		delegator.invoke("connect", pAddress, Integer.valueOf(pTimeout));
	}

	@Override
	protected void bind(InetAddress pHost, int pPort) throws IOException {
		delegator.invoke("bind", pHost, Integer.valueOf(pPort));
	}

	@Override
	protected void listen(int backlog) throws IOException {
		delegator.invoke("listen", Integer.valueOf("backlog"));
	}

	@Override
	protected void accept(SocketImpl s) throws IOException {
		delegator.invoke("accept", s);
	}

	@Override
	protected InputStream getInputStream() throws IOException {
		return delegator.invoke("getInputStream");
	}

	@Override
	protected OutputStream getOutputStream() throws IOException {
		return delegator.invoke("getOutputStream");
	}

	@Override
	protected int available() throws IOException {
		final Integer avail = delegator.invoke("available");
		return avail.intValue();
	}

	@Override
	protected void close() throws IOException {
		delegator.invoke("close");
	}

	@Override
	protected void sendUrgentData(int pData) throws IOException {
		delegator.invoke("sendUrgentData", Integer.valueOf(pData));
	}	
}
