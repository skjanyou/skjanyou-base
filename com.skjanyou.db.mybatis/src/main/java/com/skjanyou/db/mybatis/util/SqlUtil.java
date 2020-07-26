package com.skjanyou.db.mybatis.util;

import java.lang.reflect.Field;

public class SqlUtil {
 
    /**
     * @param tableName 数据库表名
     * @param fileds    字段数组
     * @param beanName  mybatis的mapper参数名字
     * https://www.cnblogs.com/leodaxin/p/10401272.html
     * @return
     */
    public static String generateInsertSql( Class<?> beanClass ) {
    	String tableName = beanClass.getSimpleName().toUpperCase(); 
    	Field[] fileds = beanClass.getDeclaredFields();
        String sql = "INSERT INTO " + tableName + "(" + generateFieldsLine(fileds) + ") values (";
 
        StringBuffer sqlBuilder = new StringBuffer();
        for (int i = 0, size = fileds.length; i < size; i++) {
            if (i == fileds.length - 1) {
                sqlBuilder.append("#").append(fileds[i].getName()).append("#");
                continue;
            }
            sqlBuilder.append("#").append(fileds[i].getName()).append("#").append(",");
        }
        return sql + sqlBuilder.toString() + ")";
    }
 
 
    public static<T> String generateSelectSQL( Class<T> beanClass  ) {
    	String tableName = beanClass.getSimpleName().toUpperCase(); 
    	Field[] fileds = beanClass.getDeclaredFields();
        StringBuilder sqlBuilder = new StringBuilder();
        return sqlBuilder.append("SELECT ").append(generateFieldsLine(fileds)).append(" FROM ").append(tableName).toString();
    }
 
    public static String generateUpdateSql(String tableName, Field[] fileds, String beanName) {
        String sql = "UPDATE " + tableName + " SET ";
 
        StringBuffer sqlBuilder = new StringBuffer();
        for (int i = 0, size = fileds.length; i < size; i++) {
            if (i == fileds.length - 1) {
                sqlBuilder.append(fileds[i].getName()).append("=").append("#" + beanName).append(".").append(fileds[i].getName()).append("#");
                continue;
            }
            sqlBuilder.append(fileds[i].getName()).append("=").append("#" + beanName).append(".").append(fileds[i].getName()).append("#").append(",");
        }
        return sql + sqlBuilder.toString() + ")";
    }
    
    public static String generateFieldsLine(Field[] fileds) {
        StringBuffer sqlBuilder = new StringBuffer();
        for (int i = 0, size = fileds.length; i < size; i++) {
            if (i == fileds.length - 1) {
                sqlBuilder.append(fileds[i].getName());
                continue;
            }
            sqlBuilder.append(fileds[i].getName()).append(",");
        }
        return sqlBuilder.toString();
    }    
}
