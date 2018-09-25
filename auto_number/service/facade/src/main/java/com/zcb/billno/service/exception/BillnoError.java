package com.zcb.billno.service.exception;

/**
 * 第1位1表示系统错误，2表示业务错误；
 * 错误码前2～5位是模块名，本模块为8002;
 * 后五位为具体错误
 */
public class BillnoError {
    public static final String SYSTEM_ERROR="180020000";//系统错误

	public static final String PARAMETER_INVALID = "2800200001";//参数错误
	public static final String SEQ_LIMIT_ERROR = "2800200002";//超过最大编号
	public static final String MAC_NO_ERROR = "2800200003";//机器编号错误
}
