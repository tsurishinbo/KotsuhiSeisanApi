package jp.co.stcinc.api.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jp.co.stcinc.api.common.Constants;
import jp.co.stcinc.api.dto.request.ApplyDetailRequestDto;
import jp.co.stcinc.api.dto.request.ApplyRequestDto;
import jp.co.stcinc.api.dto.response.ApplyResponseDto;
import jp.co.stcinc.api.dto.request.AuthRequestDto;
import jp.co.stcinc.api.dto.response.AuthResponseDto;
import jp.co.stcinc.api.dto.response.ReleaseResponseDto;
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
import jp.co.stcinc.api.common.DateUtils;
import jp.co.stcinc.api.common.JsonUtils;
import jp.co.stcinc.api.dto.response.ApplyDetailResponseDto;
import jp.co.stcinc.api.dto.response.DeleteResponseDto;
import jp.co.stcinc.api.dto.response.GetResponseDto;
import org.apache.commons.lang3.StringUtils;

/**
 * REST Web Service
 */
@Path("application")
@Stateless
public class WebService {

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
     * トークン発行 (POST)
     * @param pJson JSONリクエスト
     * @return レスポンス
     */
    @POST
    @Path("auth")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String auth(final String pJson) {
        
        AuthResponseDto responseDto = new AuthResponseDto();

        try {
            // リクエストパラメータのチェック
            AuthRequestDto requestDto = JsonUtils.parseJson(AuthRequestDto.class, pJson);
            if (requestDto == null || !requestDto.checkParam()) {
                // 異常終了（パラメータ不正）
                responseDto.SetErrorInvalidParam();
                return JsonUtils.makeJson(responseDto);
            }
            
            // リクエストパラメータの取得
            Integer empNo = Integer.parseInt(requestDto.getEmp_no()); // 社員番号
            String password = requestDto.getPassword();               // パスワード
            
            // 社員マスタのチェック
            MEmployee employee = mEmployeeFacade.getEmployee(empNo, password);
            if (employee == null) {
                // 異常終了（認証失敗）
                responseDto.SetErrorAuthFailed();
                return JsonUtils.makeJson(responseDto);
            }
            
            // トークン発行
            String token = makeToken(empNo.toString() + DateUtils.getNowDateString("yyyyMMddHHmmss"));
            TAuth newAuth = new TAuth();
            newAuth.setToken(token);
            newAuth.setEmpNo(empNo);
            newAuth.setIssued(DateUtils.getNowDate());
            newAuth.setExpire(DateUtils.getAddDate(1, 0, 0));
            tAuthFacade.create(newAuth);
            
            // 正常終了
            responseDto.setToken(token);
            responseDto.SetSuccess();
            return JsonUtils.makeJson(responseDto);
            
        } catch (NumberFormatException e) {
            // 異常終了（システム例外）
            responseDto.SetErrorSystemFailed();
            return JsonUtils.makeJson(responseDto);
        }
        
    }
    
    /**
     * トークン削除 (DELETE)
     * @param pAuth HTTPヘッダ(Authorization)
     * @return レスポンス
     */
    @DELETE
    @Path("release")
    @Produces(MediaType.APPLICATION_JSON)
    public String release(@HeaderParam("Authorization") final String pAuth) {

        ReleaseResponseDto responseDto = new ReleaseResponseDto();
        
        try {
            // 有効期限切れトークンの取得
            TAuth auth = tAuthFacade.getInvalidToken(getToken(pAuth));
            if (auth == null) {
                // 異常終了（該当トークンなし）
                responseDto.SetErrorNotfoundToken();
                return JsonUtils.makeJson(responseDto);
            }
            
            // 有効期限切れトークンの削除
            tAuthFacade.remove(auth);
            
            // 正常終了
            responseDto.SetSuccess();
            return JsonUtils.makeJson(responseDto);
            
        } catch (NumberFormatException e) {
            // 異常終了（システム例外）
            responseDto.SetErrorSystemFailed();
            return JsonUtils.makeJson(responseDto);
        }
        
    }
    
