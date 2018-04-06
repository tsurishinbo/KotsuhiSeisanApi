package jp.co.stcinc.api.dto;

import javax.xml.bind.annotation.XmlRootElement;
import jp.co.stcinc.api.util.Constants;
import jp.co.stcinc.api.util.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 交通費申請取得APIリクエスト用API
 */
@XmlRootElement
public class SearchRequestDto {

    // 社員番号
    @Getter @Setter
    private String emp_no;
    
    // トークン
    @Getter @Setter
    private String token;

    // 申請日
    @Getter @Setter
    private String apply_date;
    
    // 申請者ID
    @Getter @Setter
    private String apply_id;
    
    // 承認者ID
    @Getter @Setter
    private String approve_id;
    
    // ステータス
    @Getter @Setter
    private String status;

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
        // 申請日
        if (StringUtils.isNotEmpty(apply_date)) {
            if (StringUtils.length(apply_date) != 8) {
                return false;
            }
            if (!DateUtils.checkDateFormat(apply_date, "yyyyMMddHHmmss")) {
                return false;
            }
        }
        // 申請者ID
        if (StringUtils.isNotEmpty(apply_id)) {
            if (!StringUtils.isNumeric(apply_id)) {
                return false;
            }
        }
        // 承認者ID
        if (StringUtils.isNotEmpty(approve_id)) {
            if (!StringUtils.isNumeric(approve_id)) {
                return false;
            }
        }
        // ステータス
        if (StringUtils.isNotEmpty(status)) {
            if (!Constants.STS_NOAPPLY.equals(status) &&
                !Constants.STS_WAIT.equals(status) &&
                !Constants.STS_COMPLETE.equals(status) &&
                !Constants.STS_RETURN.equals(status)) {
                return false;
            }
        }
        
        return true;
    }
}
