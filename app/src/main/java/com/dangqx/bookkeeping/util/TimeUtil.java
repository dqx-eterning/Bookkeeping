package com.dangqx.bookkeeping.util;

import com.dangqx.bookkeeping.CostAdapter;
import com.dangqx.bookkeeping.db.Cost;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by dang on 2020-12-18.
 * Time will tell.
 *
 * @description
 */
public class TimeUtil {
    /**
     * 获取某一天所在周的周一
     */
    public Date findFirstInWeek(){
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
            Calendar cal = Calendar.getInstance();
            Date time=sdf.parse(getTimes());
            cal.setTime(time);
            //System.out.println("要计算日期为:"+sdf.format(cal.getTime())); //输出要计算日期
            //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
            if(1 == dayWeek) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            //本周周一
            Date date1 = cal.getTime();
            //System.out.println("所在周星期一的日期："+sdf.format(cal.getTime()));
            //System.out.println(cal.getFirstDayOfWeek()+"-"+day+"+6="+(cal.getFirstDayOfWeek()-day+6));
            //cal.add(Calendar.DATE, 6);
            return date1;
            /*//本周末
            Date date2 = cal.getTime();
            System.out.println("所在周星期日的日期："+sdf.format(cal.getTime()));*/

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 获取某一天所在周的周末
     */
   public Date findLastInWeek(){
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
            Calendar cal = Calendar.getInstance();
            Date time=sdf.parse(getTimes());
            cal.setTime(time);
            //System.out.println("要计算日期为:"+sdf.format(cal.getTime())); //输出要计算日期
            //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
            if(1 == dayWeek) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            //本周周一
            //Date date1 = cal.getTime();
            //System.out.println("所在周星期一的日期："+sdf.format(cal.getTime()));
            //System.out.println(cal.getFirstDayOfWeek()+"-"+day+"+6="+(cal.getFirstDayOfWeek()-day+6));
            cal.add(Calendar.DATE, 6);
            //本周末
            Date date2 = cal.getTime();
            //System.out.println("所在周星期日的日期："+sdf.format(cal.getTime()));
            return date2;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 获取系统时间方法
     * @return
     */
    public String getTimes() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH)+1);
        String day = String.valueOf(cal.get(Calendar.DATE));
        System.out.println(year + "-" + month + "-" + day);
        return  year + "-" + month + "-" + day;
    }

    /**
     * string转Date方法
     * @param date
     * @return
     */
    public Date stringToDate(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * date转string方法
     * @param date
     * @return
     */
    public String dateToString(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

}
