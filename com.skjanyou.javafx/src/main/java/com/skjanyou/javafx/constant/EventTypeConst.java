package com.skjanyou.javafx.constant;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javafx.event.Event;
import javafx.event.EventType;

public final class EventTypeConst {
	public static class Event {
		public static final String ANY = "EVENT";
	}
	public static class ActionEvent {
		public static final String ACTION = "ACTION";
		public static final String ANY = "ACTION";
	}
	public static class MediaMarkerEvent {
	}
	public static class CellEditEvent {
		public static final String ANY = "TABLE_COLUMN_EDIT";
	}
	public static class TREE_TABLE_CellEditEvent {
		public static final String ANY = "TREE_TABLE_COLUMN_EDIT";
	}
	public static class DialogEvent {
		public static final String ANY = "DIALOG";
		public static final String DIALOG_SHOWING = "DIALOG_SHOWING";
		public static final String DIALOG_SHOWN = "DIALOG_SHOWN";
		public static final String DIALOG_HIDING = "DIALOG_HIDING";
		public static final String DIALOG_HIDDEN = "DIALOG_HIDDEN";
		public static final String DIALOG_CLOSE_REQUEST = "DIALOG_CLOSE_REQUEST";
	}
	public static class DirectEvent {
		public static final String DIRECT = "DIRECT";
	}
	public static class FocusUngrabEvent {
		public static final String FOCUS_UNGRAB = "FOCUS_UNGRAB";
		public static final String ANY = "FOCUS_UNGRAB";
	}
	public static class InputEvent {
		public static final String ANY = "INPUT";
	}
	public static class ContextMenuEvent {
		public static final String CONTEXT_MENU_REQUESTED = "CONTEXTMENUREQUESTED";
		public static final String ANY = "CONTEXTMENUREQUESTED";
	}
	public static class DragEvent {
		public static final String ANY = "DRAG";
		public static final String DRAG_ENTERED_TARGET = "DRAG_ENTERED_TARGET";
		public static final String DRAG_ENTERED = "DRAG_ENTERED";
		public static final String DRAG_EXITED_TARGET = "DRAG_EXITED_TARGET";
		public static final String DRAG_EXITED = "DRAG_EXITED";
		public static final String DRAG_OVER = "DRAG_OVER";
		public static final String DRAG_DROPPED = "DRAG_DROPPED";
		public static final String DRAG_DONE = "DRAG_DONE";
	}
	public static class GestureEvent {
		public static final String ANY = "GESTURE";
	}
	public static class RotateEvent {
		public static final String ANY = "ANY_ROTATE";
		public static final String ROTATE = "ROTATE";
		public static final String ROTATION_STARTED = "ROTATION_STARTED";
		public static final String ROTATION_FINISHED = "ROTATION_FINISHED";
	}
	public static class ScrollEvent {
		public static final String ANY = "ANY_SCROLL";
		public static final String SCROLL = "SCROLL";
		public static final String SCROLL_STARTED = "SCROLL_STARTED";
		public static final String SCROLL_FINISHED = "SCROLL_FINISHED";
	}
	public static class SwipeEvent {
		public static final String ANY = "ANY_SWIPE";
		public static final String SWIPE_LEFT = "SWIPE_LEFT";
		public static final String SWIPE_RIGHT = "SWIPE_RIGHT";
		public static final String SWIPE_UP = "SWIPE_UP";
		public static final String SWIPE_DOWN = "SWIPE_DOWN";
	}
	public static class ZoomEvent {
		public static final String ANY = "ANY_ZOOM";
		public static final String ZOOM = "ZOOM";
		public static final String ZOOM_STARTED = "ZOOM_STARTED";
		public static final String ZOOM_FINISHED = "ZOOM_FINISHED";
	}
	public static class InputMethodEvent {
		public static final String INPUT_METHOD_TEXT_CHANGED = "INPUT_METHOD_TEXT_CHANGED";
		public static final String ANY = "INPUT_METHOD_TEXT_CHANGED";
	}
	public static class KeyEvent {
		public static final String ANY = "KEY";
		public static final String KEY_PRESSED = "KEY_PRESSED";
		public static final String KEY_RELEASED = "KEY_RELEASED";
		public static final String KEY_TYPED = "KEY_TYPED";
	}
	public static class MouseEvent {
		public static final String ANY = "MOUSE";
		public static final String MOUSE_PRESSED = "MOUSE_PRESSED";
		public static final String MOUSE_RELEASED = "MOUSE_RELEASED";
		public static final String MOUSE_CLICKED = "MOUSE_CLICKED";
		public static final String MOUSE_ENTERED_TARGET = "MOUSE_ENTERED_TARGET";
		public static final String MOUSE_ENTERED = "MOUSE_ENTERED";
		public static final String MOUSE_EXITED_TARGET = "MOUSE_EXITED_TARGET";
		public static final String MOUSE_EXITED = "MOUSE_EXITED";
		public static final String MOUSE_MOVED = "MOUSE_MOVED";
		public static final String MOUSE_DRAGGED = "MOUSE_DRAGGED";
		public static final String DRAG_DETECTED = "DRAG_DETECTED";
	}
	public static class MouseDragEvent {
		public static final String ANY = "MOUSE-DRAG";
		public static final String MOUSE_DRAG_OVER = "MOUSE-DRAG_OVER";
		public static final String MOUSE_DRAG_RELEASED = "MOUSE-DRAG_RELEASED";
		public static final String MOUSE_DRAG_ENTERED_TARGET = "MOUSE-DRAG_ENTERED_TARGET";
		public static final String MOUSE_DRAG_ENTERED = "MOUSE-DRAG_ENTERED";
		public static final String MOUSE_DRAG_EXITED_TARGET = "MOUSE-DRAG_EXITED_TARGET";
		public static final String MOUSE_DRAG_EXITED = "MOUSE-DRAG_EXITED";
	}
	public static class TouchEvent {
		public static final String ANY = "TOUCH";
		public static final String TOUCH_PRESSED = "TOUCH_PRESSED";
		public static final String TOUCH_MOVED = "TOUCH_MOVED";
		public static final String TOUCH_RELEASED = "TOUCH_RELEASED";
		public static final String TOUCH_STATIONARY = "TOUCH_STATIONARY";
	}
	public static class MediaErrorEvent {
		public static final String MEDIA_ERROR = "Media Error Event";
	}
	public static class RedirectedEvent {
		public static final String REDIRECTED = "REDIRECTED";
	}
	public static class ScrollToEvent {
		public static final String ANY = "SCROLL_TO";
		public static final String SCROLL_TO_TOP_INDEX = "SCROLL_TO_TOP_INDEX";
		public static final String SCROLL_TO_COLUMN = "SCROLL_TO_COLUMN";
	}
	public static class SortEvent {
		public static final String ANY = "SORT";
		public static final String SORT_EVENT = "SORT_EVENT";
	}
	public static class TransformChangedEvent {
		public static final String TRANSFORM_CHANGED = "TRANSFORM_CHANGED";
		public static final String ANY = "TRANSFORM_CHANGED";
	}
	public static class CheckBox_TreeModificationEvent {
		public static final String ANY = "TREE_MODIFICATION";
	}
	public static class TreeModificationEvent {
		public static final String ANY = "TreeNotificationEvent";
	}
	public static class WebErrorEvent {
		public static final String ANY = "WEB_ERROR";
		public static final String USER_DATA_DIRECTORY_ALREADY_IN_USE = "USER_DATA_DIRECTORY_ALREADY_IN_USE";
		public static final String USER_DATA_DIRECTORY_IO_ERROR = "USER_DATA_DIRECTORY_IO_ERROR";
		public static final String USER_DATA_DIRECTORY_SECURITY_ERROR = "USER_DATA_DIRECTORY_SECURITY_ERROR";
	}
	public static class WebEvent {
		public static final String ANY = "WEB";
		public static final String RESIZED = "WEB_RESIZED";
		public static final String STATUS_CHANGED = "WEB_STATUS_CHANGED";
		public static final String VISIBILITY_CHANGED = "WEB_VISIBILITY_CHANGED";
		public static final String ALERT = "WEB_ALERT";
	}
	public static class WindowEvent {
		public static final String ANY = "WINDOW";
		public static final String WINDOW_SHOWING = "WINDOW_SHOWING";
		public static final String WINDOW_SHOWN = "WINDOW_SHOWN";
		public static final String WINDOW_HIDING = "WINDOW_HIDING";
		public static final String WINDOW_HIDDEN = "WINDOW_HIDDEN";
		public static final String WINDOW_CLOSE_REQUEST = "WINDOW_CLOSE_REQUEST";
	}
	public static class WorkerStateEvent {
		public static final String ANY = "WORKER_STATE";
		public static final String WORKER_STATE_READY = "WORKER_STATE_READY";
		public static final String WORKER_STATE_SCHEDULED = "WORKER_STATE_SCHEDULED";
		public static final String WORKER_STATE_RUNNING = "WORKER_STATE_RUNNING";
		public static final String WORKER_STATE_SUCCEEDED = "WORKER_STATE_SUCCEEDED";
		public static final String WORKER_STATE_CANCELLED = "WORKER_STATE_CANCELLED";
		public static final String WORKER_STATE_FAILED = "WORKER_STATE_FAILED";
	}

}
