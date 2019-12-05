package com.skjanyou.annotation.core.process;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import com.skjanyou.annotation.core.util.AnnotationUtil;

public final class AnnotationResolve {
	private static Map<Class<? extends Annotation>,AnnotationProcess> processList = new HashMap<>();
	private AnnotationResolve(){}
	
	public static<T extends Annotation> void registAnnotationProcess( Class<T> anno, AnnotationProcess process ){
		if(processList.containsKey(anno)){
			processList.put(anno, process);
		} 
	}
	
	public static<T extends Annotation> void unRegistAnnotationProcess( Class<T> anno ){
		processList.remove(anno);
	}
	
	public static boolean process( Class<?> clazz ){
		processList.forEach( (anno,process) -> {
			if( AnnotationUtil.getTypeAnnotation(clazz, anno) != null ){
				process.process(clazz);
			}
		});
		return true;
	}
	
}
