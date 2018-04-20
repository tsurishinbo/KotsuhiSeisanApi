package jp.co.stcinc.api.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jp.co.stcinc.api.entity.TAuth;

/**
 * 認証情報操作
 */
@Stateless
public class TAuthFacade extends AbstractFacade<TAuth> {

    @PersistenceContext
    private EntityManager em;

    /**
     * コンストラクタ
     */
    public TAuthFacade() {
        super(TAuth.class);
    }

    /**
     * Entityマネージャ取得
     * @return Entityマネージャ
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * 認証情報を取得する
     * @param empNo 社員番号
     * @return 認証情報
     */
    public TAuth getAuthInfo(Integer empNo) {
        TAuth authInfo;
        Query query = em.createNamedQuery("TAuth.findByEmpNo");
        query.setParameter("empNo", empNo);
        try {
            authInfo = (TAuth)query.getSingleResult();
        } catch (NoResultException e) {
            authInfo = null;
        }
        return authInfo;
    }

    /**
     * 認証情報を取得する
     * @param token トークン
     * @return 認証情報
     */
    public TAuth getAuthInfoByToken(String token) {
        TAuth authInfo;
        Query query = em.createNamedQuery("TAuth.findByToken");
        query.setParameter("token", token);
        try {
            authInfo = (TAuth)query.getSingleResult();
        } catch (NoResultException e) {
            authInfo = null;
        }
        return authInfo;
    }
}
