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
    public static final Integer STATUS_SAVE         = 0; // 未申請
    public static final Integer STATUS_WAIT_BOSS    = 1; // 上司承認待ち
    public static final Integer STATUS_WAIT_MANAGER = 2; // 管理部承認待ち
    public static final Integer STATUS_WAIT_PAYMENT = 3; // 支払待ち
    public static final Integer STATUS_PAID         = 4; // 支払済

    /**
     * エラーコード
     */
    public static final String ERRCOD_SYSTEM_FAILED        = "E001"; // システム例外
    public static final String ERRCOD_INVALID_TOKEN        = "E002"; // トークン不正
    public static final String ERRCOD_INVALID_PARAM        = "E003"; // パラメータ不正
    public static final String ERRCOD_AUTH_FAILED          = "E004"; // 認証失敗
    public static final String ERRCOD_NOTFOUND_TOKEN       = "E005"; // 該当トークンなし
    public static final String ERRCOD_INVALID_CODE         = "E006"; // コード不正
    public static final String ERRCOD_NOTFOUND_APPLICATION = "E007"; // 該当申請なし
    public static final String ERRCOD_CANNOT_DELETE        = "E008"; // 削除不可ステータス
    
    /**
     * エラーメッセージ
     */
    public static final String ERRMSG_SYSTEM_FAILED        = "System failed";         // システム例外
    public static final String ERRMSG_INVALID_TOKEN        = "Invalid token";         // トークン不正
    public static final String ERRMSG_INVALID_PARAM        = "Invalid parameter";     // パラメータ不正
    public static final String ERRMSG_AUTH_FAILED          = "Authentication failed"; // 認証失敗
    public static final String ERRMSG_NOTFOUND_TOKEN       = "Not found token";       // 該当トークンなし
    public static final String ERRMSG_INVALID_CODE         = "Invalid code";          // コード不正
    public static final String ERRMSG_NOTFOUND_APPLICATION = "Not found application"; // 該当申請なし
    public static final String ERRMSG_CANNOT_DELETE        = "Can't delete status";   // 削除不可ステータス

}
