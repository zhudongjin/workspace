package com.zcb.billno.service.dto;

import com.zcb.billno.service.exception.ServiceException;
import com.zcb.billno.service.exception.TBoxError;

public class ServiceResult implements IResult {
    private static final long serialVersionUID = 1233576999100772651L;
    private String retcode = "0";
    private String retmsg = "ok";
    public static final ServiceResult SUCC_RESULT = new ServiceResult();

    public ServiceResult() {}

    public ServiceResult(String retCode, String retMsg)
    {
        this.retcode = retCode;
        this.retmsg = retMsg;
    }

    public String getRetcode()
    {
        return this.retcode;
    }

    public void setRetcode(String retcode)
    {
        this.retcode = retcode;
    }

    public String getRetmsg()
    {
        return this.retmsg;
    }

    public void setRetmsg(String retmsg)
    {
        this.retmsg = retmsg;
    }

    public static void checkSucc(IResult res, String... args)
    {
        if (null == res) {
            throw new ServiceException(TBoxError.ERR_SYSTEM_ERROR, "Null Result pointer");
        }
        if ("0".equals(res.getRetcode())) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if ((null != args[i]) && (args[i].equals(res.getRetcode()))) {
                return;
            }
        }
        throw new ServiceException(res.getRetcode(), res.getRetmsg());
    }

    public void checkSucc(String... args)
    {
        checkSucc(this, args);
    }

    public static boolean checkSuccSilent(IResult res, String... args)
    {
        if (null == res) {
            return false;
        }
        if ("0".equals(res.getRetcode())) {
            return true;
        }
        for (int i = 0; i < args.length; i++) {
            if ((null != args[i]) && (args[i].equals(res.getRetcode()))) {
                return true;
            }
        }
        return false;
    }

    public boolean checkSuccSilent(String... args)
    {
        return checkSuccSilent(this, args);
    }

    public String toString()
    {
        return "[" + this.retcode + ":" + this.retmsg + "]";
    }
}
