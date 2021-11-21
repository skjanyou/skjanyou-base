package com.skjanyou.javafx.validate.defaults;

import com.skjanyou.javafx.validate.FxValidateResult.FxValidateResultDetail;
import com.skjanyou.javafx.validate.FxValidateRuleDefined;
import com.skjanyou.util.StringUtil;

import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

/**
 * 必输要求的校验器
 *
 */
public class RequiredFxValidateRuleDefined implements FxValidateRuleDefined {
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
}