    /**
     * 交通費申請 (POST)
     * @param pAuth HTTPヘッダ(Authorization)
     * @param pJson JSONリクエスト
     * @return レスポンス
     */
    @POST
    @Path("apply")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String apply(@HeaderParam("Authorization") final String pAuth, final String pJson) {
        
        ApplyResponseDto responseDto = new ApplyResponseDto();

        try {
            // トークンのチェック
            String token = getToken(pAuth);
            if (token == null || !checkToken(token)) {
                // 異常終了（トークン不正）
                responseDto.SetErrorInvalidToken();
                return JsonUtils.makeJson(responseDto);
            }

            // リクエストパラメータのチェック
            ApplyRequestDto requestDto = JsonUtils.parseJson(ApplyRequestDto.class, pJson);
            if (requestDto == null || !requestDto.checkParam()) {
                // 異常終了（パラメータ不正）
                responseDto.SetErrorInvalidParam();
                return JsonUtils.makeJson(responseDto);
            }
            
            // リクエストパラメータの取得
            Integer empNo = Integer.parseInt(requestDto.getEmp_no());             // 社員番号
            ArrayList<ApplyDetailRequestDto> applyDetails = requestDto.getList(); // 申請明細
            
            // 作業コード、交通手段コードのコードチェック
            for (ApplyDetailRequestDto applyDetail : applyDetails) {
                MOrder order = mOrderFacade.getOrder(applyDetail.getOrder_id());
                MMeans means = mMeansFacade.getMeans(Integer.parseInt(applyDetail.getMeans_id()));
                if (order == null || means == null) {
                    // 異常終了（コード不正）
                    responseDto.SetErrorInvalidCode();
                    return JsonUtils.makeJson(responseDto);
                }
            }
            
            // 交通費申請
            int sortNo = 1;
            int totalFare = 0;
            ArrayList<TLine> lines = new ArrayList<>();
            for (ApplyDetailRequestDto applyDetail : applyDetails) {
                TLine line = new TLine();
                line.setUsedDate(DateUtils.stringToDate(applyDetail.getUsed_date(), "yyyyMMdd")); // 利用日
                line.setOrderId(applyDetail.getOrder_id());                                       // 作業コード
                line.setPlace(applyDetail.getPlace());                                            // 出張場所
                line.setPurpose(applyDetail.getPurpose());                                        // 出張目的
                line.setMeansId(Integer.parseInt(applyDetail.getMeans_id()));                     // 交通手段コード
                line.setSectionFrom(applyDetail.getSection_from());                               // 出発地
                line.setSectionTo(applyDetail.getSection_to());                                   // 到着地
                line.setIsRoundtrip(Integer.parseInt(applyDetail.getIs_roundtrip()));             // 往復フラグ
                line.setFare(Integer.parseInt(applyDetail.getFare()));                            // 料金
                line.setMemo(applyDetail.getMemo());                                              // 備考
                line.setSortNo(sortNo);                                                           // ソート番号
                lines.add(line);
                sortNo += 1;
                totalFare += Long.parseLong(applyDetail.getFare());
            }
            TApplication application = new TApplication();
            application.setStatus(Constants.STATUS_WAIT_BOSS);              // ステータス
            application.setApplyId(empNo);                                  // 申請者ID
            application.setApplyDate(new Date());                           // 申請日
            application.setBossApproveId(mEmployeeFacade.getBossId(empNo)); // 承認者ID
            application.setTotalFare(totalFare);                            // 料金合計
            application.setLines(lines);                                    // 申請明細
            tApplicationFacade.create(application);    
            
            // 正常終了
            responseDto.SetSuccess();
            return JsonUtils.makeJson(responseDto);            

        } catch (NumberFormatException e) {
            // 異常終了（システム例外）
            responseDto.SetErrorSystemFailed();
            return JsonUtils.makeJson(responseDto);
        }

    }

