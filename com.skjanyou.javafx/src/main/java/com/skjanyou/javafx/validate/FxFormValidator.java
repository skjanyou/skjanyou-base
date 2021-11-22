package com.skjanyou.javafx.validate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.skjanyou.javafx.anno.FxAnnotation.FxValidate;

import javafx.scene.control.TextField;

public class FxFormValidator {
	private static final String DEFAULT_FORM = "DEFAULT-FORM";
	private static class ControllerRuleValidBean{
		private Field field;
		private Object fieldBean;
		private FxValidate valid;
		public ControllerRuleValidBean(Field field,Object fieldBean, FxValidate valid) {
			super();
			this.field = field;
			this.fieldBean = fieldBean;
			this.valid = valid;
		}
	}
	public static Map<Object,Map<String,List<ControllerRuleValidBean>>> map = new HashMap<>();
	
	public static void registFormValidRule( Object controller , Field field, Object fieldBean, FxValidate fxValidate ){
		// 按照Form表单进行分组
		Map<String,List<ControllerRuleValidBean>> mapping = map.get(controller);
		if( mapping == null ) {
			mapping = new HashMap<>();
			map.put(controller, mapping);
		}
		List<String> formList = Arrays.asList(fxValidate.form());
		for (String form : formList) {
			List<ControllerRuleValidBean> beanList = mapping.get(form);
			if( beanList == null ){
				beanList = new LinkedList<>();
				mapping.put(form, beanList);
			}
			ControllerRuleValidBean bean = new ControllerRuleValidBean(field, fieldBean, fxValidate);
			beanList.add(bean);
		}
		
		
		System.out.println(map);
	}
	
	public static FxValidateResult valid( String form, Object controller ){
		FxValidateResult result = new FxValidateResult();
		if( map.size() == 0 ){
			result.hasFail = false;
			return result;
		}
		
		Map<String,List<ControllerRuleValidBean>> mapping = map.get(controller);
		List<ControllerRuleValidBean> beanList = mapping.get(form);
		if( beanList == null || beanList.size() == 0 ){
			throw new IllegalArgumentException(String.format("表单%s对应的校验域不存在", form));
		}
		for (ControllerRuleValidBean controllerRuleValidBean : beanList) {
			if( controllerRuleValidBean.fieldBean instanceof TextField) {
				TextField textField = (TextField) controllerRuleValidBean.fieldBean;
				FxValidateResult tempResult = FxValidationManager.validate(textField, Arrays.asList(controllerRuleValidBean.valid.value()));
				if( tempResult.hasFail ){
					result.hasFail  = true;
					result.resultDetail.addAll(tempResult.resultDetail);
				}
			}	
		}
		return result;
	}
}
