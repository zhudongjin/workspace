package com.zcb.billno.service.exception;

public class ServiceException extends ErrorCodeException {
    private static final long serialVersionUID = -4354298356788430223L;

    public ServiceException(String errorCode, String errorInfo)
    {
        super(errorCode, errorInfo);
    }

    public ServiceException(String errorCode, String errorInfo, Throwable e)
    {
        super(errorCode, errorInfo, e);
    }

    public ServiceException(String errorCode, Throwable e)
    {
        super(errorCode, e);
    }
}
