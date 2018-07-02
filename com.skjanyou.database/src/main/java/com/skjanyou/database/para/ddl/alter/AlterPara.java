
package com.skjanyou.database.para.ddl.alter;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.skjanyou.database.para.comm.ColumnType;
import com.skjanyou.database.para.ddl.DDLPara;

/**
 * DDL语句中Alter操作的参数
 * 默认使用ADD
 * 2016年3月2日9:51:25
 * **/
public final class AlterPara implements Serializable,DDLPara{
    private static final long serialVersionUID = "AlterPara".hashCode();
    @SuppressWarnings("unused")
    private static final String DEFAULT_METHOD = ADD;
    /**默认使用的字段**/
    private static final ColumnType DEFAULT_TYPE = ColumnType.VARCHAR;
    private String method = null;
    private Map<String,String> map = null;
    public AlterPara(String method){
        if(method == null || method.equals("")){
            throw new RuntimeException("无法进行空操作！");
        }else{
            if(ADD.equalsIgnoreCase(method)){
                this.method = ADD;
            }else if(DROP.equalsIgnoreCase(method)){
                this.method = DROP;
            }else if(MODIFY.equalsIgnoreCase(method)){
            	this.method = MODIFY;
            }
        }
    }
    
    /**指定字段名字和类型进行添加**/
    public synchronized void put(String key,String value) throws SQLException{
        if(map == null){
            map = new LinkedHashMap<String, String>();
        }
        if(!ColumnType.isExist(value)){throw new SQLException("给出的行类型有误！");}
        map.put(key, value);
    }
    
    /**使用默认字段类型进行添加**/
    public void put(String key) throws SQLException{
        put(key,DEFAULT_TYPE.getValue());
    }

    /**得到需要对表进行的操作**/
    public String getMethod() {
        return method;
    }

    /**得到map、key为字段名字，value为字段类型**/
    public Map<String, String> getMap() {
        return map;
    }
    
    
}

