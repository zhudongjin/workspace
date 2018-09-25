package com.zcb.billno.service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataFormatter {
    private static final Logger logger = LoggerFactory.getLogger(DataFormatter.class);

    public static String formatDate(Date date, String format)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        }
        catch (Throwable t)
        {
            logger.error("formatDate fail,{},{}", format, t.toString());
        }
        return "";
    }

    public static String formatDecimal(float data, String style)
    {
        try
        {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        }
        catch (Throwable t)
        {
            logger.error("formatDate fail,{},{},{}", new Object[] { Float.valueOf(data), style, t.toString() });
        }
        return "";
    }

    public static String formatDecimal(Float data, String style)
    {
        try
        {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        }
        catch (Throwable t)
        {
            logger.error("formatDate fail,{},{},{}", new Object[] { data, style, t.toString() });
        }
        return "";
    }

    public static String formatDecimal(double data, String style)
    {
        try
        {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        }
        catch (Throwable t)
        {
            logger.error("formatDate fail,{},{},{}", new Object[] { Double.valueOf(data), style, t.toString() });
        }
        return "";
    }

    public static String formatDecimal(Double data, String style)
    {
        try
        {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        }
        catch (Throwable t)
        {
            logger.error("formatDate fail,{},{},{}", new Object[] { data, style, t.toString() });
        }
        return "";
    }

    public static String formatDecimal(BigDecimal data, String style)
    {
        try
        {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        }
        catch (Throwable t)
        {
            logger.error("formatDate fail,{},{},{}", new Object[] { data, style, t.toString() });
        }
        return "";
    }

    public static String formatDecimal(long data, String style)
    {
        try
        {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        }
        catch (Throwable t)
        {
            logger.error("formatDate fail,{},{},{}", new Object[] { Long.valueOf(data), style, t.toString() });
        }
        return "";
    }

    public static String formatDecimal(Long data, String style)
    {
        try
        {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        }
        catch (Throwable t)
        {
            logger.error("formatDate fail,{},{},{}", new Object[] { data, style, t.toString() });
        }
        return "";
    }

    public static String formatDecimal(int data, String style)
    {
        try
        {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        }
        catch (Throwable t)
        {
            logger.error("formatDate fail,{},{},{}", new Object[] { Integer.valueOf(data), style, t.toString() });
        }
        return "";
    }

    public static String formatDecimal(Integer data, String style)
    {
        try
        {
            DecimalFormat df = new DecimalFormat(style);
            return df.format(data);
        }
        catch (Throwable t)
        {
            logger.error("formatDate fail,{},{},{}", new Object[] { data, style, t.toString() });
        }
        return "";
    }

    public static BigDecimal fen2yuan(long data)
    {
        try
        {
            BigDecimal money = new BigDecimal(data);
            return money.movePointLeft(2);
        }
        catch (Throwable t)
        {
            logger.error("fen2yuan fail,{},{}", Long.valueOf(data), t.toString());
        }
        return new BigDecimal("0.00");
    }

    public static BigDecimal fen2yuan(Long data)
    {
        try
        {
            if (data == null) {
                return new BigDecimal("0.00");
            }
            BigDecimal money = new BigDecimal(data.longValue());
            return money.movePointLeft(2);
        }
        catch (Throwable t)
        {
            logger.error("fen2yuan fail,{},{}", data, t.toString());
        }
        return new BigDecimal("0.00");
    }

    public static Long yuan2fen(BigDecimal data, int roundingMode)
    {
        try
        {
            if (data == null) {
                return Long.valueOf(0L);
            }
            data = data.movePointRight(2);
            data = data.setScale(0, roundingMode);
            return new Long(data.longValue());
        }
        catch (Throwable t)
        {
            logger.error("fen2yuan fail,{},{}", data, t.toString());
            throw new RuntimeException("yuan2fen convert fail", t);
        }
    }

    public static Long yuan2fen(BigDecimal data, RoundingMode roundingMode)
    {
        try
        {
            if (data == null) {
                return Long.valueOf(0L);
            }
            data = data.movePointRight(2);
            data = data.setScale(0, roundingMode);
            return new Long(data.longValue());
        }
        catch (Throwable t)
        {
            logger.error("fen2yuan fail,{},{}", data, t.toString());
            throw new RuntimeException("yuan2fen convert fail", t);
        }
    }

    public static Long yuan2fen(BigDecimal data)
    {
        try
        {
            if (data == null) {
                return Long.valueOf(0L);
            }
            data = data.movePointRight(2);
            data = data.setScale(0, 7);
            return new Long(data.longValue());
        }
        catch (Throwable t)
        {
            logger.error("fen2yuan fail,{},{}", data, t.toString());
            throw new RuntimeException("yuan2fen convert fail", t);
        }
    }

    public static String fen2yuanStr(long data)
    {
        try
        {
            BigDecimal money = new BigDecimal(data);
            money = money.movePointLeft(2);
            return money.toString();
        }
        catch (Throwable t)
        {
            logger.error("fen2yuan fail,{},{}", Long.valueOf(data), t.toString());
        }
        return "";
    }

    public static String fen2yuanStr(Long data)
    {
        if (data == null) {
            return "";
        }
        return fen2yuanStr(data.longValue());
    }

    public static String movePointLeft(BigDecimal data, int n)
    {
        try
        {
            if (data == null) {
                return "";
            }
            data = data.movePointLeft(n);
            return data.toString();
        }
        catch (Throwable t)
        {
            logger.error("movePointLeft fail,{},{}", data, t.toString());
        }
        return "";
    }

    public static String movePointLeft(long data, int n)
    {
        BigDecimal bd = new BigDecimal(data);
        return movePointLeft(bd, n);
    }

    public static String movePointLeft(Long data, int n)
    {
        BigDecimal bd = new BigDecimal(data.longValue());
        return movePointLeft(bd, n);
    }

    public static String movePointLeft(int data, int n)
    {
        BigDecimal bd = new BigDecimal(data);
        return movePointLeft(bd, n);
    }

    public static String movePointLeft(Integer data, int n)
    {
        BigDecimal bd = new BigDecimal(data.intValue());
        return movePointLeft(bd, n);
    }

    public static String movePointRight(BigDecimal data, int n)
    {
        try
        {
            if (data == null) {
                return "";
            }
            data = data.movePointRight(n);
            return data.toString();
        }
        catch (Throwable t)
        {
            logger.error("movePointRight fail,{},{}", data, t.toString());
        }
        return "";
    }

    public static String movePointRight(long data, int n)
    {
        BigDecimal bd = new BigDecimal(data);
        return movePointRight(bd, n);
    }

    public static String movePointRight(Long data, int n)
    {
        BigDecimal bd = new BigDecimal(data.longValue());
        return movePointRight(bd, n);
    }

    public static String movePointRight(int data, int n)
    {
        BigDecimal bd = new BigDecimal(data);
        return movePointRight(bd, n);
    }

    public static String movePointRight(Integer data, int n)
    {
        BigDecimal bd = new BigDecimal(data.intValue());
        return movePointRight(bd, n);
    }

    public static void main(String[] args)
    {
        System.out.println(formatDate(new Date(), "yyyy-MM-dd"));

        System.out.println(formatDecimal(100, "# 0,0"));
        System.out.println(formatDecimal(new BigDecimal(100), "# 0,0.00"));

        System.out.println(fen2yuan(1001L));
        System.out.println(fen2yuan(new Long(1023L)));

        System.out.println(yuan2fen(new BigDecimal("10.23")));
        System.out.println(yuan2fen(new BigDecimal("10.231"), 1));
        System.out.println(yuan2fen(new BigDecimal("10.235"), 1));
        System.out.println(yuan2fen(new BigDecimal("10.235"), 0));
        System.out.println(yuan2fen(new BigDecimal("10.231"), 4));

        System.out.println(fen2yuanStr(1001L));
        System.out.println(fen2yuanStr(new Long(1023L)));

        System.out.println(movePointLeft(1023L, 1));
        System.out.println(movePointLeft(new Long(1023L), 1));
        System.out.println(movePointLeft(1053, 4));
        System.out.println(movePointLeft(new Integer(1033), 4));
        System.out.println(movePointLeft(new BigDecimal("123.54"), 4));

        System.out.println(movePointRight(1023L, 1));
        System.out.println(movePointRight(new Long(1023L), 1));
        System.out.println(movePointRight(1053, 4));
        System.out.println(movePointRight(new Integer(1033), 4));
        System.out.println(movePointRight(new BigDecimal("123.222254"), 4));
    }
}
