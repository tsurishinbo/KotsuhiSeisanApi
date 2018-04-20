package jp.co.stcinc.api.dto.request;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 認証APIリクエスト用DTO
 */
@XmlRootElement
public class AuthRequestDto extends AbstractRequestDto {

    // 社員番号
    @Getter @Setter
    private String emp_no;
    
    // パスワード
    @Getter @Setter
    private String password;

    /**
     * パラメータチェック
     * @return チェック結果
     */
    @Override
    public boolean checkParam() {
        
        // 社員番号
        if (StringUtils.isEmpty(emp_no)) {
            return false;
        }
        if (!StringUtils.isNumeric(emp_no)) {
            return false;
        }
        // パスワード
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }
    
}
