package com.yuanqi.hangzhou.imhookup.utils;

import android.content.Context;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by yujunlong on 2016/12/16.
 */

public class NumberUtil {
    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    public static int toInt2(Object obj) {
        if (obj == null)
            return 0;
        try {
            return ((int) obj);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static Double toDouble(Object obj) {
        if (obj == null)
            return 0.00;
        return toDouble(obj.toString(), 0.00);
    }

    public static Double toDouble(String str, Double defValue) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getXiaoShu(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        String s = df.format(d);
        return s;
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * 两个String类型比较大小
     * value1 大于 value2  返回true
     * 若两个值任何一个为null或“”    均返回为true
     */
    public static boolean compareGreater(String value1, String value2) {
        if (StringUtil.isNotEmpty(value1) && StringUtil.isNotEmpty(value2)) {
            if (new BigDecimal(value1).compareTo(new BigDecimal(value2)) > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 两个String类型比较大小
     * value1 小于 value2  返回true
     * 若两个值任何一个为null或“”    均返回为true
     */
    public static boolean compareLess(String value1, String value2) {
        if (StringUtil.isNotEmpty(value1) && StringUtil.isNotEmpty(value2)) {
            if (new BigDecimal(value1).compareTo(new BigDecimal(value2)) < 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 两个String类型比较大小
     * value1 等于 value2  返回true
     * 若两个值任何一个为null或“”    均返回为true
     */
    public static boolean compareEqual(String value1, String value2) {
        if (StringUtil.isNotEmpty(value1) && StringUtil.isNotEmpty(value2)) {
            if (new BigDecimal(value1).compareTo(new BigDecimal(value2)) == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 截取小数点后两位
     */
    public static String get2String(String value) {
        String ret = "0";
        try {
            ret = new BigDecimal(value).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return ret;
//        String str = "";
//        //        删除“.”后面超过2位后的数据
//        if (value.toString().contains(".")) {
//            if (value.length() - 1 - value.toString().indexOf(".") >= 2) {
//                str = (String) value.toString().subSequence(0, value.toString().indexOf(".") + 2 + 1);
//            }
//        }
//        //如果是0则赋值0.00
//        if (new BigDecimal(value).compareTo(new BigDecimal("0")) == 0){
//            str = "0.00";
//        }
////        如果 "." 在起始位置, 则起始位置自动补0
//        if (value.toString().trim().substring(0).equals(".")) {
//            str = "0" + value;
//        }
//        //如果是整数直接返回
//        if (isInteger(value)){
//            str = value;
//        }
//        return str;
    }
    /**
     * 截取小数点后几位
     * newScale：  需要截取几位 newScale 就传几
     * 直接删除后几位
     */
    public static String interceptDown(String value, int newScale){
        String str = "0";
        try {
            str = new BigDecimal(value).setScale(newScale, BigDecimal.ROUND_DOWN).toPlainString();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return str;
    }

    /**
     * 截取小数点后几位
     * newScale：  需要截取几位 newScale 就传几
     * 直接进位
     */
    public static String interceptUp(String value, int newScale){
        String str = "0";
        try {
            str = new BigDecimal(value).setScale(newScale, BigDecimal.ROUND_UP).toPlainString();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return str;
    }

    /**
     * 提供精确加法计算的add方法
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static double add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(toDouble(value1));
        BigDecimal b2 = new BigDecimal(toDouble(value2));
        return b1.add(b2).doubleValue();
    }

    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确减法运算的sub方法
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(toDouble(value1));
        BigDecimal b2 = new BigDecimal(toDouble(value2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确乘法运算的mul方法
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double mul(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(toDouble(value1));
        BigDecimal b2 = new BigDecimal(toDouble(value2));
        return b1.multiply(b2).doubleValue();
    }

    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的除法运算方法div
     * 结果不做处理直接截取
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static String div_Intercept(String value1, String value2, int scale) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_DOWN) + "";
    }

    /**
     * 提供精确的除法运算方法div
     * (四舍五入)
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static double div(String value1, String value2, int scale) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将float数字进行格式化截断
     * @param number
     * @param count
     * @return
     */
    public static double formatFloat(Double number, int count){
        BigDecimal bg = new BigDecimal(number);
        return bg.setScale(count, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * string number转换成double
     * @param strNumber
     * @return
     */
    public static double stringNumberToDouble(String strNumber){
        if (TextUtils.isEmpty(strNumber)){
            return 0;
        }

        double ret = 0;
        try {
            ret = Double.valueOf(strNumber);
        }
        catch (NumberFormatException e){
            e.printStackTrace();
        }

        return ret;
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
