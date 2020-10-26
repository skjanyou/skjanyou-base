package com.skjanyou;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;



public class P extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> arg0,
			RoundEnvironment arg1) {
		System.out.println(arg1);
		return false;
	}

}
