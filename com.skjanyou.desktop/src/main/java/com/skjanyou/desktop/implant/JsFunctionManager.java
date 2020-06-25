package com.skjanyou.desktop.implant;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.skjanyou.desktop.annotation.JsClass;
import com.skjanyou.desktop.annotation.JsFunction;
import com.skjanyou.desktop.annotation.JsFunctionArgs;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.plugin.util.InstanceUtil;
import com.skjanyou.util.CommUtil;
import com.skjanyou.util.convert.ConvertUtil;

public class JsFunctionManager {
	private Logger logger = LogUtil.getLogger(JsFunctionManager.class);
	private JsFunctionManager(){}
	public static final JsFunctionManager INSTANCE = new JsFunctionManager();
	private static Map<String,Object> targetObjects = new HashMap<>();
	private static Map<String,Method> jsFunctionMap = new HashMap<>();
	
	
	public JsFunctionManager resolveClass( Class<?> targetClass ){
		
		JsClass jc = targetClass.getAnnotation(JsClass.class);
		if( !CommUtil.isNullOrEmpty(jc) ){
			String jsName = jc.value();
			Object targetObj = InstanceUtil.newInstance(targetClass);
			targetObjects.put(jsName, targetObj);
			
			Method[] methods = targetClass.getMethods();
			for (Method fun : methods) {
				JsFunction jsFun = fun.getAnnotation(JsFunction.class);
				if( !CommUtil.isNullOrEmpty(jsFun) ){
					String fullName = jsName + "." + jsFun.value();
					jsFunctionMap.put(fullName, fun);
				}
			}
		}
		return INSTANCE;
	}
	
	/**
	 * Js调用Java的总入口函数
	 * **/
	public Object invoke( String classId, String funcId, String msg ){
		logger.debug("收到数据:",msg);
		JSONObject request = JSON.parseObject(msg);
		JSONObject response = new JSONObject();
		Object obj = targetObjects.get(classId);
		if( CommUtil.isNullOrEmpty(obj) ){
			response.put("error_cd", "-1");
			response.put("error_title", "方法未实现");
			return response.toJSONString();
		}
		Method method = jsFunctionMap.get( classId + "." + funcId);
		if( CommUtil.isNullOrEmpty(method) ){
			response.put("error_cd", "-1");
			response.put("error_title", "方法未实现");
			return response.toJSONString();
		}
		List<Object> linkList = new LinkedList<>();
		Parameter[] parameters = method.getParameters();
		for (Parameter parameter : parameters) {
			// 查询参数
			JsFunctionArgs jsFunctionArgs = parameter.getAnnotation(JsFunctionArgs.class);
			if( jsFunctionArgs != null ){
				String p = jsFunctionArgs.value();
				Object pObject = request.get(p);
				if( pObject == null && jsFunctionArgs.required() ){
					response.put("error_cd", "-2");
					response.put("error_title", "请求参数[" + p + "]未传");
					return response.toJSONString();
				}
				if( pObject != null ){
					pObject = ConvertUtil.convert(pObject, parameter.getType());
				}
				linkList.add(pObject);
				continue;
			}
			linkList.add(null);
		}
		Object[] invokeArgs = linkList.toArray(new Object[]{});
		Object result = null;
		try {
			result = method.invoke(obj,invokeArgs);
			response.put("data", result);
			response.put("error_cd", "0");
			response.put("error_title", "成功");
		} catch (Exception e) {
			logger.error("方法调用失败",e);
			response.put("error_cd", "-1");
			response.put("error_title", "调用失败");
		}
		String resultMsg = response.toJSONString();
		logger.debug("返回数据:",resultMsg);
		return resultMsg;
	}
}
