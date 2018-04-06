package jp.co.stcinc.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jp.co.stcinc.api.dto.AuthRequestDto;
import jp.co.stcinc.api.dto.AuthResponseDto;
import jp.co.stcinc.api.dto.ReleaseRequestDto;
import jp.co.stcinc.api.dto.ReleaseResponseDto;
import jp.co.stcinc.api.entity.MEmployee;
import jp.co.stcinc.api.entity.TAuth;
import jp.co.stcinc.api.exception.KotsuhiSeisanApiException;
import jp.co.stcinc.api.facade.MEmployeeFacade;
import jp.co.stcinc.api.facade.TAuthFacade;
import jp.co.stcinc.api.util.Constants;
import jp.co.stcinc.api.util.DateUtils;
import jp.co.stcinc.api.util.JsonUtils;

/**
 * REST Web Service
 */
@Path("application")
@Stateless
public class WebService {

    @EJB
    private MEmployeeFacade mEmployeeFacade;
    @EJB
    private TAuthFacade tAuthFacade;

    /**
     * コンストラクタ
     */
    public WebService() {
    }

    /**
     * 認証 (POST)
     * @param param リクエストパラメータ
     * @return レスポンスパラメータ
     */
    @POST
    @Path("auth")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String auth(String param) {
        
        AuthResponseDto responseDto = new AuthResponseDto();

        try {
            // システム日付を取得
            String nowDate = DateUtils.getNowDate();
            // システム日付＋1時間を取得
            String newExpire = DateUtils.getAddHourDate(1);
            
            // リクエストパラメータを取得
            AuthRequestDto requestDto = JsonUtils.parseJson(AuthRequestDto.class, param);
            // リクエストパラメータチェック
            if (requestDto == null || !requestDto.checkParam()) {
                // 異常終了レスポンス（パラメータ不正）
                responseDto.SetErrorParam();
                return JsonUtils.makeJson(responseDto);
            }
            // リクエストパラメータから社員番号、パスワードを取得
            Integer empNo = Integer.parseInt(requestDto.getEmp_no());
            String password = requestDto.getPassword();
            
            // 社員マスタから社員を取得
            MEmployee employee = mEmployeeFacade.getEmployee(empNo, password);
            if (employee == null) {
                // 異常終了レスポンス（認証失敗）
                responseDto.SetErrorAuth();
                return JsonUtils.makeJson(responseDto);
            }
            
            // トークンを作成
            String newToken = makeToken(empNo.toString() + nowDate);
            
            // 認証テーブルから認証情報を取得
            TAuth auth = tAuthFacade.getAuthInfo(empNo);
            if (auth == null) {
                // 認証情報を作成
                TAuth newAuth = new TAuth();
                newAuth.setEmpNo(empNo);
                newAuth.setToken(newToken);
                newAuth.setExpire(newExpire);
                tAuthFacade.create(newAuth);
            } else {
                // 認証情報からトークンと有効期限を取得
                String token = auth.getToken();
                String expire = auth.getExpire();
                // 認証解除済 または 有効期限切れ
                if ((token == null && expire == null) || (Long.parseLong(expire) < Long.parseLong(nowDate))) {
                    // 認証情報を更新
                    auth.setToken(newToken);
                    auth.setExpire(newExpire);
                    tAuthFacade.edit(auth);
                } else {
                    // 異常終了レスポンス（認証済）
                    responseDto.SetErrorAlready();
                    return JsonUtils.makeJson(responseDto);
                }
            }

            // 正常終了レスポンス
            responseDto.SetSuccessResult(newToken);
            return JsonUtils.makeJson(responseDto);
            
        } catch (Exception e) {
            // 異常終了レスポンス（システム例外）
            responseDto.SetErrorSystem();
            return JsonUtils.makeJson(responseDto);
        }
    }
    
    /**
     * 認証解除 (PUT)
     * @param param リクエストパラメータ
     * @return レスポンスパラメータ
     */
    @PUT
    @Path("release")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String release(String param) {

        ReleaseResponseDto responseDto = new ReleaseResponseDto();
        
        try {
            // リクエストパラメータを取得
            ReleaseRequestDto requestDto = JsonUtils.parseJson(ReleaseRequestDto.class, param);
            // リクエストパラメータチェック
            if (requestDto == null || !requestDto.checkParam()) {
                // 異常終了レスポンス（パラメータ不正）
                responseDto.SetErrorParam();
                return JsonUtils.makeJson(responseDto);
            }
            // リクエストパラメータから社員番号、トークンを取得
            Integer empNo = Integer.parseInt(requestDto.getEmp_no());
            String token = requestDto.getToken();

            // 認証テーブルから認証情報を取得
            TAuth auth = tAuthFacade.getAuthInfo(empNo, token);
            if (auth != null) {
                // 認証情報を更新
                auth.setToken(null);
                auth.setExpire(null);
                tAuthFacade.edit(auth);
            } else {
                // 異常終了レスポンス（認証解除失敗）
                responseDto.SetErrorRelease();
                return JsonUtils.makeJson(responseDto);
            }

            // 正常終了レスポンス
            responseDto.SetSuccessResult();
            return JsonUtils.makeJson(responseDto);
            
        } catch (Exception e) {
            // 異常終了レスポンス（システム例外）
            responseDto.SetErrorSystem();
            return JsonUtils.makeJson(responseDto);
        }
    }
    
    /**
     * 交通費申請 (POST)
     * @param param リクエストパラメータ
     * @return レスポンスパラメータ
     */
    @POST
    @Path("apply")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String apply(String param) {
        
        return null;
    }

    /**
     * 交通費申請取消 (DELETE)
     * @param param リクエストパラメータ
     * @return レスポンスパラメータ
     */
    @DELETE
    @Path("cencel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String cancel(String param) {
        
        return null;
    }
    
    /**
     * 交通費申請検索 (GET)
     * @param param リクエストパラメータ
     * @return レスポンスパラメータ
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String search(String param) {
        
        return null;
    }
    
    /**
     * トークンを作成する
     * @param key キー
     * @return トークン
     * @throws KotsuhiSeisanApiException 
     */
    private String makeToken(String key) throws KotsuhiSeisanApiException {
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(key.getBytes());
            StringBuilder token = new StringBuilder();
            for (byte b : md.digest()) {
                token.append(String.format("%02x", b));
            }
            return token.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new KotsuhiSeisanApiException(KotsuhiSeisanApiException.EXCEPTION_SYSTEM, e);
        }
       
    }
}
