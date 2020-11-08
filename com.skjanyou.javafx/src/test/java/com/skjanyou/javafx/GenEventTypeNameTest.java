package com.skjanyou.javafx;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.sun.javafx.event.DirectEvent;
import com.sun.javafx.event.RedirectedEvent;
import com.sun.javafx.stage.FocusUngrabEvent;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.SortEvent;
import javafx.scene.control.CheckBoxTreeItem.TreeModificationEvent;
import javafx.scene.control.ListView.EditEvent;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.GestureEvent;
import javafx.scene.input.InputEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.media.MediaErrorEvent;
import javafx.scene.media.MediaMarkerEvent;
import javafx.scene.transform.TransformChangedEvent;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.stage.WindowEvent;

/**
 * 用于生成EventTypeConst里面的事件名的辅助类
 * @author skjanyou
 *  时间 : 2020-11-8
 *  作用 :用于生成EventTypeConst里面的事件名的辅助类,通过class生成
 */
public class GenEventTypeNameTest {

	public static void main(String[] args) throws Exception {
		readClass(Event.class);
		readClass(ActionEvent.class);
		readClass(MediaMarkerEvent.class);
		readClass(CellEditEvent.class);
		
		readClass(javafx.scene.control.TreeTableColumn.CellEditEvent.class);
		readClass(DialogEvent.class);
		readClass(DirectEvent.class);
		readClass(EditEvent.class);
		readClass(javafx.scene.control.TreeTableView.EditEvent.class);
		readClass(javafx.scene.control.TreeView.EditEvent.class);
		readClass(FocusUngrabEvent.class);
		readClass(InputEvent.class);
		readClass(ContextMenuEvent.class);
		readClass(DragEvent.class);
		readClass(GestureEvent.class);
		readClass(RotateEvent.class);
		readClass(ScrollEvent.class);
		readClass(SwipeEvent.class);
		readClass(ZoomEvent.class);
		readClass(InputMethodEvent.class);
		readClass(KeyEvent.class);
		readClass(MouseEvent.class);
		readClass(MouseDragEvent.class);
		readClass(TouchEvent.class);
		readClass(MediaErrorEvent.class);
		readClass(RedirectedEvent.class);
		readClass(ScrollToEvent.class);
		readClass(SortEvent.class);
		readClass(TransformChangedEvent.class);
		readClass(TreeModificationEvent.class);
		readClass(javafx.scene.control.TreeItem.TreeModificationEvent.class);
		readClass(WebErrorEvent.class);
		readClass(WebEvent.class);
		readClass(WindowEvent.class);
		readClass(WorkerStateEvent.class);
	}
	
	public static void readClass( Class<?> targetClass ) throws Exception {
//		System.out.println(targetClass);
		if( !Event.class.isAssignableFrom(targetClass) ) {
			return ;
		}
		
		System.out.println(String.format("	public static class %s {", targetClass.getSimpleName()));
		Field[] fields = targetClass.getDeclaredFields();
		for (Field field : fields) {
			if( field.getType().isAssignableFrom(EventType.class) && Modifier.isStatic(field.getModifiers())) {
				field.setAccessible(true);
				String fieldName = field.getName();
				try {
					EventType eventType = (EventType) field.get(null);
					String words = String.format("		public static final String %s = \"%s\";", fieldName,eventType.getName());
					System.out.println(words);
				}catch(Throwable e) { continue; }
			}
		}
		System.out.println("	}");
	}

}
