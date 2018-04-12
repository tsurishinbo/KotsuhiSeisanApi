package jp.co.stcinc.api.common;

/**
 * 定数定義
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
    public static final Integer STATUS_SAVE      = 1;   // 未申請
    public static final Integer STATUS_WAIT      = 2;   // 承認待ち
    public static final Integer STATUS_APPROVED  = 3;   // 承認済
    public static final Integer STATUS_REJECTION = 4;   // 差戻し

    /**
     * エラーコード
     */
    public static final String ERR_COD_SYSTEM   = "E001"; // システム例外
    public static final String ERR_COD_PARAM    = "E002"; // パラメータ不正
    public static final String ERR_COD_TOKEN    = "E003"; // トークン不正
    public static final String ERR_COD_EXPIRE   = "E004"; // トークン有効期限切れ
    public static final String ERR_COD_AUTH     = "E005"; // 認証失敗
    public static final String ERR_COD_ALREADY  = "E006"; // 認証済
    public static final String ERR_COD_CODE     = "E007"; // コード不正
    public static final String ERR_COD_NOTFOUND = "E008"; // 申請なし
    public static final String ERR_COD_DELETE   = "E009"; // 申請削除不可
    
    /**
     * エラーメッセージ
     */
    public static final String ERR_MSG_SYSTEM    = "System failed";         // システム例外
    public static final String ERR_MSG_PARAM     = "Invalid parameter";     // パラメータ不正
    public static final String ERR_MSG_TOKEN     = "Invalid token";         // トークン不正
    public static final String ERR_MSG_EXPIRE    = "Expired token";         // トークン有効期限切れ
    public static final String ERR_MSG_AUTH      = "Authentication failed"; // 認証失敗
    public static final String ERR_MSG_ALREADY   = "Already authenticated"; // 認証済
    public static final String ERR_MSG_CODE      = "Invalid code";          // コード不正
    public static final String ERR_MSG_NOTFOUND  = "Not found application"; // 申請なし
    public static final String ERR_MSG_DELETE    = "Can not delete";        // 申請削除不可

}
