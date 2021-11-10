package com.skjanyou.db.mybatis.util;

import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Clob;

import com.skjanyou.db.mybatis.anno.DDL.Table;
import com.skjanyou.db.mybatis.anno.DDL.TableField;
import com.skjanyou.util.CommUtil;

public class SqlUtil {

	/**
	 * @param tableName 数据库表名
	 * @param fields    字段数组
	 * @param beanName  mybatis的mapper参数名字
	 * https://www.cnblogs.com/leodaxin/p/10401272.html
	 * @return
	 */
	public static String generateInsertSql( Class<?> beanClass ) {
		String tableName = getTableName(beanClass);
		Field[] fields = beanClass.getDeclaredFields();
		String sql = "INSERT INTO " + tableName + "(" + generateFieldsLine(fields) + ") values (";

		StringBuffer sqlBuilder = new StringBuffer();
		for (int i = 0, size = fields.length; i < size; i++) {
			if (i == fields.length - 1) {
				sqlBuilder.append("#").append(getTableField(fields[i])).append("#");
				continue;
			}
			sqlBuilder.append("#").append(getTableField(fields[i])).append("#").append(",");
		}
		return sql + sqlBuilder.toString() + ")";
	}

	/**
	 *  通过Bean生成查询SQL
	 * @param <T>
	 * @param beanClass
	 * @return
	 */
	public static<T> String generateSelectSQL( Class<T> beanClass  ) {
		String tableName = beanClass.getSimpleName().toUpperCase(); 
		Field[] fields = beanClass.getDeclaredFields();
		StringBuilder sqlBuilder = new StringBuilder();
		return sqlBuilder.append("SELECT ").append(generateFieldsLine(fields)).append(" FROM ").append(tableName).toString();
	}

	/**
	 *  通过Bean生成查询SQL,并只取第一条
	 * @param <T>
	 * @param beanClass
	 * @return
	 */
	public static<T> String generateSelectFirstSQL( Class<T> beanClass  ) {
		return generateSelectSQL(beanClass) + " limit 1";
	}

	public static String generateUpdateSql(String tableName, Field[] fields, String beanName) {
		String sql = "UPDATE " + tableName + " SET ";

		StringBuffer sqlBuilder = new StringBuffer();
		for (int i = 0, size = fields.length; i < size; i++) {
			if (i == fields.length - 1) {
				sqlBuilder.append(getTableField(fields[i])).append("=").append("#" + beanName).append(".").append(getTableField(fields[i])).append("#");
				continue;
			}
			sqlBuilder.append(getTableField(fields[i])).append("=").append("#" + beanName).append(".").append(getTableField(fields[i])).append("#").append(",");
		}
		return sql + sqlBuilder.toString() + ")";
	}

	public static String generateFieldsLine(Field[] fields) {
		StringBuffer sqlBuilder = new StringBuffer();
		Field curField = null;
		for (int i = 0, size = fields.length; i < size; i++) {
			curField = fields[i];
			if( Modifier.isStatic(curField.getModifiers()) ) {
				continue;
			}
			if (i == fields.length - 1) {
				sqlBuilder.append(getTableField(curField));
				continue;
			}
			sqlBuilder.append(getTableField(curField)).append(",");
		}
		return sqlBuilder.toString();
	}  

	public static String clobConvertToString( Clob clob ) {
		if( clob == null ) {
			return null;
		}

		String result = null;
		Reader is = null;
		BufferedReader br = null;
		try {
			is = clob.getCharacterStream();
			br = new BufferedReader(is);
			String line = null;
			StringBuffer sb = new StringBuffer();
			while( ( line = br.readLine() ) != null ) {
				sb.append(line);
			}

			result = sb.toString();

		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			CommUtil.close(br);
			CommUtil.close(is);
		}

		return result;
	}

	/**
	 * 	获取表名
	 * @param beanClass
	 * @return
	 */
	private static String getTableName( Class<?> beanClass ) {
		String tableName = null;
		Table table = beanClass.getAnnotation(Table.class);
		if( table == null ) {
			tableName = beanClass.getSimpleName().toUpperCase(); 
		} else {
			tableName = table.value();
		}
		return tableName;
	}

	/**
	 * 	获取表字段
	 * @param field
	 * @return
	 */
	private static String getTableField( Field field ) {
		String tableFieldName = null;
		TableField tableField = field.getAnnotation(TableField.class);
		if( tableField == null ) {
			tableFieldName = field.getName();
		} else {
			tableFieldName = tableField.value();
		}

		return tableFieldName;
	}
}
