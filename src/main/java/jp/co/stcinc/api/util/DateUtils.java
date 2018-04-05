package jp.co.stcinc.api.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 日付ユーティリティクラス
 */
public class DateUtils {
    
    /**
     * 現在日時を取得する
     * @return 現在日次(yyyyMMddHHmmss)
     */
    public static String getNowDate() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar c = Calendar.getInstance(tz);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        
        return sdf.format(c.getTime());
    }
    
    /**
     * 指定時間後の日時を取得する
     * @param hour 加算する時間
     * @return 指定時間後の日時(yyyyMMddHHmmss)
     */
    public static String getAddHourDate(int hour) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar c = Calendar.getInstance(tz);
        c.add(Calendar.HOUR, hour);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        
        return sdf.format(c.getTime());
    }
    
}
