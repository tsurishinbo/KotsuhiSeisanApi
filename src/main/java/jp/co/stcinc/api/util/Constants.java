package jp.co.stcinc.api.util;

/**
 *
 * @author kageyamay
 */
public final class Constants {
    
    /**
     * 処理結果
     */
    public static final Integer RESULT_OK =  0;     // 正常終了
    public static final Integer RESULT_NG = -1;     // 異常終了
    
    /**
     * 往復フラグ
     */
    public static final Integer ROUNDTRIP_ON  = 1;  // 往復
    public static final Integer ROUNDTRIP_OFF = 0;  // 片道

    /**
     * ステータス
     */
    public static final Integer STS_NOAPPLY  = 1;   // 未申請
    public static final Integer STS_WAIT     = 2;   // 承認待ち
    public static final Integer STS_COMPLETE = 3;   // 承認済
    public static final Integer STS_RETURN   = 4;   // 差戻し

    /**
     * エラーコード
     */
    public static final String ERR_COD_PARAM   = "E001";
    public static final String ERR_COD_AUTH    = "E002";
    public static final String ERR_COD_ALREADY = "E003";
    
    public static final String ERR_COD_SYSTEM  = "E999";
    
    /**
     * エラーメッセージ
     */
    public static final String ERR_MSG_PARAM   = "Invalid parameter";
    public static final String ERR_MSG_AUTH    = "Authentication failed";
    public static final String ERR_MSG_ALREADY = "Already authenticated";

    public static final String ERR_MSG_SYSTEM  = "System failed";


}
