package jp.co.stcinc.api.dto;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * リクエスト用DTO基底クラス
 */
@XmlRootElement
public abstract class BaseRequestDto {

    // 社員番号
    @Getter @Setter
    protected String emp_no;

    /**
     * パラメータチェック
     * @return チェック結果
     */
    public abstract boolean checkParam();

}
