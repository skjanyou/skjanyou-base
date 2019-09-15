package com.skjanyou.database;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

public class Goods {
	private String goodCode;
	private String barCode;
	private String nsUrl;
	private String categoryTitle;
	private String title;
	private String remark;
	private Integer reserve;
	private Integer packge;
	private BigDecimal price;
	private String dicount;
	private BigDecimal discountPrice;
	private Integer carCount;
	public String getGoodCode() {
		return goodCode;
	}
	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getNsUrl() {
		return nsUrl;
	}
	public void setNsUrl(String nsUrl) {
		this.nsUrl = nsUrl;
	}
	public String getCategoryTitle() {
		return categoryTitle;
	}
	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getReserve() {
		return reserve;
	}
	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}
	public Integer getPackge() {
		return packge;
	}
	public void setPackge(Integer packge) {
		this.packge = packge;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getDicount() {
		return dicount;
	}
	public void setDicount(String dicount) {
		this.dicount = dicount;
	}
	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}
	public Integer getCarCount() {
		return carCount;
	}
	public void setCarCount(Integer carCount) {
		this.carCount = carCount;
	}
	
}