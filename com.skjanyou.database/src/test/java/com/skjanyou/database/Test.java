package com.skjanyou.database;

import com.skjanyou.database.bean.DBConfig;
import com.skjanyou.database.dao.DefaultDao;
import com.skjanyou.database.dao.SkjanyouDao;
import com.skjanyou.database.util.DbUtil;

public class Test {
	public static void main(String[] args) throws Exception {
		DBConfig dbConfig = DbUtil.getConfig();
		dbConfig.addAliasRegistry("com.skjanyou.database");
		dbConfig.addMapperRegistry("com.skjanyou.database");
		
		SkjanyouDao<Goods> dao = new DefaultDao<Goods>() {
			
		};
		
		dao.query(new Goods());
		GoodsMapper gm = DbUtil.getMapper(GoodsMapper.class);
		gm.selectAllGoods();
	}
}
