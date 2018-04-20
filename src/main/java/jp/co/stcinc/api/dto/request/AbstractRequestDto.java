package jp.co.stcinc.api.dto.request;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * リクエスト用DTO基底クラス
 */
@XmlRootElement
public abstract class AbstractRequestDto {

    /**
     * パラメータチェック
     * @return チェック結果
     */
    public abstract boolean checkParam();
    
}
