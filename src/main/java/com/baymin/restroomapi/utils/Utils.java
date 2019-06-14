package com.baymin.restroomapi.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by baymin on 17-8-1.
 */
public class Utils {

    public static void main(String[] args) {
        String aa="MR7102L01RAJ";
        System.out.println(aa.substring(3,5));
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

    /**
     * @author LuoB.
     * @param oldTime 较小的时间
     * @param newTime 较大的时间 (如果为空   默认当前时间 ,表示和当前时间相比)
     * @return -1 ：同一天.    0：昨天 .   1 ：至少是前天.
     * @throws ParseException 转换异常
     */
    public static int isYeaterday(Date oldTime,Date newTime) {
        try {
            if (newTime == null) {
                newTime = new Date();
            }
            //将下面的 理解成  yyyy-MM-dd 00：00：00 更好理解点
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = format.format(newTime);
            Date today = null;

            today = format.parse(todayStr);

            //昨天 86400000=24*60*60*1000 一天
            if ((today.getTime() - oldTime.getTime()) > 0 && (today.getTime() - oldTime.getTime()) <= 86400000) {
                return 0;
            } else if ((today.getTime() - oldTime.getTime()) <= 0) { //至少是今天
                return -1;
            } else { //至少是前天
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 2;
    }

    /**
     * 判断多个参数是否都为空
     * @param str
     * @return
     */
    public static boolean stringIsNotNull(String str) {
        return !"".equals(str) || str != null;
    }



    public static int getRandom() throws PatternSyntaxException {
        return new Random().nextInt(899999) + 100000;
        //System.out.println(sj);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    public static Date addDay(Date date, Integer addDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, addDays);// 今天+1天
        return c.getTime();
    }


    public static String getDateStringNow() {
        SimpleDateFormat tempDate = new SimpleDateFormat("MM月dd日");
        return tempDate.format(new Date());
    }

    /**
     * 毫秒级别+特殊值组成订单号
     *
     * @param id 订单特殊值
     * @return
     */
    public static String getOrderId(Integer id) {
        return new SimpleDateFormat("yyyyMMddhhmmssSSSS").format(new Date()) + id;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param newest 套餐到期时间
     * @param old    一般是当前时间
     * @return
     */
    public static int differentDaysByMillisecond(Date old, Date newest) {
        int days = (int) ((newest.getTime() - old.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 用户密码+支付密码
     * SHA256加密算法
     *
     * @param password 密码
     * @param salt     盐
     * @return
     */
    public static String GenerateHashWithSalt(String password, String salt) throws Exception {
        // convert this merged value to a byte array
        byte[] saltedHashBytes = new byte[0];
        saltedHashBytes = (password + salt).getBytes("utf-8");
        MessageDigest messageDigest;
        byte[] hash = null;
        messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(saltedHashBytes);
        hash = messageDigest.digest();
        // return the has as a base 64 encoded string
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * 返回长度为【strLength】的随机数，在前面补0
     */
    public static String getFixLenthString(int strLength) {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }


    public static String sha256(String password, String salt) {
        return new SimpleHash("SHA-256", password, salt, 16).toString();
    }

    /**
     * MD5加密
     *
     * @param str 内容
     * @throws Exception
     */
    @SuppressWarnings("unused")
    public static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            byte[] result = md.digest();
            StringBuffer sb = new StringBuffer(32);
            for (int i = 0; i < result.length; i++) {
                int val = result[i] & 0xff;
                if (val <= 0xf) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString().toLowerCase();
        } catch (Exception err) {
            return "err";
        }
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date StrToDateOnlyDay(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }
}
