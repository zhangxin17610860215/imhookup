package com.yuanqi.hangzhou.imhookup.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 工程内部字符串比较统一使用此工具类。
 */
public class StringUtil {
    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static boolean isEmpty(String string) {
        if (null == string || 0 == string.trim().length())
            return true;
        else
            return false;
    }

    public static String ensureNoNull(String string) {
        return null == string ? "" : string;
    }

    public static boolean notEquals(String string1, String string2) {
        if (null == string1) {
            return string2 != null;
        } else {
            return !string1.equals(string2);
        }
    }

    public static boolean isEquals(String string1, String string2) {
        return !notEquals(string1, string2);
    }

    public static double isSimilar(String strA, String strB) {
        String newStrA = removeSign(strA);
        String newStrB = removeSign(strB);
        if (newStrA.length() < newStrB.length()) {
            String temp = newStrA;
            newStrA = newStrB;
            newStrB = temp;
        }
        return getLCSlen(newStrA, newStrB) * 1.0 / newStrA.length();
    }

    /**
     * 保留有效数字
     * */
    public static String effectiveNum(String str) {
        if (StringUtil.isNotEmpty(str)){
            BigDecimal bigDecimal = new BigDecimal(str);
            return bigDecimal.stripTrailingZeros().toPlainString();
        }else {
            return "";
        }
    }

    private static String removeSign(String str) {
        StringBuffer sb = new StringBuffer();
        for (char item : str.toCharArray()) {
            if (charReg(item)) {
                sb.append(item);
            }
        }
        return sb.toString();
    }

    private static boolean charReg(char charValue) {
        return (charValue >= 0x4E00 && charValue <= 0x9FA5) || (charValue >= 'a' && charValue <= 'z') || (charValue >= 'A' && charValue <= 'Z');
    }

    private static int getLCSlen(String strA, String strB) {
        String[] strAElement = new String[strA.length()];
        for (int i = 0; i < strA.length(); i++) {
            strAElement[i] = strA.substring(i, i + 1);
        }
        String[] strBElement = new String[strB.length()];
        for (int i = 0; i < strB.length(); i++) {
            strBElement[i] = strB.substring(i, i + 1);
        }
        int m = strAElement.length;
        int n = strBElement.length;
        int[][] matrix = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (strAElement[i - 1].equals(strBElement[j - 1])) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                } else {
                    matrix[i][j] = Math.max(Math.max(matrix[i][j - 1], matrix[i - 1][j]), matrix[i - 1][j - 1]);
                }
            }
        }
        return matrix[m][n];
    }

    /**
     * 检查邮箱的合法性
     *
     * @param email
     * @return
     */
    public static boolean checkMail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][23456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 验证用户名的合法性
     *
     * @param userName
     * @return
     */
    public static boolean isUserName(String userName) {
        String str = "^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$";//只有字母、数字和下划线且不能以下划线开头和结尾
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(userName);
        return m.matches();
    }

    /**
     * 验证是否为和非的密码串。
     * 只能包含数字和字母，且必须包含数字、字母, 6~20位
     *
     * @param strPassword
     * @return
     */
    public static boolean isPassword(String strPassword) {
        String str = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";//""/^[a-zA-Z0-9]{6,10}$/";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(strPassword);
        return m.matches();
    }

    /**
     * 对手机号中间几位替换成   *
     */
    public static String getPwdPhone(String phone) {
        String one = phone.substring(0, 3);
        String two = phone.substring(7, 11);
        return one + "****" + two;
    }

    /**
     * 对字符串后四位替换成   *
     */
    public static String getPwdstr(String str) {
        String newStr = "";
        if (isNotEmpty(str) && str.length() >= 4){
            StringBuilder sb = new StringBuilder(str);
            sb.replace(str.length()-4, str.length(),"****");
            newStr = sb.toString();
        }
        return newStr;
    }


    /**
     * 只允许字母、数字和汉字
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";//正则表达式
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 只允许输入汉字
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter1(String str) throws PatternSyntaxException {
        //只允许汉字
        String regEx = "[^\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 只允许输入数字和汉字
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter2(String str) throws PatternSyntaxException {
        //只允许数字和汉字
        String regEx = "[^0-9\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    /**
     * 过滤emoji     禁止输入表情符号
     */
    private static final byte UTF8_HIGH_MASK = (byte)0xf0;
    public static Boolean has4BytesUTF8(String str){
        if(StringUtil.isEmpty(str)){
            return true;
        }

        try {
            byte[] bytes = str.getBytes("UTF8");
            for(byte b : bytes){
                if(((byte)(b&UTF8_HIGH_MASK))==UTF8_HIGH_MASK){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }
    /**
     * 查询一个字符串中另一个字符串出现的次数
     * @param s1    完整的字符串
     * @param s2    需要查询的字符串
     * @return      count
     */
    public static int count(String s1, String s2){
        if (isEmpty(s1) || isEmpty(s2)){
            return 0;
        }
        int fromIndex = 0;
        int count = 0;
        while(true){
            int index = s1.indexOf(s2, fromIndex);
            if(-1 != index){
                fromIndex = index + 1;
                count++;
            }else{
                break;
            }
        }
        return count;
    }

    //判断是否是字母，汉字，
    // 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
    // *小写字母：[0x61,0x7a]（或十进制[97, 122]）
    //     * 大写字母：[0x41,0x5a]（或十进制[65, 90]）
    //'·'=183
    public static boolean judgeLegal(String value){
        boolean flag=true;
        for(int i=0;i<value.length();i++){
            char c= value.charAt(i);
            int ints = (int)c;
            if(!(ints>=65&&ints<=90)&&!(ints>=97&&ints<=122)&&!(ints>=19968&&ints<=40869)&&!(ints==183)){
                flag=false;
                return flag;
            }
        }
        return flag;
    }
    //仅限输入字母、数字、下划线    (微信账号的校验)
    public static boolean judgeWeiXin(String value){

        char xhx='_';
        for(int i=0;i<value.length();i++){
            char valueChar=value.charAt(i);
            if(!Character.isLetter(valueChar)&&!Character.isDigit(valueChar)&&valueChar!=xhx){

                return false;
            }
        }
        return true;
    }
    /**
     * 支付宝账号的校验,   字母、数字、常规符号（比如：@  .  _）
     * */
    public static boolean judgeZfb(String value){
        char xhx='@';
        char xhxs='.';
        char xhxss='_';
        for(int i=0;i<value.length();i++){
            char valueChar=value.charAt(i);
            if(!Character.isLetter(valueChar)&&!Character.isDigit(valueChar)&&valueChar!=xhx&&xhxs!=valueChar&&xhxss!=valueChar){

                return false;
            }
        }
        return true;
    }
    //只能输入汉字
    public static boolean judgeChinese(String value){
        for(int i=0;i<value.length();i++){
            char c= value.charAt(i);
            int ints = (int)c;
            if(!(ints>=19968&&ints<=40869)){
                return false;
            }
        }
        return true;
    }
    /**
     * 获取汉字串拼音首字母，英文字符不变
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null && temp.length > 1) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);//增加单聊群聊发红包功能中校验支付密码功能   //处理选择谁可以领红包页面只有一个用户时的异常
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 返回当前程序版本号
     */
    public static String getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode + "";
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName=null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
