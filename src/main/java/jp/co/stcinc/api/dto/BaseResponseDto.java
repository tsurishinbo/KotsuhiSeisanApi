package jp.co.stcinc.api.dto;

import javax.xml.bind.annotation.XmlRootElement;
import jp.co.stcinc.api.common.Constants;
import lombok.Getter;
import lombok.Setter;

/**
 * レスポンス用DTO基底クラス
 */
@XmlRootElement
public abstract class BaseResponseDto {

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
    public void SetSuccessResult() {
        result = Constants.RESULT_OK;
        err_code = null;
        err_message = null;
    }

    /**
     * エラー設定（システム例外）
     */
    public void SetErrorSystem() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERR_COD_SYSTEM;
        err_message = Constants.ERR_MSG_SYSTEM;
    }

    /**
     * エラー設定（パラメータ不正）
     */    
    public void SetErrorParam() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERR_COD_PARAM;
        err_message = Constants.ERR_MSG_PARAM;
    }

    /**
     * エラー設定（トークン不正）
     */
    public void SetErrorToken() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERR_COD_TOKEN;
        err_message = Constants.ERR_MSG_TOKEN;
    }

    /**
     * エラー設定（トークン有効期限切れ）
     */
    public void SetErrorExpire() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERR_COD_EXPIRE;
        err_message = Constants.ERR_MSG_EXPIRE;
    }

    /**
     * エラー設定（認証失敗）
     */
    public void SetErrorAuth() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERR_COD_AUTH;
        err_message = Constants.ERR_MSG_AUTH;
    }
    
    /**
     * エラー設定（認証済）
     */
    public void SetErrorAlready() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERR_COD_ALREADY;
        err_message = Constants.ERR_MSG_ALREADY;
    }

    /**
     * エラー設定（コード不正）
     */
    public void SetErrorCode() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERR_COD_CODE;
        err_message = Constants.ERR_MSG_CODE;
    }
    
    /**
     * エラー設定（該当申請なし）
     */
    public void SetErrorNotfound() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERR_COD_NOTFOUND;
        err_message = Constants.ERR_MSG_NOTFOUND;
    }
    
    /**
     * エラー設定（申請取消不可）
     */
    public void SetErrorCancel() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERR_COD_DELETE;
        err_message = Constants.ERR_MSG_DELETE;
    }
    
}
