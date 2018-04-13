package jp.co.stcinc.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jp.co.stcinc.api.dto.ApplyDetailRequestDto;
import jp.co.stcinc.api.dto.ApplyRequestDto;
import jp.co.stcinc.api.dto.ApplyResponseDto;
import jp.co.stcinc.api.dto.AuthRequestDto;
import jp.co.stcinc.api.dto.AuthResponseDto;
import jp.co.stcinc.api.dto.ReleaseRequestDto;
import jp.co.stcinc.api.dto.ReleaseResponseDto;
import jp.co.stcinc.api.entity.MEmployee;
import jp.co.stcinc.api.entity.MMeans;
import jp.co.stcinc.api.entity.MOrder;
import jp.co.stcinc.api.entity.TApplication;
import jp.co.stcinc.api.entity.TAuth;
import jp.co.stcinc.api.entity.TLine;
import jp.co.stcinc.api.facade.MEmployeeFacade;
import jp.co.stcinc.api.facade.MMeansFacade;
import jp.co.stcinc.api.facade.MOrderFacade;
import jp.co.stcinc.api.facade.TApplicationFacade;
import jp.co.stcinc.api.facade.TAuthFacade;
import jp.co.stcinc.api.common.Constants;
import jp.co.stcinc.api.common.DateUtils;
import jp.co.stcinc.api.common.JsonUtils;
import jp.co.stcinc.api.dto.ApplyDetailResponseDto;
import jp.co.stcinc.api.dto.BaseResponseDto;
import jp.co.stcinc.api.dto.DeleteRequestDto;
import jp.co.stcinc.api.dto.DeleteResponseDto;
import jp.co.stcinc.api.dto.GetResponseDto;

/**
 * REST Web Service
 */
@Path("application")
@Stateless
public class WebService {

    private static final int VALID_TOKEN = 0;   // トークンが有効
    private static final int INVALID_TOKEN = 1; // トークンが不正
    private static final int EXPIRED_TOKEN = 2; // トークンが有効期限切れ
    
    @EJB
    private MEmployeeFacade mEmployeeFacade;
    @EJB
    private MOrderFacade mOrderFacade;
    @EJB
    private MMeansFacade mMeansFacade;
    @EJB
    private TAuthFacade tAuthFacade;
    @EJB
    private TApplicationFacade tApplicationFacade;

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
            // リクエストパラメータのチェック
            AuthRequestDto requestDto = JsonUtils.parseJson(AuthRequestDto.class, param);
            if (requestDto == null || !requestDto.checkParam()) {
                // 異常終了（パラメータ不正）
                responseDto.SetErrorParam();
                return JsonUtils.makeJson(responseDto);
            }
            
            // リクエストパラメータの取得
            Integer empNo = Integer.parseInt(requestDto.getEmp_no());   // 社員番号
            String password = requestDto.getPassword();                 // パスワード
            
            // 社員マスタのチェック
            MEmployee employee = mEmployeeFacade.getEmployee(empNo, password);
            if (employee == null) {
                // 異常終了（認証失敗）
                responseDto.SetErrorAuth();
                return JsonUtils.makeJson(responseDto);
            }
            
            // 認証
            String nowDate = DateUtils.getNowDate();                // 現在日時
            String newExpire = DateUtils.getAddHourDate(1);         // 現在日時+1時間
            String newToken = makeToken(empNo.toString() + nowDate);// トークン
            TAuth auth = tAuthFacade.getAuthInfo(empNo);
            if (auth == null) {
                // 認証情報作成
                TAuth newAuth = new TAuth();
                newAuth.setEmpNo(empNo);
                newAuth.setToken(newToken);
                newAuth.setExpire(newExpire);
                tAuthFacade.create(newAuth);
            } else {
                // 認証情報更新
                String token = auth.getToken();
                String expire = auth.getExpire();
                if ((token == null && expire == null) || (Long.parseLong(expire) < Long.parseLong(nowDate))) {
                    auth.setToken(newToken);
                    auth.setExpire(newExpire);
                    tAuthFacade.edit(auth);
                } else {
                    // 異常終了（認証済）
                    responseDto.SetErrorAlready();
                    return JsonUtils.makeJson(responseDto);
                }
            }
            