    /**
     * 交通費申請取得 (GET)
     * @param pAuth HTTPヘッダ(Authorization)
     * @param pId 申請ID
     * @return レスポンス
     */
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@HeaderParam("Authorization") final String pAuth, @PathParam("id") final String pId) {
        
        GetResponseDto responseDto = new GetResponseDto();
        
        try {
            // トークンのチェック
            String token = getToken(pAuth);
            if (token == null || !checkToken(token)) {
                // 異常終了（トークン不正）
                responseDto.SetErrorInvalidToken();
                return JsonUtils.makeJson(responseDto);
            }

            // リクエストパラメータのチェック
            if (StringUtils.isEmpty(pId) || !StringUtils.isNumeric(pId)) {
                // 異常終了（パラメータ不正）
                responseDto.SetErrorInvalidParam();
                return JsonUtils.makeJson(responseDto);
            }
            
            // 交通費申請取得
            TApplication application = tApplicationFacade.getApplication(Integer.parseInt(pId));
            if (application == null) {
                // 異常終了（該当申請なし）
                responseDto.SetErrorNotfoundApplication();
                return JsonUtils.makeJson(responseDto);
            }
            
            // 正常終了
            // *申請
            responseDto.setId(application.getId());
            responseDto.setStatus(application.getStatus());
            responseDto.setApply_id(application.getApplyId());
            if (application.getApplicant() != null) {
                responseDto.setApply_name(application.getApplicant().getEmployeeName());
            }
            responseDto.setApply_date(DateUtils.dateToString(application.getApplyDate(), "yyyyMMdd"));
            responseDto.setBoss_approve_id(application.getBossApproveId());
            if (application.getBoss() != null) {
                responseDto.setBoss_approve_name(application.getBoss().getEmployeeName());
            }
            responseDto.setBoss_approve_date(DateUtils.dateToString(application.getBossApproveDate(), "yyyyMMdd"));
            responseDto.setManager_approve_id(application.getManagerApproveId());
            if (application.getManager() != null) {
                responseDto.setManager_approve_name(application.getManager().getEmployeeName());
            }
            responseDto.setManager_approve_date(DateUtils.dateToString(application.getManagerApproveDate(), "yyyyMMdd"));
            responseDto.setPayment_id(application.getPaymentId());
            if (application.getPayer() != null) {
                responseDto.setPayment_name(application.getPayer().getEmployeeName());
            }
            responseDto.setPayment_date(DateUtils.dateToString(application.getPaymentDate(), "yyyyMMdd"));
            responseDto.setTotal_fare(application.getTotalFare());
            responseDto.setReject_cnt(application.getRejectCnt());

            // *申請明細
            ArrayList<ApplyDetailResponseDto> details = new ArrayList<>();
            for (TLine line : application.getLines()) {
                ApplyDetailResponseDto detail = new ApplyDetailResponseDto();
                detail.setUsed_date(DateUtils.dateToString(line.getUsedDate(),"yyyyMMdd"));
                detail.setOrder_id(line.getOrderId());
                detail.setPlace(line.getPlace());
                detail.setPurpose(line.getPurpose());
                detail.setMeans_id(line.getMeansId());
                detail.setMeans_name(line.getMeans().getMeans());
                detail.setSection_from(line.getSectionFrom());
                detail.setSection_to(line.getSectionTo());
                detail.setIs_roundtrip(line.getIsRoundtrip());
                detail.setFare(line.getFare());
                detail.setMemo(line.getMemo());
                if (line.getOrder() != null) {
                    detail.setOrder_name(line.getOrder().getOrderName());
                }
                details.add(detail);
            }
            responseDto.setList(details);
            responseDto.SetSuccess();
            return JsonUtils.makeJson(responseDto);            
            
        } catch (NumberFormatException e) {
            // 異常終了（システム例外）
            responseDto.SetErrorSystemFailed();
            return JsonUtils.makeJson(responseDto);
        }

    }

    /**
     * 交通費申請削除 (DELETE)
     * @param pAuth HTTPヘッダ(Authorization)
     * @param pId 申請ID
     * @return レスポンス
     */
    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String cancel(@HeaderParam("Authorization") final String pAuth, @PathParam("id") final String pId) {
        
        DeleteResponseDto responseDto = new DeleteResponseDto();
        
        try {
            // トークンのチェック
            String token = getToken(pAuth);
            if (token == null || !checkToken(token)) {
                // 異常終了（トークン不正）
                responseDto.SetErrorInvalidToken();
                return JsonUtils.makeJson(responseDto);
            }

            // リクエストパラメータのチェック
            if (StringUtils.isEmpty(pId) || !StringUtils.isNumeric(pId)) {
                // 異常終了（パラメータ不正）
                responseDto.SetErrorInvalidParam();
                return JsonUtils.makeJson(responseDto);
            }

            // 交通費申請削除
            TApplication application = tApplicationFacade.getApplication(Integer.parseInt(pId));
            if (application == null) {
                // 異常終了（該当申請なし）
                responseDto.SetErrorNotfoundApplication();
                return JsonUtils.makeJson(responseDto);
            }
            if (application.getStatus() != Constants.STATUS_SAVE && application.getStatus() != Constants.STATUS_WAIT_BOSS) {
                // 異常終了（削除不可ステータス）
                responseDto.SetErrorCannotDelete();
                return JsonUtils.makeJson(responseDto);
            }
            tApplicationFacade.remove(application);

            // 正常終了
            responseDto.SetSuccess();
            return JsonUtils.makeJson(responseDto);            
            
        } catch (NumberFormatException e) {
            // 異常終了（システム例外）
            responseDto.SetErrorSystemFailed();
            return JsonUtils.makeJson(responseDto);
        }

    }
    
    /**
     * HTTPヘッダ(Authorization)からトークンを取得する
     * @param auth HTTPヘッダ(Authorization)
     * @return トークン
     */
    private String getToken(String auth) {
        if (auth == null) {
            return null;
        }
        String token = auth.replaceFirst("Bearer ", "");
        return token;
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
     * @param token トークン
     * @return チェック結果 
     */
    private boolean checkToken(String token) {
        TAuth auth = tAuthFacade.getValidToken(token);
        if (auth == null) {
            return false;
        }
        return true;
    }

}
