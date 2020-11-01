package com.skjanyou.javafx.component;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.skjanyou.util.StreamUtil;

public class JavaFxTray {
	private static final boolean isSupport = SystemTray.isSupported();
	private static JavaFxTray instance = new JavaFxTray();
	private int width;
	private int height;
	private Image image;
	
	private JavaFxTray() {}
	
	public static boolean isSupport() {
		return isSupport;
	}
	
	public static JavaFxTray getTray() {
		return instance;
	}
	
	public JavaFxTray setSize( int w, int h ) {
		this.width = w;
		this.height = h;
		return this;
	}
	
	public JavaFxTray setIcon( String image ) {
		try {
			this.image = ImageIO.read(StreamUtil.getInputStreamIgnoreLocation(image));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	
	public JavaFxTray mount() {
		SystemTray st = SystemTray.getSystemTray();
		TrayIcon ti = new TrayIcon(image);
		try {
			st.add(ti);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		
		ti.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(e);
			}
		});
		return this;
	}
	
	public static void main(String[] args) {
		JavaFxTray.getTray().setSize(400, 400).setIcon("D:/1.jpg").mount();
	}
}
