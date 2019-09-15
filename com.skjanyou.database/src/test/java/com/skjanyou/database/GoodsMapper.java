package com.skjanyou.database;

import java.util.List;
interface GoodsMapper {
	public List<Goods> selectAllGoods() throws Exception;
	public List<Goods> selectRecommendGoods() throws Exception;
}