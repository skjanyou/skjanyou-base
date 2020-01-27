package com.skjanyou.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.TimeZone;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class XmlParseUtil {

	private static final DomDriver DOM_DRIVER = new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_"));

	private static final DateConverter DATE_CONVERTER = new DateConverter("yyyy-MM-dd HH:mm:ss", null, TimeZone.getTimeZone("GMT+8"));
	/**
	 * @param filePath file's path
	 * @param clz the class which is target Bean
	 * @param <T>
	 * @return the bean parse from xml
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2Bean(String filePath, Class<T> clz) {
		// loading driver
		// xstream也业提供了json的转换驱动，JettisonMappedXmlDriver和JsonHierarchicalStreamDriver。
		XStream xStream = new XStream(DOM_DRIVER);
		// setup security configuration
		// setupDefaultSecurity to be removed after 1.5
		XStream.setupDefaultSecurity(xStream);
		// register converter
		xStream.registerConverter(DATE_CONVERTER);
		// processing annotations
		xStream.processAnnotations(clz);
		// processing xml
		T t = (T) xStream.fromXML(new File(filePath));
		return t;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T xml2Bean( InputStream input,Class<T> clz) {
		// loading driver
		// xstream也业提供了json的转换驱动，JettisonMappedXmlDriver和JsonHierarchicalStreamDriver。
		XStream xStream = new XStream(DOM_DRIVER);
		// setup security configuration
		// setupDefaultSecurity to be removed after 1.5
		XStream.setupDefaultSecurity(xStream);
		// register converter
		xStream.registerConverter(DATE_CONVERTER);
		// processing annotations
		xStream.processAnnotations(clz);
		// processing xml
		T t = (T) xStream.fromXML(input);
		return t;
	}	

	/**
	 * return string of obj parsed by xstream.
	 *
	 * @param obj
	 * @return
	 */
	public static String toXml(Object obj) {
		XStream xStream = new XStream(DOM_DRIVER);
		// register converter
		xStream.registerConverter(DATE_CONVERTER);
		// processing annotations
		xStream.processAnnotations(obj.getClass());
		return xStream.toXML(obj);
	}

	/**
	 * parse obj to xml and write to file.
	 *
	 * @param obj
	 * @param filePath file path
	 */
	public void outputFile(Object obj, String filePath) throws FileNotFoundException {
		XStream xStream = new XStream(DOM_DRIVER);
		// register converter
		xStream.registerConverter(DATE_CONVERTER);
		// processing annotations
		xStream.processAnnotations(obj.getClass());
		// create target file
		FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
		// output
		xStream.toXML(obj, fileOutputStream);
	}
}