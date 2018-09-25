package com.zcb.billno.service.dto;

public class BillNo extends ServiceResult {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8736444046284466245L;
	//根据格式生成的单号
	private String billNo;
	//生成数字
	private String seqNo;
	
	//machine no
	private String macNo;
	
	public BillNo() {
	}

	public BillNo(String retCode, String retMsg) {
		super(retCode, retMsg);
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getMacNo() {
		return macNo;
	}

	public void setMacNo(String macNo) {
		this.macNo = macNo;
	}
}
