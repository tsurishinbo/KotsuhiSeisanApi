package jp.co.stcinc.api.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    /**
     * 交通手段を取得する
     * @param id 交通手段コード
     * @return 交通手段エンティティ
     */
    public MMeans getMeans(Integer id) {
        MMeans means;
        Query query = em.createNamedQuery("MMeans.findById");
        query.setParameter("id", id);
        try {
            means = (MMeans)query.getSingleResult();
        } catch (NoResultException e) {
            means = null;
        }
        return means;
    }
    
}
