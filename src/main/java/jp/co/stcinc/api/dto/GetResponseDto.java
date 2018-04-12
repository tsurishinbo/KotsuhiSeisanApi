package jp.co.stcinc.api.dto;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * 交通費申請取得APIレスポンス用DTO
 */
@XmlRootElement
public class GetResponseDto extends BaseResponseDto {

    // 交通費申請リスト
    @Getter @Setter
    private ArrayList<ApplyDto> list;

}
