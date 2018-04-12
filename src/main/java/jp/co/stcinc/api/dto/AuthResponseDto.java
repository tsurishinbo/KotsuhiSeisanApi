package jp.co.stcinc.api.dto;

import javax.xml.bind.annotation.XmlRootElement;
import jp.co.stcinc.api.common.Constants;
import lombok.Getter;
import lombok.Setter;

/**
 * 認証APIレスポンス用DTO
 */
@XmlRootElement
public class AuthResponseDto extends BaseResponseDto {

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
    
}
