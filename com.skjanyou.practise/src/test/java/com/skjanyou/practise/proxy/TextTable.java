package com.skjanyou.practise.proxy;

import java.awt.Font;
import java.util.List;
import java.util.Map;
 
/**
 * 
 * @author Benjamin su
 * QQ:506825719
 * Email:jiang506825719@qq.com
 *
 */
public class TextTable {
	//这个变量只是用来获取字符串的标准长度，修改无意义
	private static Font font = new Font("宋体", Font.PLAIN, 16);
	private String columnInterval="|";//列之间的间隔符号
	private AlignType at=AlignType.Left;
	private Integer MaxValueLenth=40;
	private Map<String,Integer> maxFieldValueLengthMap=null;
	List<String> columnNameList=null;
	List<List<String>> columnValueList=null;
	private boolean isException=false;
	private Integer pringtTableRow=30;
	/**
	 * 初始化时不给定列长度Map
	 * @param columnNameListTmp
	 * @param columnValueListTmp
	 */
	public TextTable(List<String> columnNameListTmp,List<List<String>> columnValueListTmp){
	}
	/**
	 *  初始化时给定列长度Map
	 * @param columnNameListTmp
	 * @param columnValueListTmp
	 * @param maxFieldValueLengthMapTmp
	 */
	public TextTable(List<String> columnNameListTmp,List<List<String>> columnValueListTmp,Map<String,Integer> maxFieldValueLengthMapTmp){
	}
	
	/**
	 * 获得处理后的表格字符串
	 * @return
	 */
	public String printTable(){
		return null;
	}
	/**
	 * 获取一个字符串的标准长度是多少
	 * A2*.  这样是四个标准长度
	 * 系统系统 这样是8个标准长度
	 * @param str
	 * @return
	 */
    public static Integer getStrPixelsLenth(String str){  
    	return null;
    }
    /**
     * 让列值居中
     * @param columnValue
     * @param ColumnPixelsLenth
     * @return
     */
    private String makeValueCenter(String columnValue,Integer repaireStrLength){
    	return null;
    }
    /**
     * 让列值左对齐
     * @param columnValue
     * @param ColumnPixelsLenth
     * @return
     */
    private String makeValueLeft(String columnValue,Integer repaireStrLength){
    	return null;
    }
    /**
     * 让列值右对齐
     * @param columnValue
     * @param ColumnPixelsLenth
     * @return
     */
    private String makeValueRight(String columnValue,Integer repaireStrLength){
    	return null;
    }
    /**
     * 获取用于填充的字符串
     * @param str
     * @param PixelsLenth
     * @return
     */
    private static String getRepairStr(char str,Integer PixelsLenth){
    	return null;
    }
    /**
     * 根据像素长度来切割字符串
     * @param str
     * @param PixelsLenth
     * @return
     */
    private static String subStrByPixels(String str,Integer PixelsLenth){
    	return null;
    }
    /**
     * 让列值对齐(左对齐OR居中OR右对齐)
     * @param columnValue
     * @param ColumnPixelsLenth
     * @return
     */
    private String makeValueAlign(String columnValue,Integer ColumnPixelsLenth){
    	return null;
    }
    /**
     * 设置列与列之间用什么间隔，默认是|
     * @param columnInterval
     */
	public void setColumnInterval(String columnInterval) {
	}
	/**
	 * 设置列值最大长度，超过则用省略号代替
	 * @param maxValueLenth
	 */
	public void setMaxValueLenth(Integer maxValueLenth) {
	}
	/**
	 * 如果不指定列长度Map，这里将自动计算
	 * @param columnNameListTmp
	 * @param columnValueListTmp
	 * @param maxFieldValueLengthMapTmp
	 */
	private void setMaxFieldValueLengthMap(List<String> columnNameListTmp,List<List<String>> columnValueListTmp,Map<String, Integer> maxFieldValueLengthMapTmp) {
	}
	/**
	 * 设置最多打印多少行
	 * @param pringtTableRow
	 */
	public void setPringtTableRow(Integer pringtTableRow) {
	}
	/**
	 * 设置对齐方式
	 * @param at
	 */
	public void setAt(AlignType at) {
	}
	public enum AlignType {
    	Left,
        Center,
        Rigth
    }
}