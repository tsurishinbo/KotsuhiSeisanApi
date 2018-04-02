package jp.co.stcinc.api.dto;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

/**
 * 交通費申請取得APIレスポンス用DTO
 */
public class GetResponseDto {

    // 処理結果
    @Getter @Setter
    private Integer result;
    
    // エラーコード
    @Getter @Setter
    private String err_code;
    
    // エラーメッセージ
    @Getter @Setter
    private String err_message;

    // 交通費申請リスト
    @Getter @Setter
    private ArrayList<ApplyDto> list;
}
