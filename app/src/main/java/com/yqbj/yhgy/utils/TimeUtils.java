package com.yqbj.yhgy.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 */
public class TimeUtils {
    public static final String TIME_TYPE_01 = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_TYPE_02 = "yyyy-MM-dd";
    public static final String TIME_TYPE_03 = "yyyy年MM月dd日";
    public static final String TIME_TYPE_04 = "yyyy年MM月dd日 HH时mm分";
    public static final String TIME_TYPE_05 = "yyyy-MM-dd HH:mm";
    public static final String TIME_TYPE_06 = "MM-dd HH:mm:ss:SSS";
    public static final String TIME_TYPE_07 = "yyyy.MM.dd";
    public static final String TIME_TYPE_08 = "MM-dd HH:mm";

    /**
     * 时间戳转换成字符窜
     */
    public static String getDateToString(long time, String type) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(type);
        return sf.format(d);
    }

    /**
     * 将字符串转为时间戳
     */
    public static long getStringToDate(String time) {
        return getStringToDate(time, TIME_TYPE_01);
    }

    public static long getStringToDate(String time, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 时间间隔计算
     *
     * @param time 时间戳 10位
     * @return
     */
    public static String getInterval(long time) {

        String interval = "";

        //间隔时间（秒）
        long betweenTime = (System.currentTimeMillis() - time * 1000) / 1000;
        if (betweenTime < 0) {
            interval = "刚刚";
        } else if (betweenTime >= 0 && betweenTime < 10) {
            interval = "刚刚";
        } else if (betweenTime >= 10 && betweenTime < 60) {
            interval = "刚刚";
        } else if (betweenTime >= 60 && betweenTime < 3600) {
            interval = betweenTime / 60 + "分钟前";
        } else if (betweenTime >= 3600 && betweenTime < 3600 * 24) {
            interval = betweenTime / 3600 + "小时前";
        } else if (betweenTime >= 3600 * 24 && betweenTime < 3600 * 24 * 2) {
            interval = "1天前";
        } else if (betweenTime >= 3600 * 24 * 2 && betweenTime < 3600 * 24 * 30) {
            interval = betweenTime / (3600 * 24) + "天前";
        } else if (betweenTime >= 3600 * 24 * 30) {
            interval = "30天以上";
        }
        return interval;
    }

    /**
     * 返回该月的天数
     *
     * @return
     */
    public static int getMonthCount(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);//先指定年份
        calendar.set(Calendar.MONTH, month - 1);//再指定月份 Java月份从0开始算
        return calendar.getActualMaximum(Calendar.DATE);//获取指定年份中指定月份有几天
    }


    /**
     * 获取系统时间戳
     *
     * @return
     */
    public static String getCurrentTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;
    }


    public static int getDay(String weekday) {
        int day = 0;
        if ("星期日".equals(weekday)) {
            day = 1;
        } else if ("星期一".equals(weekday)) {
            day = 2;
        } else if ("星期二".equals(weekday)) {
            day = 3;
        } else if ("星期三".equals(weekday)) {
            day = 4;
        } else if ("星期四".equals(weekday)) {
            day = 5;
        } else if ("星期五".equals(weekday)) {
            day = 6;
        } else if ("星期六".equals(weekday)) {
            day = 7;
        }
        return day;
    }

    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    public static String timediff(long beginTime,long endTime){

        if (beginTime == endTime){
            return "1秒";
        }
        int diff = (int) (beginTime - endTime) / 1000;
        StringBuilder sb = new StringBuilder();
        //计算天数
        int day = diff / 86400;
        if (day > 0){
            sb.append(day + "天");
        }
        //计算小时数
        int hour = diff % 86400 / 3600;
        if (hour > 0){
            sb.append(hour + "小时");
        }
        //计算分钟数
        int min = diff % 3600 / 60;
        if (min > 0){
            sb.append(min + "分钟");
        }
        //计算秒数
        int sec = diff % 60;
        if (sec > 0){
            sb.append(sec + "秒");
        }
        if (sec == 0){
            sb.append("1秒");
        }

        return sb.toString();
    }

    //将List按照时间倒序排列
    @SuppressLint("SimpleDateFormat")
    public static List<String> invertOrderList(List<String> dataList){
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date d1;
      Date d2;
      String temp_r;
      //做一个冒泡排序，大的在数组的前列
      for(int i=0; i<dataList.size()-1; i++){
          for(int j=i+1; j<dataList.size();j++){
              ParsePosition pos1 = new ParsePosition(0);
              ParsePosition pos2 = new ParsePosition(0);
              d1 = sdf.parse(dataList.get(i), pos1);
              d2 = sdf.parse(dataList.get(j), pos2);
              if(d1.before(d2)){//如果队前日期靠前，调换顺序
                  temp_r = dataList.get(i);
                  dataList.set(i, dataList.get(j));
                  dataList.set(j, temp_r);
              }
          }
      }
      return dataList;
    }

    // 根据年月日计算年龄,birthTimeString:"1994-11-14"
    public static int getAgeFromBirthTime(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_TYPE_02);
        Date date = new Date();
        try {
            date = sdf.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 得到当前时间的年、月、日
        if (date!=null){
            Calendar cal = Calendar.getInstance();
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayNow = cal.get(Calendar.DATE);
            //得到输入时间的年，月，日
            cal.setTime(date);
            int selectYear = cal.get(Calendar.YEAR);
            int selectMonth = cal.get(Calendar.MONTH) + 1;
            int selectDay =cal.get(Calendar.DATE);
            // 用当前年月日减去生日年月日
            int yearMinus = yearNow - selectYear;
            int monthMinus = monthNow - selectMonth;
            int dayMinus = dayNow - selectDay;
            int age = yearMinus;// 先大致赋值
            if (yearMinus <=0) {
                age = 0;
            }if (monthMinus < 0) {
                age=age-1;
            } else if (monthMinus == 0) {
                if (dayMinus < 0) {
                    age=age-1;
                }
            }
            return age;
        }
        return 0;
    }

    public static void main(String args[]){
        List<Double> numList = new ArrayList<Double>();
        numList.add(12.05);
        numList.add(33.35);
        numList.add(11.85);
        numList.add(55.98);
        numList.add(66.57);

        Double maxNum = Collections.max(numList);
        Double minNum = Collections.min(numList);
        System.out.println(">>>>>>>>>>>>" + numList.toString());
        System.out.println("最大值："+maxNum+" 索引：" + numList.indexOf(maxNum));
        System.out.println("最小值："+minNum+" 索引：" + numList.indexOf(minNum));

    }

}
