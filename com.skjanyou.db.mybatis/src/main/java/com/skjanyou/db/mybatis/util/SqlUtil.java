package com.skjanyou.db.mybatis.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class SqlUtil {
 
    /**
     * @param tableName 数据库表名
     * @param fields    字段数组
     * @param beanName  mybatis的mapper参数名字
     * https://www.cnblogs.com/leodaxin/p/10401272.html
     * @return
     */
    public static String generateInsertSql( Class<?> beanClass ) {
    	String tableName = beanClass.getSimpleName().toUpperCase(); 
    	Field[] fields = beanClass.getDeclaredFields();
        String sql = "INSERT INTO " + tableName + "(" + generateFieldsLine(fields) + ") values (";
 
        StringBuffer sqlBuilder = new StringBuffer();
        for (int i = 0, size = fields.length; i < size; i++) {
            if (i == fields.length - 1) {
                sqlBuilder.append("#").append(fields[i].getName()).append("#");
                continue;
            }
            sqlBuilder.append("#").append(fields[i].getName()).append("#").append(",");
        }
        return sql + sqlBuilder.toString() + ")";
    }
 
 
    public static<T> String generateSelectSQL( Class<T> beanClass  ) {
    	String tableName = beanClass.getSimpleName().toUpperCase(); 
    	Field[] fields = beanClass.getDeclaredFields();
        StringBuilder sqlBuilder = new StringBuilder();
        return sqlBuilder.append("SELECT ").append(generateFieldsLine(fields)).append(" FROM ").append(tableName).toString();
    }
 
    public static String generateUpdateSql(String tableName, Field[] fields, String beanName) {
        String sql = "UPDATE " + tableName + " SET ";
 
        StringBuffer sqlBuilder = new StringBuffer();
        for (int i = 0, size = fields.length; i < size; i++) {
            if (i == fields.length - 1) {
                sqlBuilder.append(fields[i].getName()).append("=").append("#" + beanName).append(".").append(fields[i].getName()).append("#");
                continue;
            }
            sqlBuilder.append(fields[i].getName()).append("=").append("#" + beanName).append(".").append(fields[i].getName()).append("#").append(",");
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
                sqlBuilder.append(curField.getName());
                continue;
            }
            sqlBuilder.append(curField.getName()).append(",");
        }
        return sqlBuilder.toString();
    }    
}
