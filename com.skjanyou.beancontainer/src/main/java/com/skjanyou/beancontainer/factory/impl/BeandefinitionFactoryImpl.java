package com.skjanyou.beancontainer.factory.impl;

import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.beancontainer.factory.BeandefinitionFactory;

public class BeandefinitionFactoryImpl implements BeandefinitionFactory {

	@Override
	public Beandefinition create() {
		return new BeandefinitionImpl();
	}
	
}
