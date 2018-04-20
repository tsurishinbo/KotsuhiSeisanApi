package jp.co.stcinc.api.dto.response;

import javax.xml.bind.annotation.XmlRootElement;
import jp.co.stcinc.api.common.Constants;
import lombok.Getter;
import lombok.Setter;

/**
 * 認証APIレスポンス用DTO
 */
@XmlRootElement
public class AuthResponseDto extends AbstractResponseDto {

    // トークン
    @Getter @Setter
    private String token;

}
