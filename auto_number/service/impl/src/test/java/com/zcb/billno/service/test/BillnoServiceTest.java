package com.zcb.billno.service.test;


import java.net.MalformedURLException;

import org.junit.Test;

import com.caucho.hessian.client.HessianProxyFactory;
import com.zcb.billno.service.dto.BillnoInput;
import com.zcb.billno.service.facade.BillnoFacade;
import com.zcb.billno.service.type.ListidPattern;
import com.zcb.billno.service.type.ListidType;

import dbox.tbox.lang.ObjUtils;
import dbox.tbox.type.ServiceResult;

public class BillnoServiceTest {
	private static String url = "http://localhost:8080/billno-service-war/services/billnoFacade.hs";
	//private static String url = "http://192.168.1.14:9080/billno-service/services/billnoFacade.hs";
	private static BillnoFacade billnoFacade;
	public static BillnoFacade getFacadeBean() throws MalformedURLException,
			ClassNotFoundException {
		if (billnoFacade != null)
			return billnoFacade;
		
		HessianProxyFactory hpf = new HessianProxyFactory();
		billnoFacade = (BillnoFacade) hpf.create(url);

		return billnoFacade;
	}

	
	public static ServiceResult genBillNo(BillnoFacade billnoFacade) throws Exception {
		BillnoInput input = new BillnoInput();
		input.setAppid("101");
		input.setListType(ListidType.C2C);
		input.setPattern(ListidPattern.DATE_SEQNO);
		
		input.setSpid("1000000000");
		
		return billnoFacade.genBillNo(input);
	}
	
	@Test
	public void testGenBillNo() {
		try {
			ServiceResult result = genBillNo(getFacadeBean());

			if (result != null)
				System.out.println(ObjUtils.obj2String(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
