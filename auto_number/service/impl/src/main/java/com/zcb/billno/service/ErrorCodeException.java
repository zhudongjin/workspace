package com.zcb.billno.service;

public class ErrorCodeException extends RuntimeException implements IError {
    private static final long serialVersionUID = 4038040954783731576L;
    protected String errorCode;

    public ErrorCodeException(String errorCode, String errorInfo)
    {
        super(errorInfo);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(IError errorObj)
    {
        this(errorObj.getErrorCode(), errorObj.getErrorInfo());
    }

    public ErrorCodeException(String errorCode, String errorInfo, Throwable e)
    {
        super(errorInfo, e);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(String errorCode, Throwable e)
    {
        super(e);
        this.errorCode = errorCode;
    }

    public String getErrorCode()
    {
        return this.errorCode;
    }

    public String getErrorInfo()
    {
        return getMessage();
    }

    public String toString()
    {
        return this.errorCode + ":" + getMessage();
    }
}
