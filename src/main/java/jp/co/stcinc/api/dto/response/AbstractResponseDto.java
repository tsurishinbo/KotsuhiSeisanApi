package jp.co.stcinc.api.dto.response;

import javax.xml.bind.annotation.XmlRootElement;
import jp.co.stcinc.api.common.Constants;
import lombok.Getter;
import lombok.Setter;

/**
 * レスポンス用DTO基底クラス
 */
@XmlRootElement
public abstract class AbstractResponseDto {

    // 処理結果
    @Getter @Setter
    protected Integer result;
    
    // エラーコード
    @Getter @Setter
    protected String err_code;
    
    // エラーメッセージ
    @Getter @Setter
    protected String err_message;

    /**
     * 正常レスポンス設定
     */
    public void SetSuccess() {
        result = Constants.RESULT_OK;
        err_code = null;
        err_message = null;
    }

    /**
     * エラー設定（システム例外）
     */
    public void SetErrorSystemFailed() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERRCOD_SYSTEM_FAILED;
        err_message = Constants.ERRMSG_SYSTEM_FAILED;
    }

    /**
     * エラー設定（トークン不正）
     */
    public void SetErrorInvalidToken() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERRCOD_INVALID_TOKEN;
        err_message = Constants.ERRMSG_INVALID_TOKEN;
    }

    /**
     * エラー設定（パラメータ不正）
     */    
    public void SetErrorInvalidParam() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERRCOD_INVALID_PARAM;
        err_message = Constants.ERRMSG_INVALID_PARAM;
    }

    /**
     * エラー設定（認証失敗）
     */
    public void SetErrorAuthFailed() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERRCOD_AUTH_FAILED;
        err_message = Constants.ERRMSG_AUTH_FAILED;
    }

    /**
     * エラー設定（該当トークンなし）
     */
    public void SetErrorNotfoundToken() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERRCOD_NOTFOUND_TOKEN;
        err_message = Constants.ERRMSG_NOTFOUND_TOKEN;
    }

    /**
     * エラー設定（コード不正）
     */
    public void SetErrorInvalidCode() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERRCOD_INVALID_CODE;
        err_message = Constants.ERRMSG_INVALID_CODE;
    }
    
    /**
     * エラー設定（該当申請なし）
     */
    public void SetErrorNotfoundApplication() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERRCOD_NOTFOUND_APPLICATION;
        err_message = Constants.ERRMSG_NOTFOUND_APPLICATION;
    }
    
    /**
     * エラー設定（削除不可ステータス）
     */
    public void SetErrorCannotDelete() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERRCOD_CANNOT_DELETE;
        err_message = Constants.ERRMSG_CANNOT_DELETE;
    }
    
}
