package jp.co.stcinc.api.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jp.co.stcinc.api.entity.TApplication;

/**
 * 申請情報操作
 */
@Stateless
public class TApplicationFacade extends AbstractFacade<TApplication> {

    @PersistenceContext
    private EntityManager em;

    /**
     * コンストラクタ
     */
    public TApplicationFacade() {
        super(TApplication.class);
    }

    /**
     * Entityマネージャ取得
     * @return Entityマネージャ
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
