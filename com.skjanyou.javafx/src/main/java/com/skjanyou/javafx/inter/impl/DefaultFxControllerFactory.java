package com.skjanyou.javafx.inter.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;

import com.skjanyou.javafx.anno.FxAnnotation.FxController;
import com.skjanyou.javafx.anno.FxAnnotation.FxDecorator;
import com.skjanyou.javafx.anno.FxAnnotation.ResponsiveBean;
import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.core.BeanProperty;
import com.skjanyou.javafx.core.BeanPropertyHelper;
import com.skjanyou.javafx.inter.BeanPropertyBuilder;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.FxControllerFactoryProperty;
import com.skjanyou.javafx.inter.FxEventDispatcher;
import com.skjanyou.javafx.inter.FxFXMLLoader;
import com.skjanyou.javafx.inter.JavaFxDecorator;
import com.sun.javafx.fxml.BeanAdapter;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SuppressWarnings("restriction")
public class DefaultFxControllerFactory implements FxControllerFactory,FxControllerFactoryProperty {
	/** 事件分发器 **/
	private FxEventDispatcher eventDispatcher;
	/** FXML加载器 **/
	private FxFXMLLoader fxmlLoader;
	/** Controller类 **/
	private Class<?> controllerClass;
	/** Controller注解类 **/
	private FxController fxControllerAnno;
	/** Controller类上面的装饰器注解类 **/
	private FxDecorator fxDecorator;
	/** Controller实例对象 **/
	private Object proxyController;
	/** Root对象 **/
	private Parent parent;
	/** 加载的结果,包括Root对象和Controller类 **/
	private LoadResult loadResult;
	/** 用于展示用的Stage **/
	private Stage stage;
	/** Scene **/
	private Scene scene;
	
	public DefaultFxControllerFactory( Class<?> controllerClass ) {
		this.controllerClass = controllerClass;
		this.fxControllerAnno = controllerClass.getAnnotation(FxController.class);
		if( fxControllerAnno == null ) {
			throw new RuntimeException("Controller类上面必须携带FxController注解");
		}
		this.fxDecorator = controllerClass.getAnnotation(FxDecorator.class);
	}
	
	@Override
	public FxEventDispatcher getFxEventDispatcher() {
		return this.eventDispatcher = this.eventDispatcher == null ? new DefaultFxEventDispatcher(this.proxyController,this.controllerClass) : this.eventDispatcher;
	}


	@Override
	public FxFXMLLoader getFxFXMLLoader() {
		return this.fxmlLoader = this.fxmlLoader == null ? new DefaultFxFXMLLoader( this.controllerClass ) : this.fxmlLoader;
	}
	

	@Override
	public LoadResult createController() {
		if( this.loadResult == null ) {
			this.loadResult = initControllerBean();
			this.parent = loadResult.getParent();
			this.proxyController = loadResult.getController();
			this.stage = loadResult.getStage();
			EventHandler<Event> handler = getFxEventDispatcher().getEventHandler();
			this.parent.addEventFilter(Event.ANY, handler);
			
			
			initFxml( this.proxyController,this.parent );
			initDecorator( this.proxyController,this.parent,this.stage );
			initEventHandler( this.proxyController,this.parent );
			initResponsiveBean( this.proxyController,this.parent );
		} 
		return this.loadResult;
	}

	/**
	 *   初始化窗口修饰器
	 * @param proxyController
	 * @param parent
	 */
	private void initDecorator(Object proxyController, Parent parent,Stage stage) {
		
		if( this.fxDecorator == null ) {
			this.scene = new Scene(this.parent);
			return ;
		}
        try {
        	String fxml = this.fxDecorator.fxml();
            Class<?> configClass = this.fxDecorator.config();
        	FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Thread.currentThread().getContextClassLoader().getResource(fxml));
            Pane root = fxmlLoader.load();
            Object controller = fxmlLoader.getController();
            if( controller.getClass() != configClass ) {
            	throw new IllegalArgumentException(String.format("FXML文件{%s}配置的controller必须为{%s}", fxml,configClass.toString()));
            }
            
            JavaFxDecorator decorator = (JavaFxDecorator) controller;
            decorator.addContent(parent).initDecorator(stage);
            
            this.scene = new Scene(root);
            this.scene.setFill(Color.TRANSPARENT);
            this.stage.initStyle(StageStyle.TRANSPARENT);
            this.loadResult.setScene(this.scene);
            this.loadResult.setParent(root);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

	/** 
	 * 初始化Controller类
	 */
	protected LoadResult initControllerBean() {
		FxFXMLLoader fxmlLoader = getFxFXMLLoader();
		return fxmlLoader.loader();
	}
	
	
	protected void initFxml(Object controller,Parent parent) {
		
	}
	
	
	
	protected void initEventHandler(Object controller,Parent parent) {
		FxEventDispatcher eventDispatcher = getFxEventDispatcher();
		parent.addEventFilter(Event.ANY, eventDispatcher.getEventHandler());
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected void initResponsiveBean(Object controller,Parent parent) {
		Field[] fields = this.controllerClass.getDeclaredFields();
		for (Field field : fields) {
			ResponsiveBean respBean = field.getAnnotation(ResponsiveBean.class);
			if( respBean != null ) {
				String[] binds = respBean.value();
				Class<?> clazz = field.getType();
				

				BeanProperty beanProperty = new BeanPropertyHelper(clazz).builder();
				try {
					field.setAccessible(true);
					Object propertyBean = beanProperty.getBean();
					field.set(controller, propertyBean);
					for (String express : binds) {
						// card_id=#testText.text
						String key = express.split("=")[0];
						String selector = express.split("=")[1].split("\\.")[0];
						String property = express.split("=")[1].split("\\.")[1];
						Set<Node> set = parent.lookupAll(selector);
						for( Node node : set ) {
							BeanAdapter beanAdapter = new BeanAdapter(node);
							ObservableValue value = beanAdapter.getPropertyModel(property);
							Property p = (Property) value;
							BeanPropertyBuilder.bind(propertyBean,key,p);
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
