package com.zcb.billno.service;

public abstract interface IError {
    public static final String SUCC_CODE = "0";
    public static final String SUCC_INFO = "ok";

    public abstract String getErrorCode();

    public abstract String getErrorInfo();
}
