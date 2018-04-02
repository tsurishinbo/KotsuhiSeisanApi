package jp.co.stcinc.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 交通費申請DTO
 */
public class ApplyDto {

    // 申請ID
    @Getter @Setter
    private String id;

    // ステータス
    @Getter @Setter
    private String status;

    // 申請者ID
    @Getter @Setter
    private String apply_id;

    // 申請日
    @Getter @Setter
    private String apply_date;

    // 承認者ID
    @Getter @Setter
    private String approve_id;

    // 承認日
    @Getter @Setter
    private String approve_date;

    // 合計料金
    @Getter @Setter
    private String total_fare;
}
