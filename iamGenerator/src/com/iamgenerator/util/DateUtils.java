package com.iamgenerator.util;

import static java.lang.System.out;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期帮助工具类
 * @author kado
 */
public class DateUtils {
    public static String getNow()
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        Date date = new Date();
        return df.format(date);
    }
    
    public static String getJustDate()
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = new Date();
        return df.format(date);
    }
//    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//    Date d1 = df.parse("2004-03-26 13:31:40");
//    Date d2 = df.parse("2004-01-02 11:30:24");
//    long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
//    long days = diff / (1000 * 60 * 60 * 24);
//    long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
//    long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
//    System.out.println(""+days+"天"+hours+"小时"+minutes+"分"); 
    
    public static long getMiniutes(Date starDate, Date endDate)
    {
      long miniutesNum = 0L;
      long secondesNum = getSecondes(starDate, endDate);
      miniutesNum = secondesNum / 60000L;
      return miniutesNum;
    }

    public static long getSecondes(Date starDate, Date endDate)
    {
      long secondesNum = 0L;
      secondesNum = endDate.getTime() - starDate.getTime();
      return secondesNum;
    }

    public static String getNowDate()
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date dt = new Date();
        String  returnTime ="";
        try {
                returnTime = sdf.format((dt));
        } catch (Exception e) {
                e.printStackTrace();
        }
        return returnTime;
    }

    public static String getDate(int jumpDay)
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
          Calendar   cal   =   Calendar.getInstance();
          cal.add(Calendar.DATE, jumpDay);
        String  returnTime ="";
        try {
                returnTime = sdf.format((cal.getTime()));
        } catch (Exception e) {
                e.printStackTrace();
        }
        return returnTime;
    }

    public static String formatDate(String Date){
        if(Date!=null&&Date.length()==10&&Date.indexOf('-')>0)
        {
                String year = Date.substring(0, 4);
                String month = Date.substring(5,7);
                String day = Date.substring(8, 10);
                return year+month+day;
        }
        return Date;
    }

//    public static void main(String[] args) {
//        DateUtils.getNowDate();
//        System.out.println(DateUtils.getNowDate());
//        Math.random();
//        System.out.println(Math.random()*1000000);
//    }
}
