
package com.skjanyou.database.para.dml.update;

import java.util.List;

public class UpdateParaItem {
    private List<Object> list = null;
    public UpdateParaItem(List<Object> list){
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

