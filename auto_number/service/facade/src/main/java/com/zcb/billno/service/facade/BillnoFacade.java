package com.zcb.billno.service.facade;

import javax.jws.WebService;

import com.zcb.billno.service.dto.BillNo;
import com.zcb.billno.service.dto.BillnoInput;

//import com.hstypay.sandbox.annotation.HessianService;

/**
 * 获取订单号服务的接口
 * @author tinnfy
 *
 */
@WebService
//@HessianService
public interface BillnoFacade {

	/**
	 * 生成订单号
	 * 
	 * @param billInput
	 * @return
	 */
	public BillNo genBillNo(BillnoInput billInput);

}
