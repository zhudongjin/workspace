package com.zcb.billno.service.dao.model;

public class BillnoAppidDO {
	public String appid;
	public Integer startNo;
	public String memo;
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appId) {
		this.appid = appId;
	}

	public Integer getStartNo() {
		return startNo;
	}

	public void setStartNo(Integer startNo) {
		this.startNo = startNo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
