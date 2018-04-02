package jp.co.stcinc.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 認証APIレスポンス用DTO
 */
public class AuthResponseDto {

    // 処理結果
    @Getter @Setter
    private Integer result;
    
    // エラーコード
    @Getter @Setter
    private String err_code;
    
    // エラーメッセージ
    @Getter @Setter
    private String err_message;
    
    // トークン
    @Getter @Setter
    private String token;
    
}
