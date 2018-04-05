package jp.co.stcinc.api.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jp.co.stcinc.api.entity.MMeans;

/**
 * 交通手段マスタ操作
 */
@Stateless
public class MMeansFacade extends AbstractFacade<MMeans> {

    @PersistenceContext
    private EntityManager em;

    /**
     * コンストラクタ
     */
    public MMeansFacade() {
        super(MMeans.class);
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
