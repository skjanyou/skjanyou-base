
package com.skjanyou.database.para.comm;


/**列的类型枚举，包括列的大小写形态**/
public enum ColumnType{
    VARCHAR("VARCHAR"),CHAR("CHAR"),NUMBER("NUMBER"),DECIMAL("DECIMAL"),
    VARCHAR_LOW("varchar"),CHAR_LOW("char"),NUMBER_LOW("number"),DECIMAL_LOW("decimal");
    String value;
    ColumnType(String value){
        this.value = value;
    }
    
    public String getValue(){
        return this.value;
    }
    @Override
    public String toString() {
        return getValue();
    }
    
    public static boolean isExist(String type){
        boolean result = true;
        if(type.contains(ColumnType.VARCHAR.getValue())){
            
        }else if(type.contains(ColumnType.CHAR.getValue())){
            
        }else if(type.contains(ColumnType.DECIMAL.getValue())){
            
        }else if(type.contains(ColumnType.NUMBER.getValue())){
            
        }else if(type.contains(ColumnType.VARCHAR_LOW.getValue())){
            
        }else if(type.contains(ColumnType.CHAR_LOW.getValue())){
            
        }else if(type.contains(ColumnType.DECIMAL_LOW.getValue())){
            
        }else if(type.contains(ColumnType.NUMBER_LOW.getValue())){
            
        }else{
            result = false;
        }
        return result;
    }
    
}

