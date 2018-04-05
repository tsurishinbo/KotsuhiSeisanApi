package jp.co.stcinc.api.dto;

import javax.xml.bind.annotation.XmlRootElement;
import jp.co.stcinc.api.util.Constants;
import lombok.Getter;
import lombok.Setter;

/**
 * 認証APIレスポンス用DTO
 */
@XmlRootElement
public class AuthResponseDto {

    // 処理結果
    @Getter @Setter
    private Integer result;
    
    // エラーコード
    @Getter @Setter
    private String err_code;
    
    // エラーメッセージ
    @Getter @Setter
    private String err_message;
    
    // トークン
    @Getter @Setter
    private String token;

    /**
     * 正常レスポンス設定
     * @param token トークン
     */
    public void SetSuccessResult(String token) {
        result = Constants.RESULT_OK;
        err_code = null;
        err_message = null;
        this.token = token;
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
}
