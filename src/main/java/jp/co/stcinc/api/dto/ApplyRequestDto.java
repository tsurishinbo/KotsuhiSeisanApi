package jp.co.stcinc.api.dto;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import jp.co.stcinc.api.util.Constants;
import jp.co.stcinc.api.util.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 交通費申請APIリクエスト用DTO
 */
@XmlRootElement
public class ApplyRequestDto {
    
    // 社員番号
    @Getter @Setter
    private String emp_no;
    
    // トークン
    @Getter @Setter
    private String token;
    
    // 交通費申請明細リスト
    @Getter @Setter
    private ArrayList<ApplyDetailDto> list;

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
        // 交通費申請明細リスト
        if (list.size() == 0) {
            return false;
        }
        for (ApplyDetailDto dto : list) {
            // 利用日
            if (StringUtils.isEmpty(dto.getUsed_date())) {
                return false;
            }
            if (StringUtils.length(dto.getUsed_date()) != 8) {
                return false;
            }
            if (!DateUtils.checkDateFormat(dto.getUsed_date(), "yyyyMMdd")) {
                return false;
            }
            // 出張場所
            if (StringUtils.isEmpty(dto.getPlace())) {
                return false;
            }
            if (StringUtils.length(dto.getPlace()) > 40) {
                return false;
            }
            // 出張目的
            if (StringUtils.isEmpty(dto.getPurpose())) {
                return false;
            }
            if (StringUtils.length(dto.getPurpose()) > 40) {
                return false;
            }
            // 交通手段コード
            if (StringUtils.isEmpty(dto.getMeans_id())) {
                return false;
            }
            // 出発地
            if (StringUtils.isEmpty(dto.getSection_from())) {
                return false;
            }
            if (StringUtils.length(dto.getSection_from()) > 40) {
                return false;
            }
            // 到着地
            if (StringUtils.isEmpty(dto.getSection_to())) {
                return false;
            }
            if (StringUtils.length(dto.getSection_to()) > 40) {
                return false;
            }
            // 往復フラグ
            if (!Constants.ROUNDTRIP_ON.toString().equals(dto.getIs_roundtrip()) && 
                !Constants.ROUNDTRIP_OFF.toString().equals(dto.getIs_roundtrip())) {
                return false;
            }
            // 料金
            if (StringUtils.isEmpty(dto.getFare())) {
                return false;
            }
            if (!StringUtils.isNumeric(dto.getFare())) {
                return false;
            }
        }

        
        return true;
    }
}
