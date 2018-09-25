package com.zcb.billno.service.dto;

import java.io.Serializable;

public class ServiceInput implements Serializable {
    private static final long serialVersionUID = -5697862095018370752L;
    private String msgno;

    public String getMsgno()
    {
        return this.msgno;
    }

    public void setMsgno(String msgno)
    {
        this.msgno = msgno;
    }
}
