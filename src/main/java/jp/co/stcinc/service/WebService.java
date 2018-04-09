package jp.co.stcinc.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
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
import jp.co.stcinc.api.dto.ApplyDetailDto;
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

        // リクエストパラメータのチェック
        AuthRequestDto requestDto = JsonUtils.parseJson(AuthRequestDto.class, param);
        if (requestDto == null || !requestDto.checkParam()) {
            // 異常終了（パラメータ不正）
            responseDto.SetErrorParam();
            return JsonUtils.makeJson(responseDto);
        }
        // リクエストパラメータの取得
        Integer empNo = Integer.parseInt(requestDto.getEmp_no()); // 社員番号
        String password = requestDto.getPassword(); // パスワード
        // 社員マスタのチェック
        MEmployee employee = mEmployeeFacade.getEmployee(empNo, password);
        if (employee == null) {
            // 異常終了（認証失敗）
            responseDto.SetErrorAuth();
            return JsonUtils.makeJson(responseDto);
        }
        // 認証
        String nowDate = DateUtils.getNowDate(); // 現在日時
        String newExpire = DateUtils.getAddHourDate(1); // 現在日時+1時間
        String newToken = makeToken(empNo.toString() + nowDate); // トークン
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
        
        // リクエストパラメータのチェック
        ReleaseRequestDto requestDto = JsonUtils.parseJson(ReleaseRequestDto.class, param);
        if (requestDto == null || !requestDto.checkParam()) {
            // 異常終了（パラメータ不正）
            responseDto.SetErrorParam();
            return JsonUtils.makeJson(responseDto);
        }
        // リクエストパラメータの取得
        Integer empNo = Integer.parseInt(requestDto.getEmp_no()); // 社員番号
        String token = requestDto.getToken(); // トークン
        // 認証解除
        TAuth auth = tAuthFacade.getAuthInfo(empNo, token);
        if (auth != null) {
            // 認証情報を更新
            auth.setToken(null);
            auth.setExpire(null);
            tAuthFacade.edit(auth);
        } else {
            // 異常終了（認証解除失敗）
            responseDto.SetErrorRelease();
            return JsonUtils.makeJson(responseDto);
        }
        // 正常終了
        responseDto.SetSuccessResult();
        return JsonUtils.makeJson(responseDto);
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
        
        // リクエストパラメータのチェック
        ApplyRequestDto requestDto = JsonUtils.parseJson(ApplyRequestDto.class, param);
        if (requestDto == null || !requestDto.checkParam()) {
            // 異常終了（パラメータ不正）
            responseDto.SetErrorParam();
            return JsonUtils.makeJson(responseDto);
        }
        // リクエストパラメータの取得
        Integer empNo = Integer.parseInt(requestDto.getEmp_no()); // 社員番号
        String token = requestDto.getToken(); // トークン
        ArrayList<ApplyDetailDto> applyDetails = requestDto.getList(); // 申請内容
        // 作業コード、交通手段コードのマスタチェック
        for (ApplyDetailDto applyDetail : applyDetails) {
            MOrder order = mOrderFacade.getOrder(applyDetail.getOrder_id());
            MMeans means = mMeansFacade.getMeans(Integer.parseInt(applyDetail.getMeans_id()));
            if (order == null || means == null) {
                // 異常終了（パラメータ不正）
                responseDto.SetErrorParam();
                return JsonUtils.makeJson(responseDto);
            }
        }
        // 認証チェック
        if (!isAuthed(empNo, token)) {
            // 異常終了（認証失敗）
            responseDto.SetErrorAuth();
            return JsonUtils.makeJson(responseDto);
        }
        // 交通費申請
        int sortNo = 1;
        Long totalFare = 0L;
        ArrayList<TLine> lines = new ArrayList<>();
        for (ApplyDetailDto applyDetail : applyDetails) {
            TLine line = new TLine();
            line.setUsedDate(DateUtils.stringToDate(applyDetail.getUsed_date(), "yyyyMMdd")); // 利用日
            line.setOrderId(applyDetail.getOrder_id()); // 作業コード
            line.setPlace(applyDetail.getPlace()); // 出張場所
            line.setPurpose(applyDetail.getPurpose()); // 出張目的
            line.setMeansId(Integer.parseInt(applyDetail.getMeans_id())); // 交通手段コード
            line.setSectionFrom(applyDetail.getSection_from()); // 出発地
            line.setSectionTo(applyDetail.getSection_to()); // 到着地
            line.setIsRoundtrip(Integer.parseInt(applyDetail.getIs_roundtrip())); // 往復フラグ
            line.setFare(Long.parseLong(applyDetail.getFare())); // 料金
            line.setMemo(applyDetail.getMemo()); // 備考
            line.setSortNo(sortNo); // ソート番号
            lines.add(line);
            sortNo += 1;
            totalFare += Long.parseLong(applyDetail.getFare());
        }
        TApplication application = new TApplication();
        application.setStatus(Constants.STS_WAIT); // ステータス
        application.setApplyId(empNo); // 申請者ID
        application.setApplyDate(new Date()); // 申請日
        application.setApproveId(mEmployeeFacade.getBossId(empNo)); // 承認者ID
        application.setTotalFare(totalFare); // 料金合計
        application.setLines(lines); // 申請明細
        tApplicationFacade.create(application);        
        // 正常終了
        responseDto.SetSuccessResult();
        return JsonUtils.makeJson(responseDto);
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
     * 認証されているかどうかを返す
     * @param empNo 社員番号
     * @param token トークン
     * @return 認証状態
     */
    private boolean isAuthed(Integer empNo, String token) {
        TAuth auth = tAuthFacade.getAuthInfo(empNo, token);
        // 認証情報なし
        if (auth == null) {
            return false;
        }
        // システム日付を取得
        String nowDate = DateUtils.getNowDate();
        // 有効期限を取得 
        String expire = auth.getExpire();
        // 有効期限切れ
        if (expire == null || (Long.parseLong(expire) < Long.parseLong(nowDate))) {
            return false;
        }
        return true;
    }
    
}
