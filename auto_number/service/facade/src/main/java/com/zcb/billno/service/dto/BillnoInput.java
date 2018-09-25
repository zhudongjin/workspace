package com.zcb.billno.service.dto;

import com.zcb.billno.service.type.ListidPattern;

public class BillnoInput extends ServiceInput {
	private static final long serialVersionUID = 5645936202343875139L;

	private String listType;// 类型 0仅生成10位单号 101转账单号 102提现单号 103充值单号104冻结单号 105解冻单号
	private String appid;// 应用ID
	private String spid;// 商户号
	private String dateFormat;
	private int pattern = ListidPattern.TYPE_SPID_DATE_SEQNO;
	private String prefix;//前缀，默认为null，不加前缀
	private String suffix;//后缀,默认为null，不加后缀
	private boolean placeSeqno = true;//是否占用自增序号的长度

	public String getListType() {
		return listType;
	}
	
	/**
	 * @see com.zcb.billno.service.type.ListidType
	 * @param listType
	 */
	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appId) {
		this.appid = appId;
	}

	public String getSpid() {
		return spid;
	}

	public void setSpid(String spId) {
		this.spid = spId;
	}

	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public int getPattern() {
		return pattern;
	}

	public void setPattern(int pattern) {
		this.pattern = pattern;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}
	/**
	 * 设置订单后缀，如果placeSeqno属性为true，则10位自增序号长度变短，再加上后缀
	 * 如果placeSeqno为false,直接将后缀拼到最后
	 * @param suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public boolean isPlaceSeqno() {
		return placeSeqno;
	}
	
	/**
	 * 设置后缀是否占用自增序号的长度标记，true占用,false不占用，默认值为true
	 * @param placeSeqno
	 */
	public void setPlaceSeqno(boolean placeSeqno) {
		this.placeSeqno = placeSeqno;
	}
}
