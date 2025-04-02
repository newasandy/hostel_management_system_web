package daoImp;

import daoInterface.TransactionStatementDAO;
import model.MonthlyFee;
import model.TransactionStatement;
import utils.EntityManageUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;

public class TransactionStatementDAOImp extends BaseDAOImp<TransactionStatement> implements TransactionStatementDAO {
    public TransactionStatementDAOImp(){
        super(TransactionStatement.class);
    }

    private EntityManager entityManager = EntityManageUtils.getEntityManager();
    private EntityTransaction entityTransaction  =entityManager.getTransaction();

    @Override
    public List<TransactionStatement> getStatementByEachUser(Long userId) {
        if (entityManager == null) {
            return Collections.emptyList();
        }

        try{
            return entityManager.createQuery("SELECT t FROM TransactionStatement t WHERE t.studentId.id = :studentId ORDER BY t.paymentDate DESC", TransactionStatement.class)
                    .setParameter("studentId",userId)
                    .getResultList();
        }catch (NoResultException e){
            return Collections.emptyList();
        }
    }

    @Override
    public List<TransactionStatement> getPendingPaymentRequest() {
        if (entityManager == null) {
            return Collections.emptyList();
        }
        try{
            return entityManager.createQuery("SELECT t FROM TransactionStatement t WHERE t.status = :status ORDER BY t.paymentDate DESC", TransactionStatement.class)
                    .setParameter("status", "PENDING")
                    .getResultList();
        }catch (NoResultException e){
            return Collections.emptyList();
        }
    }

    @Override
    public boolean paymentCompletedByAdmin(MonthlyFee selectedFee) {
        if (entityManager == null) {
            return false;
        }

        try{
            entityTransaction.begin();
            int updateRow = entityManager.createQuery("UPDATE TransactionStatement ts SET ts.status = 'FAILED' WHERE ts.feeId = :feeId AND ts.status = 'PENDING'")
                    .setParameter("feeId",selectedFee)
                    .executeUpdate();
            entityManager.flush();
            entityManager.clear();
            entityTransaction.commit();
            return updateRow >0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
