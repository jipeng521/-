package com.mian.wxPay.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 设置微信二维码失效时间，并返回具体失效的时间点
     * @param expire 二维码的有效时间，单位是毫秒
     * @return
     */
    public static String getOrderExpireTime(Long expire){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        Date afterDate = new Date(now .getTime() + expire);
        return sdf.format(afterDate );
    }
}
