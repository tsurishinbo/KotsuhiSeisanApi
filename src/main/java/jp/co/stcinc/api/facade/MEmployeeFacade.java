package jp.co.stcinc.api.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jp.co.stcinc.api.entity.MEmployee;

/**
 * 社員マスタ操作
 */
@Stateless
public class MEmployeeFacade extends AbstractFacade<MEmployee> {

    @PersistenceContext
    private EntityManager em;

    /**
     * コンストラクタ
     */
    public MEmployeeFacade() {
        super(MEmployee.class);
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
     * 社員を取得する
     * @param id 社員番号
     * @param password パスワード
     * @return 社員エンティティ
     */
    public MEmployee getEmployee(Integer id, String password) {
        MEmployee employee;
        Query query = em.createNamedQuery("MEmployee.findByIdAndPassword");
        query.setParameter("id", id);
        query.setParameter("password", password);
        try {
            employee = (MEmployee)query.getSingleResult();
        } catch (NoResultException e) {
            employee = null;
        }
        return employee;
    }
    
    /**
     * 上司の社員番号を取得する
     * @param id 社員番号
     * @return 上司の社員番号
     */
    public Integer getBossId(Integer id) {
        MEmployee employee;
        Query query = em.createNamedQuery("MEmployee.findById");
        query.setParameter("id", id);
        try {
            employee = (MEmployee)query.getSingleResult();
            return employee.getBossId();
        } catch (NoResultException e) {
            return null;
        }
    }
}
