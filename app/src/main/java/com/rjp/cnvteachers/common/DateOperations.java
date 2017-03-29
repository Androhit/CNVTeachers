package com.rjp.cnvteachers.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rohit on 21/4/16.
 */
public class DateOperations
{
    static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    static SimpleDateFormat ddMMMyyyy = new SimpleDateFormat("dd-MMM-yyyy");
    static SimpleDateFormat MMMyy = new SimpleDateFormat("MMM-yy");
    static SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
    static SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
    static SimpleDateFormat mm = new SimpleDateFormat("MM");
    static SimpleDateFormat ddmmm = new SimpleDateFormat("dd-MMM");
    static SimpleDateFormat mmm = new SimpleDateFormat("MMM");
    static SimpleDateFormat dd = new SimpleDateFormat("dd");


     public static String convertToddMMMyyyy(String strDate)
     {

         Date date = null;
         try {
             date = ddMMyyyy.parse(strDate);
             //System.out.println(ddMMMyyyy.format(examType));
             strDate =  ddMMMyyyy.format(date);
         } catch (ParseException e)
         {
             e.printStackTrace();
         }

         return  strDate;
     }

    public static String convertToMMMyy(String strDate)
    {

        Date date = null;
        try {
            date = yyyyMMdd.parse(strDate);
            //System.out.println(ddMMMyyyy.format(examType));
            strDate =  MMMyy.format(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return  strDate;
    }

    public static String convertToddMMM(String strDate)
    {

        Date date = null;
        try {
            date = yyyyMMdd.parse(strDate);
            //System.out.println(ddMMMyyyy.format(examType));
            strDate =  ddmmm.format(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return  strDate;
    }

    public static String convertToyyyyMM(String strDate)
    {

        Date date = null;
        try {
            date = yyyyMMdd.parse(strDate);
            //System.out.println(ddMMMyyyy.format(examType));
            strDate =  yyyyMM.format(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return  strDate;
    }

    public static String getYear(String strDate)
    {

        Date date = null;
        try {
            date = yyyyMMdd.parse(strDate);
            //System.out.println(ddMMMyyyy.format(examType));
            strDate =  yyyy.format(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return  strDate;
    }

    public static String getMonth(String strDate)
    {

        Date date = null;
        try {
            date = yyyyMMdd.parse(strDate);
            //System.out.println(ddMMMyyyy.format(examType));
            strDate =  mm.format(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return  strDate;
    }

    public static String getMonthName(String strDate)
    {

        Date date = null;
        try {
            date = yyyyMMdd.parse(strDate);
            //System.out.println(ddMMMyyyy.format(examType));
            strDate =  mmm.format(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return  strDate;
    }

    public static String currentDateTime()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  df.format(c.getTime());
    }

    public static String getDay(String strDate)
    {

        Date date = null;
        try {
            date = yyyyMMdd.parse(strDate);
            //System.out.println(ddMMMyyyy.format(examType));
            strDate =  dd.format(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return  strDate;
    }

    public static String getTimeStamp(String dateStr) {

        Calendar calendar = Calendar.getInstance();
        String today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }
}
