package com.skjanyou.javafx.util;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 拉伸工具类
 * @author Light
 */
public class DragUtil {
    enum  DIRECTION {
    	NONE,TOP,RIGHT,BOTTOM,LEFT,TOP_RIGHT,RIGHT_BOTTOM,LEFT_BOTTOM,TOP_LEFT
    } 
    private  DIRECTION direction = DIRECTION.NONE;
    //窗体拉伸属性
    private final int RESIZE_WIDTH = 8;// 判定是否为调整窗口状态的范围与边界距离
    private double MIN_WIDTH = 300;// 窗口最小宽度
    private double MIN_HEIGHT = 250;// 窗口最小高度
    public DragUtil() {}
    
    public  void addDrawFunc(Stage stage,Pane root,double appMinWidth,double appMinHeight) {
    	if( MIN_WIDTH <= appMinWidth) {
    		MIN_WIDTH = appMinWidth;
    	}
    	if( MIN_HEIGHT <= appMinHeight ) {
    		MIN_HEIGHT = appMinHeight;
    	}
    	
    	// 拖拽窗口改变大小
        stage.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
			    // 保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
	            double x = event.getSceneX();
	            double y = event.getSceneY();
                double nextX = stage.getX();
                double nextY = stage.getY();
                double nextWidth = stage.getWidth();
                double nextHeight = stage.getHeight();
                
                if (direction == DIRECTION.RIGHT || direction == DIRECTION.RIGHT_BOTTOM) {// 所有右边调整窗口状态
                    nextWidth = x;
                }
                if (direction == DIRECTION.BOTTOM || direction == DIRECTION.RIGHT_BOTTOM || direction == DIRECTION.LEFT_BOTTOM) {// 所有下边调整窗口状态
                    nextHeight = y;
                }
                if(direction == DIRECTION.LEFT || direction == DIRECTION.LEFT_BOTTOM ) {
                	nextWidth -= x;
                	nextX += x;
                }
                // 顶部拉伸与拖拽事件有冲突，不开放顶部拉伸功能
//                if(direction == DIRECTION.TOP || direction == DIRECTION.TOP_LEFT || direction == DIRECTION.TOP_RIGHT) {
//                	nextHeight -= y;
//                	nextY += y;
//                }
                
                if (nextWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
                    nextWidth = MIN_WIDTH;
                    nextX = stage.getX();
                }
                if (nextHeight <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
                    nextHeight = MIN_HEIGHT;
                    nextY = stage.getY();
                }
                // 最后统一改变窗口的x、y坐标和宽度、高度，可以防止刷新频繁出现的屏闪情况
                stage.setX(nextX);
                stage.setY(nextY);
                stage.setWidth(nextWidth);
                stage.setHeight(nextHeight);	                
			}
        });
        
        stage.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
	            double x = event.getSceneX();
	            double y = event.getSceneY();
	            double width = stage.getWidth();
	            double height = stage.getHeight();
	            Cursor cursorType = Cursor.DEFAULT;// 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型
	            // 先将所有调整窗口状态重置
	            direction = DIRECTION.NONE;
	            if (y >= height - RESIZE_WIDTH) {
	                if (x <= RESIZE_WIDTH) {// 左下角调整窗口状态
	                	direction = DIRECTION.LEFT_BOTTOM;
	                	cursorType = Cursor.SW_RESIZE;
	                } else if (x >= width - RESIZE_WIDTH) {// 右下角调整窗口状态
	                	direction = DIRECTION.RIGHT_BOTTOM;
	                    cursorType = Cursor.SE_RESIZE;
	                } else {// 下边界调整窗口状态
	                	direction = DIRECTION.BOTTOM;
	                    cursorType = Cursor.S_RESIZE;
	                }
	            } 
	            // 顶部不允许调整大小
//	            else if( y <= RESIZE_WIDTH ){
//	                if (x <= RESIZE_WIDTH) {// 左上角调整窗口状态
//	                	direction = DIRECTION.TOP_LEFT;
//	                } else if (x >= width - RESIZE_WIDTH) {// 右上角调整窗口状态
//	                	direction = DIRECTION.TOP_RIGHT;
//	                    cursorType = Cursor.NE_RESIZE;
//	                } else {// 下边界调整窗口状态
//	                	direction = DIRECTION.TOP;
//	                    cursorType = Cursor.N_RESIZE;
//	                }
//	            } 
	            else if (x >= width - RESIZE_WIDTH) {// 右边界调整窗口状态
	            	direction = DIRECTION.RIGHT;
	            	cursorType = Cursor.E_RESIZE;
	            } else if (x <= RESIZE_WIDTH) {
	            	direction = DIRECTION.LEFT;
	            	cursorType = Cursor.W_RESIZE;
	            }
	            // 最后改变鼠标光标
	            root.setCursor(cursorType);
			}
		});
    }
}