package daoImp;

import daoInterface.TransactionStatementDAO;
import model.TransactionStatement;
import utils.EntityManageUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;

public class TransactionStatementDAOImp extends BaseDAOImp<TransactionStatement> implements TransactionStatementDAO {
    public TransactionStatementDAOImp(){
        super(TransactionStatement.class);
    }

    private EntityManager entityManager = EntityManageUtils.getEntityManager();

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
}