            // 正常終了
            responseDto.SetSuccessResult(newToken);
            return JsonUtils.makeJson(responseDto);
            
        } catch (Exception e) {
            // 異常終了（システム例外）
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
            // リクエストパラメータのチェック
            ReleaseRequestDto requestDto = JsonUtils.parseJson(ReleaseRequestDto.class, param);
            if (requestDto == null || !requestDto.checkParam()) {
                // 異常終了（パラメータ不正）
                responseDto.SetErrorParam();
                return JsonUtils.makeJson(responseDto);
            }
            
            // リクエストパラメータの取得
            Integer empNo = Integer.parseInt(requestDto.getEmp_no());   // 社員番号
            String token = requestDto.getToken();                       // トークン
            
            // トークンのチェック
            if (!checkToken(empNo, token, responseDto)) {
                // 異常終了（トークン不正 または トークン有効期限切れ）
                return JsonUtils.makeJson(responseDto);
            }
            
            // 認証解除
            TAuth auth = tAuthFacade.getAuthInfo(empNo, token);
            if (auth != null) {
                // 認証情報を更新
                auth.setToken(null);
                auth.setExpire(null);
                tAuthFacade.edit(auth);
            }
            
            // 正常終了
            responseDto.SetSuccessResult();
            return JsonUtils.makeJson(responseDto);
            
        } catch (Exception e) {
            // 異常終了（システム例外）
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
        
        ApplyResponseDto responseDto = new ApplyResponseDto();

        try {
            // リクエストパラメータのチェック
            ApplyRequestDto requestDto = JsonUtils.parseJson(ApplyRequestDto.class, param);
            if (requestDto == null || !requestDto.checkParam()) {
                // 異常終了（パラメータ不正）
                responseDto.SetErrorParam();
                return JsonUtils.makeJson(responseDto);
            }
            
            // リクエストパラメータの取得
            Integer empNo = Integer.parseInt(requestDto.getEmp_no());       // 社員番号
            String token = requestDto.getToken();                           // トークン
            ArrayList<ApplyDetailRequestDto> applyDetails = requestDto.getList();  // 申請内容
            
            // 作業コード、交通手段コードのマスタチェック
            for (ApplyDetailRequestDto applyDetail : applyDetails) {
                MOrder order = mOrderFacade.getOrder(applyDetail.getOrder_id());
                MMeans means = mMeansFacade.getMeans(Integer.parseInt(applyDetail.getMeans_id()));
                if (order == null || means == null) {
                    // 異常終了（コード不正）
                    responseDto.SetErrorCode();
                    return JsonUtils.makeJson(responseDto);
                }
            }
            
            // トークンのチェック
            if (!checkToken(empNo, token, responseDto)) {
                // 異常終了（トークン不正 または トークン有効期限切れ）
                return JsonUtils.makeJson(responseDto);
            }
            
            // 交通費申請
            int sortNo = 1;
            Long totalFare = 0L;
            ArrayList<TLine> lines = new ArrayList<>();
            for (ApplyDetailRequestDto applyDetail : applyDetails) {
                TLine line = new TLine();
                line.setUsedDate(DateUtils.stringToDate(applyDetail.getUsed_date(), "yyyyMMdd"));   // 利用日
                line.setOrderId(applyDetail.getOrder_id());                                         // 作業コード
                line.setPlace(applyDetail.getPlace());                                              // 出張場所
                line.setPurpose(applyDetail.getPurpose());                                          // 出張目的
                line.setMeansId(Integer.parseInt(applyDetail.getMeans_id()));                       // 交通手段コード
                line.setSectionFrom(applyDetail.getSection_from());                                 // 出発地
                line.setSectionTo(applyDetail.getSection_to());                                     // 到着地
                line.setIsRoundtrip(Integer.parseInt(applyDetail.getIs_roundtrip()));               // 往復フラグ
                line.setFare(Long.parseLong(applyDetail.getFare()));                                // 料金
                line.setMemo(applyDetail.getMemo());                                                // 備考
                line.setSortNo(sortNo);                                                             // ソート番号
                lines.add(line);
                sortNo += 1;
                totalFare += Long.parseLong(applyDetail.getFare());
            }
            TApplication application = new TApplication();
            application.setStatus(Constants.STATUS_WAIT);                  // ステータス
            application.setApplyId(empNo);                              // 申請者ID
            application.setApplyDate(new Date());                       // 申請日
            application.setApproveId(mEmployeeFacade.getBossId(empNo)); // 承認者ID
            application.setTotalFare(totalFare);                        // 料金合計
            application.setLines(lines);                                // 申請明細
            tApplicationFacade.create(application);    
            
            // 正常終了
            responseDto.SetSuccessResult();
            return JsonUtils.makeJson(responseDto);            

        } catch (Exception e) {
            // 異常終了（システム例外）
            responseDto.SetErrorSystem();
            return JsonUtils.makeJson(responseDto);
        }

    }

    /**
     * 交通費申請削除 (PUT)
     * @param param リクエストパラメータ
     * @return レスポンスパラメータ
     */
    @PUT
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String cancel(String param) {
        
        DeleteResponseDto responseDto = new DeleteResponseDto();
        
        try {
            // リクエストパラメータのチェック
            DeleteRequestDto requestDto = JsonUtils.parseJson(DeleteRequestDto.class, param);
            if (requestDto == null || !requestDto.checkParam()) {
                // 異常終了（パラメータ不正）
                responseDto.SetErrorParam();
                return JsonUtils.makeJson(responseDto);
            }
            
            // リクエストパラメータの取得
            Integer empNo = Integer.parseInt(requestDto.getEmp_no());   // 社員番号
            String token = requestDto.getToken();                       // トークン
            Integer id = Integer.parseInt(requestDto.getId());          // 申請ID
            
            // トークンのチェック
            if (!checkToken(empNo, token, responseDto)) {
                // 異常終了（トークン不正 または トークン有効期限切れ）
                return JsonUtils.makeJson(responseDto);
            }
            
            // 交通費申請取消（削除）
            TApplication application = tApplicationFacade.getApplication(id);
            if (application == null) {
                // 異常終了（該当申請なし）
                responseDto.SetErrorNotfound();
                return JsonUtils.makeJson(responseDto);
            }
            if (application.getStatus() != Constants.STATUS_SAVE &&
                application.getStatus() != Constants.STATUS_WAIT) {
                // 異常終了（申請取消不可）
                responseDto.SetErrorCancel();
                return JsonUtils.makeJson(responseDto);
            }
            tApplicationFacade.remove(application);

            // 正常終了
            responseDto.SetSuccessResult();
            return JsonUtils.makeJson(responseDto);            
            
        } catch (Exception e) {
            // 異常終了（システム例外）
            responseDto.SetErrorSystem();
            return JsonUtils.makeJson(responseDto);
        }

    }
    
    /**
     * 交通費申請取得 (PUT)
     * @param param リクエストパラメータ
     * @return レスポンスパラメータ
     */
    @PUT
    @Path("get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String get(String param) {
        
        GetResponseDto responseDto = new GetResponseDto();
        
        try {
            // リクエストパラメータのチェック
            DeleteRequestDto requestDto = JsonUtils.parseJson(DeleteRequestDto.class, param);
            if (requestDto == null || !requestDto.checkParam()) {
                // 異常終了（パラメータ不正）
                responseDto.SetErrorParam();
                return JsonUtils.makeJson(responseDto);
            }
            
            // リクエストパラメータの取得
            Integer empNo = Integer.parseInt(requestDto.getEmp_no());   // 社員番号
            String token = requestDto.getToken();                       // トークン
            Integer id = Integer.parseInt(requestDto.getId());          // 申請ID
            
            // トークンのチェック
            if (!checkToken(empNo, token, responseDto)) {
                // 異常終了（トークン不正 または トークン有効期限切れ）
                return JsonUtils.makeJson(responseDto);
            }
            
            // 交通費申請取得
            TApplication application = tApplicationFacade.getApplication(id);
            if (application == null) {
                // 異常終了（該当申請なし）
                responseDto.SetErrorNotfound();
                return JsonUtils.makeJson(responseDto);
            }
            
            // レスポンスに取得内容を設定
            // *申請
            
            
            // *申請明細
            ArrayList<ApplyDetailResponseDto> details = new ArrayList<>();
            for (TLine line : application.getLines()) {
                ApplyDetailResponseDto detail = new ApplyDetailResponseDto();
                detail.setUsed_date(line.getUsedDate().toString());
                
                
                
                
                details.add(detail);
            }
            responseDto.setList(details);
            
            // 正常終了
            responseDto.SetSuccessResult();
            return JsonUtils.makeJson(responseDto);            
            
        } catch (Exception e) {
            // 異常終了（システム例外）
            responseDto.SetErrorSystem();
            return JsonUtils.makeJson(responseDto);
        }
    }
    
    /**
     * トークンを作成する
     * @param key キー
     * @return トークン
    */
    private String makeToken(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(key.getBytes());
            StringBuilder token = new StringBuilder();
            for (byte b : md.digest()) {
                token.append(String.format("%02x", b));
            }
            return token.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    
    /**
     * トークンのチェックを行う
     * @param empNo 社員番号
     * @param token トークン
     * @param response レスポンス ※エラーの場合に異常終了レスポンスを設定
     * @return チェック結果 
     */
    private boolean checkToken(Integer empNo, String token, BaseResponseDto response) {
        TAuth auth = tAuthFacade.getAuthInfo(empNo, token);
        // トークンの存在をチェック
        if (auth == null) {
            // トークンが不正
            response.SetErrorToken();
            return false;
        }
        // トークンの有効期限をチェック
        String nowDate = DateUtils.getNowDate();
        String expire = auth.getExpire();
        if (expire == null || (Long.parseLong(expire) < Long.parseLong(nowDate))) {
            // トークンが有効期限切れ
            response.SetErrorExpire();
            return false;
        }
        // トークンが有効
        return true;
    }

}
