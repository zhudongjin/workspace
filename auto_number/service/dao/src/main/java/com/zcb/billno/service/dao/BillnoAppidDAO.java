package com.zcb.billno.service.dao;

import com.zcb.billno.service.dao.model.BillnoAppidDO;

import dbox.slite.db.annotation.AutoGenDao;

@AutoGenDao
public interface BillnoAppidDAO {

	public BillnoAppidDO queryBillnoAppid(String appid);
	
}
