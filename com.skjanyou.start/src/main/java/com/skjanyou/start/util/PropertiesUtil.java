package com.skjanyou.start.util;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class PropertiesUtil {

	public static Properties read( URL url ) throws IOException{
		Properties propertis = new Properties();
		propertis.load(url.openStream());
		return propertis;
	}

	public static Properties combineByUrl( List<URL> urlList ) {
		Properties result = new Properties();

		return result;
	}
}
