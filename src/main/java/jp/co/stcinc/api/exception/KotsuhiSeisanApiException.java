package jp.co.stcinc.api.exception;

/**
 * 交通費精算API例外クラス
 */
public class KotsuhiSeisanApiException extends Exception {

    public static final String EXCEPTION_DB     = "Exception at database.";
    public static final String EXCEPTION_SYSTEM = "Exception at system.";
    
    private Exception ex;

    /**
     * コンストラクタ
     */
    public KotsuhiSeisanApiException() {
    }
    
    /**
     * コンストラクタ
     * @param msg メッセージ
     * @param e 例外
     */
    public KotsuhiSeisanApiException(String msg, Exception e) {
        super(msg);
        this.ex = e;
    }
    
    /**
     * 例外を取得する
     * @return 例外
     */
    public Exception getException() {
        return this.ex;
    }
}
