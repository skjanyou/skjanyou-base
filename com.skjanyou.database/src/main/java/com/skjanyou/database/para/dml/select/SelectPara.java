
package com.skjanyou.database.para.dml.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.skjanyou.database.para.dml.DMLPara;

/**K：字段名，V：值，M：查询方式，N：亦或
 * 例如  and name like 'tank' or age = 18
 * k1=name,k2=age,v1='tank',v2=18,m1=like,m2='=',n1=and,n2=or
 * **/
public class SelectPara<K, V, M, N> implements Serializable,DMLPara{
    private static final long serialVersionUID = "SelectPara".hashCode();
    private Vector<K> k = new Vector<K>();
    private Vector<V> v = new Vector<V>();
    private Vector<M> m = new Vector<M>();
    private Vector<N> n = new Vector<N>();
    private int current_index = 0;
    
    @SuppressWarnings("rawtypes")
    public synchronized SelectPara put(K k,V v,M m,N n){
        this.k.add(k);
        this.v.add(v);
        this.m.add(m);
        this.n.add(n);
        return this;
    };
    /**取出一条数据**/
    public synchronized SelectParaItem get(){
        SelectParaItem p = null;
        List<Object> list = null;
        if(sizeNotEqual()){
            throw new RuntimeException("存储结构出错");
        }
        if(current_index <= k.size()){
            list = new ArrayList<Object>();
            list.add(k.get(current_index));
            list.add(v.get(current_index));
            list.add(m.get(current_index));
            list.add(n.get(current_index));
            current_index++;
            p = new SelectParaItem(list);
        }
        return p;
    };
    
    public boolean hasNext(){
        boolean result = false;
        if(sizeNotEqual()){
            throw new RuntimeException("存储结构出错");
        }
        if(current_index < k.size()){
            result = true;
        }
        return result;
    }
    
    public int size(){
        if(sizeNotEqual()){
            throw new RuntimeException("存储结构出错");
        }
        return k.size();
    };
    
    
    private boolean sizeNotEqual(){
        boolean result = false;
        int ksize = k.size();
        int vsize = v.size();
        int msize = m.size();
        int nsize = n.size();
        if(ksize != vsize || vsize != msize || msize != nsize){
            result = true;
        }
        return result;
    }
    
    
}

