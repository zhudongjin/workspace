package com.zcb.billno.service.type;

/**
 * C2C 100 转账单号<br>
 * FETCH 101 提现单号<br>
 * RECHARGE 102 充值单号<br>
 * FREEZE 103 冻结单号<br>
 * UNFREEZE 104 解冻单号<br>
 * UNKNOWN 999 通用<br>
 * @author tinnfy
 *
 */
public class ListidType {
	public static final String C2C = "100";// 转账单号
	public static final String FETCH = "101";// 提现单号
	public static final String RECHARGE = "102";// 充值单号
	public static final String FREEZE = "103";// 冻结单号
	public static final String UNFREEZE = "104";// 解冻单号

	public static final String UNKNOWN = "999";// 通用单号
}
