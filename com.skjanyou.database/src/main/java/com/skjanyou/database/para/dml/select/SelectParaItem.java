
package com.skjanyou.database.para.dml.select;

import java.util.List;

public class SelectParaItem{
    private List<Object> list = null;
    public SelectParaItem(List<Object> list){
        this.list = list;
    }
    
    public Object getColumnName(){
        return list.get(0);
    }
    
    public Object getValue(){
        return list.get(1);
    }
    
    public Object getMethod(){
        return list.get(2);
    }
    
    public Object getAndOr(){
        return list.get(3);
    }
}
