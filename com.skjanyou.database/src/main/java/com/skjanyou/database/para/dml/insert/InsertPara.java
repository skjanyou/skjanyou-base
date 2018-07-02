
package com.skjanyou.database.para.dml.insert;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.skjanyou.database.para.dml.DMLPara;

public class InsertPara<K,V> implements Serializable,DMLPara{
    private static final long serialVersionUID = "InsertPara".hashCode();
    private Map<K,V> map = new HashMap<K, V>();

    @SuppressWarnings("rawtypes")
    public InsertPara put(K k,V v){
        map.put(k,v);
        return this;
    }
    
    public int size(){
        return map.size();
    }
    
    public Map<K,V> getMap(){
        return map;
    }
    
}

