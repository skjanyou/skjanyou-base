package com.skjanyou.database.bean;

import java.util.ArrayList;
import java.util.List;

public class DBConfig {
	private List<String> typeAliasRegistryList = new ArrayList<String>();
	private List<String> mapperRegistry = new ArrayList<String>();
	private boolean autoCommit = false;
	
	public List<String> getTypeAliasRegistryList() {
		return typeAliasRegistryList;
	}
	public void setTypeAliasRegistryList(List<String> typeAliasRegistryList) {
		this.typeAliasRegistryList = typeAliasRegistryList;
	}
	public List<String> getMapperRegistry() {
		return mapperRegistry;
	}
	public void setMapperRegistry(List<String> mapperRegistry) {
		this.mapperRegistry = mapperRegistry;
	}
	public void addAliasRegistry(String alias){
		this.typeAliasRegistryList.add(alias);
	}
	public void addMapperRegistry(String mapper){
		this.mapperRegistry.add(mapper);
	}
	public boolean isAutoCommit() {
		return autoCommit;
	}
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}
}
