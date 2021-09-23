package com.skjanyou.start.util;

import java.util.stream.IntStream;

public class StringUtils {
    /**
     * 得到一个字符串中双字节出现的次数
     *
     * @param cell
     * @return
     */
    public static Integer getZHCharCount(String cell) {
        if (cell == null) {
            return 0;
        }
        return cell.length() - getENCharCount(cell);
    }
    
    /**
     * 得到一个字符串中单字节出现的次数
     *
     * @param cell
     * @return
     */
    public static Integer getENCharCount(String cell) {
        if (cell == null) {
            return 0;
        }
        String reg = "[^\t\\x00-\\xff]";
//        String reg = "|[^\t\\x00-\\xff]";
        return cell.replaceAll(reg, "").length();
    }  
    
    /**
     * 将str重复count次，返回结果
     *
     * @param str
     * @param count
     * @return
     */
    public static String getRepeatChar(String str, int count) {
        StringBuilder res = new StringBuilder();
        IntStream.range(0, count).forEach(i -> res.append(str));
        return res.toString();
    }    
}
