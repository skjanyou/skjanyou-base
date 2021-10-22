package com.skjanyou.util.plus.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapCache<K,V> implements ICache<K,V>{
	private Map<K,V> mapCache = new HashMap<>();
	private Map<K,Long> timeoutMap = new HashMap<>();
	
	@Override
	public ICache<K, V> clear() {
		this.mapCache.clear();
		return this;
	}

	@Override
	public ICache<K, V> put(K key, V value) {
		this.mapCache.put(key, value);
		return this;
	}

	@Override
	public ICache<K, V> put(K key, V value, long timeout) {
		this.mapCache.put(key, value);
		this.timeoutMap.put(key, timeout + System.currentTimeMillis());
		return this;
	}
	
	@Override
	public ICache<K,V> remove( K key ) {
		this.mapCache.remove(key);
		this.timeoutMap.remove(key);
		return this;
	}

	@Override
	public V get(K key) {
		V value = this.mapCache.get(key);
		// 检查是否过期
		if( this.timeoutMap.get(key) > System.currentTimeMillis() ) {
			this.timeoutMap.remove(key);
			this.mapCache.remove(key);
			value = null;
		}
		
		return value;
	}

	@Override
	public Map<K, V> getCacheMap() {
		this.refresh();
		return this.mapCache;
	}

	/** 清理无用/过期缓存 **/
	@Override
	public ICache<K, V> refresh() {
		Iterator<Entry<K,Long>> it = this.timeoutMap.entrySet().iterator();
		while( it.hasNext() ) {
			Entry<K,Long> entry = it.next();
			K key = entry.getKey();
			Long timeout = entry.getValue();
			if( timeout > System.currentTimeMillis() ) {
				this.timeoutMap.remove(key);
				this.mapCache.remove(key);
			}
		}
		
		return this;
	}

}
