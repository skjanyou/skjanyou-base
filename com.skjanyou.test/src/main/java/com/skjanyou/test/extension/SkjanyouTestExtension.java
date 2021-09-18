package com.skjanyou.test.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import com.skjanyou.start.start.SkjanyouApplicationStart;


public class SkjanyouTestExtension implements BeforeAllCallback, AfterAllCallback, TestInstancePostProcessor,BeforeEachCallback, AfterEachCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback,ParameterResolver{
	private static final Namespace NAMESPACE = Namespace.create(SkjanyouTestExtension.class);
	
	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		System.out.println("supportsParameter");
		return false;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		System.out.println("resolveParameter");
		return null;
	}

	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		System.out.println("afterTestExecution");
	}

	@Override
	public void beforeTestExecution(ExtensionContext context) throws Exception {
		System.out.println("beforeTestExecution");
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		System.out.println("afterEach");
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		System.out.println("beforeEach");
	}

	@Override
	public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
		System.out.println("postProcessTestInstance");
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		System.out.println("afterAll");
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		System.out.println("beforeAll");
		// 拿到了Test类
		Class<?> testClass = context.getRequiredTestClass();
		Store store = context.getRoot().getStore(NAMESPACE);
		SkjanyouStartTest sst = testClass.getAnnotation(SkjanyouStartTest.class);
		if( sst == null ) {
			throw new IllegalArgumentException(String.format("类%s缺少注解@SkjanyouStartTest", testClass.getName()));
		}
		String[] propertys = sst.property();
		for (String property : propertys) {
			String[] arr = property.split("=");
			if( arr.length < 2 ) {
				throw new IllegalArgumentException(String.format("环境变量[%s]格式错误,应为[key=value]格式", property));
			}
			System.setProperty(arr[0], arr[1]);
		}
		String[] argus = sst.args();
		if( argus.length == 0 ) {
			argus = new String[] {"start"};
		}
		SkjanyouApplicationStart.start(SkjanyouStartTest.class, argus);
	}

}
