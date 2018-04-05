package jp.co.stcinc.api.dto;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 交通費申請取消APIリクエスト用DTO
 */
@XmlRootElement
public class CancelRequestDto {

    // 社員番号
    @Getter @Setter
    private String emp_no;
    
    // トークン
    @Getter @Setter
    private String token;
    
    // 申請ID
    @Getter @Setter
    private String id;

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
        // 申請ID
        if (StringUtils.isEmpty(id)) {
            return false;
        }
        if (!StringUtils.isNumeric(id)) {
            return false;
        }
        
        return true;
    }
}
