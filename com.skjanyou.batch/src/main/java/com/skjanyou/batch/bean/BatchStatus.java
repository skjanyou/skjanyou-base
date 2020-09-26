package com.skjanyou.batch.bean;

public enum BatchStatus {
	STOP(0,"返回","虽然批次处理抛出异常,但是算是成功处理"),
	ERROR(-1,"失败","批次处理失败,需要回滚事务,并中断批量处理"),
	CONTINUE(-2,"跳过","批次处理失败,不需要中断");
	private int level;
	private String text;
	private String remark;
	
	BatchStatus( int level, String text, String remark ){
		this.level = level;
		this.text = text;
		this.remark = remark;
	}

	public int getLevel() {
		return level;
	}

	public String getText() {
		return text;
	}

	public String getRemark() {
		return remark;
	}
}
