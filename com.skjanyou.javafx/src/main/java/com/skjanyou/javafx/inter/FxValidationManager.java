package com.skjanyou.javafx.inter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.skjanyou.javafx.anno.FxAnnotation.FxValidate;
import com.skjanyou.javafx.anno.FxAnnotation.FxValidateRule;
import com.skjanyou.util.StringUtil;

import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

public class FxValidationManager {
	public static interface FxValidateRuleDefined {
		/**
		 * 获取校验规则名字
		 * @return
		 */
		public String name();
		/**
		 * 规则校验
		 * @param parent
		 * @param args
		 * @return 返回True表示通过校验,False表示不通过校验
		 */
		public FxValidateResultDetail validate( Parent parent, String[] args );
	}
	
	public static class FxValidateResult {
		public boolean hasFail;
		public List<FxValidateResultDetail> resultDetail = new LinkedList<>();
	}
	
	public static class FxValidateResultDetail {
		public String rule;
		public String error;
		public Parent parent;
		public boolean hasFail;
	}
	
	private static Map<String,FxValidateRuleDefined> rulesMap = new HashMap<>();
	
	static {
		addValidateRule(new FxValidateRuleDefined() {
			@Override
			public FxValidateResultDetail validate(Parent parent, String[] args) {
				FxValidateResultDetail detail = new FxValidateResultDetail();
				detail.rule = this.name();
				detail.parent = parent;
				detail.hasFail = false;
				detail.error = args[0];
				Object text = null;
				if( parent instanceof PasswordField) {
					PasswordField pw = (PasswordField) parent;
					text = pw.getText();
				}else if( parent instanceof TextField ) {
					TextField tf = (TextField) parent;
					text = tf.getText();
				}else if( parent instanceof ChoiceBox<?> ) {
					ChoiceBox<?> cb = (ChoiceBox<?>) parent;
					text = cb.getSelectionModel().getSelectedItem();
				}else if( parent instanceof CheckBox ) {
					CheckBox cb = (CheckBox) parent;
					text = cb.getText();
				}else if( parent instanceof HTMLEditor ) {
					
				}
				
				if( text == null || StringUtil.isBlank(text.toString() ) ) {
					detail.hasFail = true;
				}
				
				if( detail.hasFail ) {
					return detail;
				}
				
				return null;
			}
			
			@Override
			public String name() {
				return "required";
			}
		});
	}
	
	public static void addValidateRule( FxValidateRuleDefined rule ) {
		if( rulesMap.get(rule.name()) != null ) {
			throw new RuntimeException( String.format("校验规则%s已存在", rule.name()));
		}
		
		rulesMap.put(rule.name(), rule);
	}
	
	private static FxValidateResultDetail processValidateRule( String name , Parent parent, String[] param ) {
		FxValidateRuleDefined rule = rulesMap.get(name);
		FxValidateResultDetail resultDetail = null;
		if( rule == null ) {
			throw new RuntimeException( String.format("校验规则%s不存在", name));
		}
		resultDetail = rule.validate(parent, param);
		if( resultDetail != null ) {
			return resultDetail;
		}
		return resultDetail;
	}
	
	public static FxValidateResult validate( Parent parent, FxValidate validate ) {
		FxValidateResult result = new FxValidateResult();
		result.hasFail = false;
		if( validate != null ) {
			FxValidateRule[] validateRuleList = validate.value();
			for (FxValidateRule fxValidateRule : validateRuleList) {
				String name = fxValidateRule.rule();
				String[] param = fxValidateRule.param();
				fxValidateRule.preRule();
				FxValidateResultDetail resultDetail = processValidateRule(name,parent,param);
				if( resultDetail != null && resultDetail.hasFail ) {
					result.hasFail = true;
					result.resultDetail.add(resultDetail);
				}
			}
		}
		return result;
	}	
}
