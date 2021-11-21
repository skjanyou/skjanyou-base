package com.skjanyou.javafx.validate;

import com.skjanyou.javafx.validate.FxValidateResult.FxValidateResultDetail;

import javafx.scene.Parent;

public interface FxValidateRuleDefined {
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
