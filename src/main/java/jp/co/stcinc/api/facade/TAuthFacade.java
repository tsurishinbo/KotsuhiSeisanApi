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
     * 有効期限内の認証情報を取得する
     * @param token トークン
     * @return 認証情報
     */
    public TAuth getValidToken(String token) {
        TAuth authInfo;
        StringBuilder sql = new StringBuilder();
        sql.append("select * ");
        sql.append("from t_auth ");
        sql.append("where token = ?token ");
        sql.append("and expire >= current_timestamp ");
        Query query = em.createNativeQuery(sql.toString(), TAuth.class);
        query.setParameter("token", token);
        try {
            authInfo = (TAuth)query.getSingleResult();
        } catch (NoResultException e) {
            authInfo = null;
        }
        return authInfo;
    }

    /**
     * 有効期限切れの認証情報を取得する
     * @param token トークン
     * @return 認証情報
     */
    public TAuth getInvalidToken(String token) {
        TAuth authInfo;
        StringBuilder sql = new StringBuilder();
        sql.append("select * ");
        sql.append("from t_auth ");
        sql.append("where token = ?token ");
        sql.append("and expire < current_timestamp ");
        Query query = em.createNativeQuery(sql.toString(), TAuth.class);
        query.setParameter("token", token);
        try {
            authInfo = (TAuth)query.getSingleResult();
        } catch (NoResultException e) {
            authInfo = null;
        }
        return authInfo;
    }
    
}
