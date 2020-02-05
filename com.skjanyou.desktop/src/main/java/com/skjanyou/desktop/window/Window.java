package com.skjanyou.desktop.window;

import com.skjanyou.desktop.implant.Implant;

public interface Window {
	/** 设置宽 **/
	public Window setWidth( float width );
	/** 设置高 **/
	public Window setHeight( float height );
	/** 显示 **/
	public Window show();
	/** 隐藏 **/
	public Window hide();
	/** 设置定点坐标 **/
	public Window setLocation( float x,float y );
	/** 关闭窗口并释放资源 **/
	public Window destroy();
	/** 设置访问的Url **/
	public Window setUrl( String url );
	/** 设置渲染的html字符串 **/
	public Window setHtmlString( String htmlString );
	/** 执行js **/
	public Window executeJscriptWithoutReturn( String javaScript );
	/** 执行js获得返回 **/
	public Object executeJscript( String javaScript );
	/** 添加注入脚本 **/
	public Window addImplant( Implant... implants );
}