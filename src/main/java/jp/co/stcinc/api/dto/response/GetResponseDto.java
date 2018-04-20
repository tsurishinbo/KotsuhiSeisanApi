package jp.co.stcinc.api.dto.response;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * 交通費申請取得APIレスポンス用DTO
 */
@XmlRootElement
public class GetResponseDto extends AbstractResponseDto {

    @Getter @Setter
    private Integer id;
    
    @Getter @Setter
    private Integer status;
    
    @Getter @Setter
    private Integer apply_id;
    
    @Getter @Setter
    private String apply_name;
    
    @Getter @Setter
    private String apply_date;
    
    @Getter @Setter
    private Integer approve_id;
    
    @Getter @Setter
    private String approve_name;
    
    @Getter @Setter
    private String approve_date;
    
    @Getter @Setter
    private Long total_fare;

    // 申請明細リスト
    @Getter @Setter
    private ArrayList<ApplyDetailResponseDto> list;

}
