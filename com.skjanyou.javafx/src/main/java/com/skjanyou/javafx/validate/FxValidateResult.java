package com.skjanyou.javafx.validate;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Parent;

public class FxValidateResult {
	public boolean hasFail;
	public List<FxValidateResultDetail> resultDetail = new LinkedList<>();
	
	public static class FxValidateResultDetail {
		public String rule;
		public String error;
		public Parent parent;
		public boolean hasFail;
	}
}
