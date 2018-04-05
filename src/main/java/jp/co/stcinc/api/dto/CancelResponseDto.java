package jp.co.stcinc.api.dto;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * 交通費申請取消APIレスポンス用DTO
 */
@XmlRootElement
public class CancelResponseDto {

    // 処理結果
    @Getter @Setter
    private Integer result;
    
    // エラーコード
    @Getter @Setter
    private String err_code;
    
    // エラーメッセージ
    @Getter @Setter
    private String err_message;
    
}
