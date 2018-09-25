package com.zcb.billno.service.exception;


public class BillnoSvcException extends ErrorCodeException {

	private static final long serialVersionUID = -5296524740798554632L;

	public BillnoSvcException(String retcode, Throwable e) {
		super(retcode, e);
	}
	
	public BillnoSvcException(String retcode, String retmsg) {
		
		super(retcode, retmsg);
	}
}
