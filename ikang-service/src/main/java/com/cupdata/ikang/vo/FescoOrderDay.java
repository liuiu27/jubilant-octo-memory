package com.cupdata.ikang.vo;

public class FescoOrderDay {

	private Long maxNum;
	private String strRegDate;
	private Long usedNum;
	/**
	 * 预约日期，yyyy-MM-dd
	 */
	public String getStrRegDate() {
		return strRegDate;
	}
	public void setStrRegDate(String strRegDate) {
		this.strRegDate = strRegDate;
	}
	/**
	 * 最大体检人数
	 */
	public Long getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(Long maxNum) {
		this.maxNum = maxNum;
	}
	/**
	 * 已预约人数
	 */
	public Long getUsedNum() {
		return usedNum;
	}
	public void setUsedNum(Long usedNum) {
		this.usedNum = usedNum;
	}
}
