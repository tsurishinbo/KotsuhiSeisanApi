package jp.co.stcinc.api.dto;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 認証解除APIリクエスト用DTO
 */
@XmlRootElement
public class ReleaseRequestDto {

    // 社員番号
    @Getter @Setter
    private String emp_no;
    
    // トークン
    @Getter @Setter
    private String token;

    /**
     * パラメータチェック
     * @return チェック結果
     */
    public boolean checkParam() {
        
        // 社員番号
        if (StringUtils.isEmpty(emp_no)) {
            return false;
        }
        if (!StringUtils.isNumeric(emp_no)) {
            return false;
        }
        // トークン
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        
        return true;
    }
}
