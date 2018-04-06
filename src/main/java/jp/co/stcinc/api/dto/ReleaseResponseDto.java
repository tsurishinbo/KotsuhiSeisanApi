package jp.co.stcinc.api.dto;

import javax.xml.bind.annotation.XmlRootElement;
import jp.co.stcinc.api.util.Constants;
import lombok.Getter;
import lombok.Setter;

/**
 * 認証解除APIレスポンス用DTO
 */
@XmlRootElement
public class ReleaseResponseDto {

    // 処理結果
    @Getter @Setter
    private Integer result;
    
    // エラーコード
    @Getter @Setter
    private String err_code;
    
    // エラーメッセージ
    @Getter @Setter
    private String err_message;
 
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
     * エラー設定（認証解除失敗）
     */
    public void SetErrorRelease() {
        result = Constants.RESULT_NG;
        err_code = Constants.ERR_COD_AUTH;
        err_message = Constants.ERR_MSG_AUTH;
    }
}
