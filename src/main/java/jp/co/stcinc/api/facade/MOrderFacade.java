package jp.co.stcinc.api.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jp.co.stcinc.api.entity.MOrder;

/**
 * 作業マスタ操作
 */
@Stateless
public class MOrderFacade extends AbstractFacade<MOrder> {

    @PersistenceContext
    private EntityManager em;

    /**
     * コンストラクタ
     */
    public MOrderFacade() {
        super(MOrder.class);
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
     * 作業を取得する
     * @param id 作業コード
     * @return 作業エンティティ
     */
    public MOrder getOrder(String id) {
        MOrder order;
        Query query = em.createNamedQuery("MOrder.findById");
        query.setParameter("id", id);
        try {
            order = (MOrder)query.getSingleResult();
        } catch (NoResultException e) {
            order = null;
        }
        return order;
    }

}
