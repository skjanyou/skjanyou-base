package com.skjanyou.util.plus.cache;

import java.util.Map;

public interface ICache<K,V> {
	/** 清空缓存 **/
	public ICache<K,V> clear();
	/** 添加缓存key:value **/
	public ICache<K,V> put( K key, V value );
	/** 添加缓存key:value,超时时间为timeout,单位豪秒 **/
	public ICache<K,V> put( K key, V value, long timeout );
	/** 移除缓存key **/
	public ICache<K,V> remove( K key );
	/** 通过key获取缓存 **/
	public V get( K key );
	/** 获取所有的缓存数据 **/
	public Map<K,V> getCacheMap();
	/** 刷新缓存 **/
	public ICache<K,V> refresh();
}
