package com.skjanyou.javafx.util;

import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

public class SVGUtil {
	private static Map<String,SVGPath> pathMap = new HashMap<>();
	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	public static SVGPath loadSvg( String filePath ) {
		SVGPath svgPath = new SVGPath();
		if( filePath.startsWith("/") ) {
			filePath = filePath.substring(1);
		}
		if( pathMap.containsKey(filePath) ) { return pathMap.get(filePath); }
		String path = loadSvgFile(filePath);
		svgPath.setContent(path);
		
		pathMap.put(filePath, svgPath);
		
		return svgPath;
	}


	public static String loadSvgFile(String pathName) {
		String result = "";
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//禁止DTD验证,防止网络阻塞
			builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
			URL url = new URL(pathName);
			Document root = builder.parse(url.openStream());
			NodeList pathList = root.getElementsByTagName("path");
			int size = pathList.getLength();
			for( int index = 0; index < size ; index++ ) {
				Node node = pathList.item(index);
				String path = node.getAttributes().getNamedItem("d").getNodeValue();
				result += path;
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static Region getSvgRegion( SVGPath path ) {
        Region playModeRegion = new Region();	
//        playModeRegion.prefWidth(30);
//        playModeRegion.prefHeight(30);
//        playModeRegion.setMinSize(30, 30);
//        playModeRegion.setPrefSize(30, 30);
//        playModeRegion.setMaxSize(30, 30);        
        playModeRegion.setShape(path);
        
        return playModeRegion;
	}
	

}
