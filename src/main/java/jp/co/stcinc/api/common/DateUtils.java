package jp.co.stcinc.api.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日付ユーティリティクラス
 */
public class DateUtils {
    
    /**
     * 現在日時を取得する
     * @return 現在日時
     */
    public static Date getNowDate() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar c = Calendar.getInstance(tz);
        
        return c.getTime();
    }

    /**
     * 指定時間後の日時を取得する
     * @param hour 加算する時間
     * @param min 加算する分
     * @param sec 加算する秒
     * @return 指定時間後の日時 
     */
    public static Date getAddDate(int hour, int min, int sec) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar c = Calendar.getInstance(tz);
        c.add(Calendar.HOUR, hour);
        c.add(Calendar.MINUTE, min);
        c.add(Calendar.SECOND, sec);
        
        return c.getTime();
        
    }
    
    /**
     * 現在日時の文字列を取得する
     * @param format 日付の書式
     * @return 現在日時の文字列
     */
    public static String getNowDateString(String format) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar c = Calendar.getInstance(tz);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        
        return sdf.format(c.getTime());
    }
    
    /**
     * 指定時間後の日時の文字列を取得する
     * @param format 日付の書式
     * @param hour 加算する時間
     * @param min 加算する分
     * @param sec 加算する秒
     * @return 指定時間後の日時の文字列
     */
    public static String getAddDateString(String format, int hour, int min, int sec) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar c = Calendar.getInstance(tz);
        c.add(Calendar.HOUR, hour);
        c.add(Calendar.MINUTE, min);
        c.add(Calendar.SECOND, sec);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        
        return sdf.format(c.getTime());
    }

    /**
     * 日付として正しいかをチェックする
     * @param value チェックする日付
     * @param format 日付の書式
     * @return チェック結果
     */
    public static boolean checkDateFormat(String value, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            sdf.setLenient(false);
            sdf.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    } 
    
    /**
     * 文字列型を日付型に変換する
     * @param value 変換する文字列
     * @param format 日付の書式
     * @return 変換された日付
     */
    public static Date stringToDate(String value, String format) {
        if (value == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            sdf.setLenient(false);
            Date dt = sdf.parse(value);
            return dt;
        } catch (ParseException e) {
            return null; 
        }
    }
    
    /**
     * 日付型を文字列型に変換する
     * @param value 変換する日付
     * @param format 日付の書式
     * @return 変換された文字列
     */
    public static String dateToString(Date value, String format) {
        if (value == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(value);
    }
}
