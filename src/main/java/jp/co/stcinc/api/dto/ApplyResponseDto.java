package jp.co.stcinc.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 交通費申請APIレスポンス用DTO
 */
public class ApplyResponseDto {

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
