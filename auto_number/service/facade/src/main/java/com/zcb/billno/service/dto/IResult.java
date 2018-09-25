package com.zcb.billno.service.dto;

import java.io.Serializable;

public abstract interface IResult extends Serializable {
    public static final String SUCCESS_CODE = "0";
    public static final String SUCCESS_INFO = "ok";

    public abstract String getRetcode();

    public abstract String getRetmsg();

    public abstract void setRetcode(String paramString);

    public abstract void setRetmsg(String paramString);
}
