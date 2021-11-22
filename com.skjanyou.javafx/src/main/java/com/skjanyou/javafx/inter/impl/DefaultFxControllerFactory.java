package com.skjanyou.javafx.inter.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skjanyou.javafx.anno.FxAnnotation.FxBean;
import com.skjanyou.javafx.anno.FxAnnotation.FxContext;
import com.skjanyou.javafx.anno.FxAnnotation.FxController;
import com.skjanyou.javafx.anno.FxAnnotation.FxDecorator;
import com.skjanyou.javafx.anno.FxAnnotation.FxValidate;
import com.skjanyou.javafx.anno.FxAnnotation.FxValidateRule;
import com.skjanyou.javafx.anno.FxAnnotation.FxValidateRule.RuleValidTrigger;
import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.constant.ControllerType;
import com.skjanyou.javafx.core.BeanProperty;
import com.skjanyou.javafx.core.BeanPropertyHelper;
import com.skjanyou.javafx.inter.BeanPropertyBuilder;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.FxControllerFactoryProperty;
import com.skjanyou.javafx.inter.FxEventDispatcher;
import com.skjanyou.javafx.inter.FxFXMLLoader;
import com.skjanyou.javafx.inter.JavaFxDecorator;
import com.skjanyou.javafx.validate.FxFormValidator;
import com.skjanyou.javafx.validate.FxValidateResult;
import com.skjanyou.javafx.validate.FxValidateResult.FxValidateResultDetail;
import com.skjanyou.javafx.validate.FxValidationManager;
import com.skjanyou.util.BeanUtil;
import com.skjanyou.util.StreamUtil;
import com.sun.javafx.fxml.BeanAdapter;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

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
	/** 历史Stage **/
	private LinkedList<Stage> stageList;
	/** 历史Scene **/
	private LinkedList<Scene> sceneList;
	/** 父Stage **/
	private Stage fatherStage;
	/** 用于展示用的Stage **/
	private Stage stage;
	/** Scene **/
	private Scene scene;
	// 类中的所有成员变量
	protected Field[] fields;
	// 类中的所有方法
	protected Method[] methods;
	
	public DefaultFxControllerFactory( Class<?> controllerClass ) {
		this(controllerClass,null,null);
	}
	
	public DefaultFxControllerFactory( Class<?> controllerClass,Stage stage ) {
		this(controllerClass,stage,null);
	}	
	
	public DefaultFxControllerFactory( Class<?> controllerClass,Stage stage ,Stage fatherStage  ) {
		this.controllerClass = controllerClass;
		this.stage = stage;
		this.fatherStage = fatherStage;
		this.fxControllerAnno = controllerClass.getAnnotation(FxController.class);
		if( fxControllerAnno == null ) {
			throw new RuntimeException("Controller类上面必须携带FxController注解");
		}
		this.fxDecorator = controllerClass.getAnnotation(FxDecorator.class);
		this.stageList = new LinkedList<>();
		this.sceneList = new LinkedList<>();
		this.fields = this.controllerClass.getDeclaredFields();
		this.methods = this.controllerClass.getDeclaredMethods();
	}
	
	@Override
	public FxEventDispatcher getFxEventDispatcher() {
		return this.eventDispatcher = this.eventDispatcher == null ? new BubbleFxEventDispatcher(this.proxyController,this.controllerClass) : this.eventDispatcher;
	}


	@Override
	public FxFXMLLoader getFxFXMLLoader() {
		return this.fxmlLoader = this.fxmlLoader == null ? new DefaultFxFXMLLoader( this.controllerClass,this.stage ) : this.fxmlLoader;
	}
	

	@Override
	public LoadResult createController() {
		if( this.loadResult == null ) {
			this.loadResult = initControllerBean();
			this.parent = loadResult.getParent();
			this.proxyController = loadResult.getController();
			this.stage = loadResult.getStage();
			try {
				this.stage.getIcons().add(new Image(StreamUtil.getInputStreamIgnoreLocation(this.fxControllerAnno.icon())));
				this.stage.setTitle(this.fxControllerAnno.title());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if( this.fxControllerAnno.type() == ControllerType.DIALOG && this.fatherStage != null ) {
				this.stage.initModality(Modality.APPLICATION_MODAL);
				this.stage.initOwner(this.fatherStage);
			}
//			if( this.fxControllerAnno.exitType() == ControllerExitType.CLOSE ) {
//				this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//					@Override
//					public void handle(WindowEvent event) {
//						event.consume();
//					}
//				});
//			}
			EventHandler<Event> handler = getFxEventDispatcher().getEventHandler();
			this.parent.addEventFilter(Event.ANY, handler);
			
			
			initFxml( this.proxyController,this.parent );
			initDecorator( this.proxyController,this.parent,this.stage );
			initEventHandler( this.proxyController,this.parent );
			initResponsiveBean( this.proxyController,this.parent );
			initContext(this.proxyController,this.parent);
			initValidate(this.proxyController,this.parent);
		} 
		return this.loadResult;
	}


	/**
	 *   初始化窗口修饰器
	 * @param proxyController
	 * @param parent
	 */
	private void initDecorator( Object proxyController, Parent parent, Stage stage ) {
		
		if( this.fxDecorator == null ) {
			// 这里不记得为什么要加这句代码,加入了会引发问题,暂时注释掉
//			this.scene = new Scene(this.parent);
			return ;
		}
        try {
        	String fxml = this.fxDecorator.fxml();
            Class<?> configClass = this.fxDecorator.config();
        	FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Thread.currentThread().getContextClassLoader().getResource(fxml));
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
				@Override
				public Object call(Class<?> param) {
					return BeanUtil.newInstanceIgnoreException(configClass);
				}
			});
            Pane root = fxmlLoader.load();
            Object controller = fxmlLoader.getController();
            if( !controller.getClass().isAssignableFrom(configClass) ) {
            	throw new IllegalArgumentException(String.format("FXML文件{%s}配置的controller必须为{%s}", fxml,configClass.toString()));
            }
            
            JavaFxDecorator decorator = (JavaFxDecorator) controller;
            decorator.addContent(parent).initDecorator(stage,this.fxDecorator);
            
            this.scene = new Scene(root);
            this.scene.setFill(Color.TRANSPARENT);
            try {
            	// 多次设置样式会出现问题,这里捕获并忽略
            	this.stage.initStyle(StageStyle.TRANSPARENT);
            }catch(IllegalStateException e) {}
           
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
		for (Field field : this.fields) {
			FxBean respBean = field.getAnnotation(FxBean.class);
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
	
	
	protected void initContext(Object controller,Parent parent) {
		for (Field field : this.fields) {
			FxContext fxContext = field.getAnnotation(FxContext.class);
			if( fxContext != null ) {
				// 
				Type type = field.getGenericType();
				if( type instanceof Class<?>) {
					Class<?> clazz = (Class<?>) type;
					field.setAccessible(true);
					try {
						if( clazz == Stage.class) {
							field.set(controller, this.stage);
						}else if( clazz == Scene.class) {
							field.set(controller, this.scene);
						}
					} catch( Exception e ) {
						throw new RuntimeException(e);
					}
				}
				
			}
		}
	}

	/**
	  *   初始化校验规则
	 * @param proxyController
	 * @param parent
	 */
	private void initValidate(Object proxyController, Parent parent) {
		for (Field field : this.fields) {
			FxValidate fxValidate = field.getAnnotation(FxValidate.class);
			if( fxValidate != null ) {
				FXML fxml = field.getAnnotation(FXML.class);
				if( fxml == null ) {
					throw new RuntimeException(String.format("变量%s的@FxValidate注解必须搭配@FXML注解使用", field.toGenericString()));
				}
				// 这里分成两类,第一类是Change,第二类是Blur,将校验规则分组后,然后再绑定事件监听
				FxValidateRule[] rules = fxValidate.value();
				// change事件的规则合集
				List<FxValidateRule> changeRuleList = new LinkedList<>();
				boolean hasChangeRule = false;
				// focus的事件的规则合集
				List<FxValidateRule> blurRuleList = new LinkedList<>();
				boolean hasblurRule = false;				
				for (FxValidateRule fxValidateRule : rules) {
					if( fxValidateRule.trigger() == RuleValidTrigger.CHANGE ){
						hasChangeRule = true;
						changeRuleList.add(fxValidateRule);
					}else{
						hasblurRule = true;
						blurRuleList.add(fxValidateRule);
					}
				}
				// 根据前面统计的结果进行事件绑定
				try {
					field.setAccessible(true);
					Object fieldBean = field.get(proxyController);
					if( fieldBean != null  ) {
						if( fieldBean instanceof TextField) {
							TextField textField = (TextField) fieldBean;
							// 判断是否有Change事件
							if( hasChangeRule ){
								textField.textProperty().addListener(e->{
									FxValidateResult result = FxValidationManager.validate(textField, changeRuleList);
									if( result.hasFail ) {
										String errorMsg = "";
										
										for (FxValidateResultDetail detail : result.resultDetail) {
											errorMsg += detail.rule + ":" + detail.error + ",";
										}
										if( errorMsg.length() > 0 ) { errorMsg = errorMsg.substring(0, errorMsg.length() - 1); }
										System.out.println("change: " + errorMsg);
									}
								});
							}
							// 判断是否有Blur事件
							if( hasblurRule ){
								textField.focusedProperty().addListener((ob, old, now) ->{
									if( !now ){
										System.out.println("blur");
										FxValidateResult result = FxValidationManager.validate(textField, blurRuleList);
										if( result.hasFail ) {
											String errorMsg = "";
											
											for (FxValidateResultDetail detail : result.resultDetail) {
												errorMsg += detail.rule + ":" + detail.error + ",";
											}
											if( errorMsg.length() > 0 ) { errorMsg = errorMsg.substring(0, errorMsg.length() - 1); }
											System.out.println("focus: " + errorMsg);
										}								
										
									}
								});
							}
						}
						// 添加表单校验规则
						FxFormValidator.registFormValidRule(proxyController, field,fieldBean, fxValidate );
					}
					field.setAccessible(false);
					
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
