package jp.co.stcinc.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 交通費申請明細DTO
 */
public class ApplyDetailDto {

    // 利用日
    @Getter @Setter
    private String used_date;

    // 作業コード
    @Getter @Setter
    private String order_id;

    // 出張場所
    @Getter @Setter
    private String place;

    // 出張目的
    @Getter @Setter
    private String purpose;

    // 交通手段コード
    @Getter @Setter
    private String means_id;

    // 出発地
    @Getter @Setter
    private String section_from;

    // 到着地
    @Getter @Setter
    private String section_to;

    // 往復フラグ
    @Getter @Setter
    private String is_roundtrip;

    // 料金
    @Getter @Setter
    private String fare;

    // 備考
    @Getter @Setter
    private String memo;
}
