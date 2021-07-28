package com.skjanyou.server.niohttpserver.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class NioTest {
	private static int port = 6542;
	public static void main(String[] args) throws Exception {
		ServerSocketChannel acceptorSvr = ServerSocketChannel.open();
		acceptorSvr.socket().bind(new InetSocketAddress(port));
		
		acceptorSvr.configureBlocking(false);
		
		Selector selector = Selector.open();
		new Thread(new ReactorTask(acceptorSvr,selector)).start();
		
		SocketChannel channel = acceptorSvr.accept();
		channel.configureBlocking(false);
		channel.socket().setReuseAddress(true);
		
		channel.register(selector, SelectionKey.OP_READ, new Object());
	}

	
	public static class ReactorTask implements Runnable {
		private ServerSocketChannel acceptorSvr;
		private Selector selector;
		private SelectionKey key;
		public ReactorTask( ServerSocketChannel acceptorSvr,Selector selector ) throws Exception {
			this.acceptorSvr = acceptorSvr;
			this.selector = selector;
			this.key = this.acceptorSvr.register(this.selector, SelectionKey.OP_ACCEPT, new Object()); 
		}
		
		
		@Override
		public void run() {
			try {
				int num = this.selector.select();
				
				Set<SelectionKey> selectKeys = this.selector.selectedKeys();
				for (SelectionKey selectionKey : selectKeys) {
					SelectableChannel channel = selectionKey.channel();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
