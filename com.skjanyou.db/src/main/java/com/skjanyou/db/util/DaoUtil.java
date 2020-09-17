package com.skjanyou.db.util;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoUtil {
	public static List<String> getMetaData( ResultSetMetaData metaData ) throws SQLException{
		List<String> list = new ArrayList<>();
		int count = metaData.getColumnCount();
		// 下标从1开始
		for( int columnIdx = 1; columnIdx <= count;columnIdx++ ){
			String columnName = metaData.getColumnName(columnIdx);
			list.add(columnName);
		}		
		return list;
	}
}
