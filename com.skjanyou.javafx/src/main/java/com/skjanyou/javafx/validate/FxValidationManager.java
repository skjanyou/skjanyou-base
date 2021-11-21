package com.skjanyou.javafx.validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skjanyou.javafx.anno.FxAnnotation.FxValidate;
import com.skjanyou.javafx.anno.FxAnnotation.FxValidateRule;
import com.skjanyou.javafx.validate.FxValidateResult.FxValidateResultDetail;
import com.skjanyou.javafx.validate.defaults.ComplexityFxValidateRuleDefined;
import com.skjanyou.javafx.validate.defaults.RequiredFxValidateRuleDefined;

import javafx.scene.Parent;

public class FxValidationManager {
	
	
	
	private static Map<String,FxValidateRuleDefined> rulesMap = new HashMap<>();
	
	static {
		addValidateRule(new RequiredFxValidateRuleDefined());
		addValidateRule(new ComplexityFxValidateRuleDefined());
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

	public static FxValidateResult validate( Parent parent, List<FxValidateRule> validateRuleList ) {
		FxValidateResult result = new FxValidateResult();
		result.hasFail = false;
		if( validateRuleList != null ) {
			for (FxValidateRule fxValidateRule : validateRuleList) {
				String name = fxValidateRule.rule();
				String[] param = fxValidateRule.param();
				String preRule = fxValidateRule.preRule();
				FxValidateResultDetail resultDetail = processValidateRule(name,parent,param);
				if( resultDetail != null && resultDetail.hasFail ) {
					result.hasFail = true;
					result.resultDetail.add(resultDetail);
				}
			}
		}
		return result;
	}
	
	public static FxValidateResult validate( Parent parent, FxValidate validate ) {
		FxValidateResult result = new FxValidateResult();
		result.hasFail = false;
		if( validate != null ) {
			FxValidateRule[] validateRuleList = validate.value();
			for (FxValidateRule fxValidateRule : validateRuleList) {
				String name = fxValidateRule.rule();
				String[] param = fxValidateRule.param();
				String preRule = fxValidateRule.preRule();
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
