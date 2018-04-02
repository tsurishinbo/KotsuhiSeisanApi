package jp.co.stcinc.api.util;

/**
 *
 * @author kageyamay
 */
public final class Constants {
    
    /**
     * 処理結果
     */
    public static final Integer RESULT_OK =  0; // 正常終了
    public static final Integer RESULT_NG = -1; // 異常終了
    
    /**
     * 往復フラグ
     */
    public static final String ROUNDTRIP_ON  = "1"; // 往復
    public static final String ROUNDTRIP_OFF = "0"; // 片道

    /**
     * ステータス
     */
    public static final String STS_NOAPPLY  = "1";  // 未申請
    public static final String STS_WAIT     = "2";  // 承認待ち
    public static final String STS_COMPLETE = "3";  // 承認済
    public static final String STS_RETURN   = "4";  // 差戻し


}
