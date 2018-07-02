
package com.skjanyou.database.para.dml.delete;

import java.io.Serializable;
import java.util.List;

public class DeleteParaItem implements Serializable{
    private static final long serialVersionUID = "DeleteParaItem".hashCode();
    
    private List<Object> list = null;
    public DeleteParaItem(List<Object> list){
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

