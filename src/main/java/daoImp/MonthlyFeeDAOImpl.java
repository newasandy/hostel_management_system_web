package daoImp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import daoInterface.MonthlyFeeDAO;
import model.MonthlyFee;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class MonthlyFeeDAOImpl extends BaseDAOImp<MonthlyFee> implements MonthlyFeeDAO {

    @Inject
    private EntityManager entityManager;

    public MonthlyFeeDAOImpl(){
        super(MonthlyFee.class);
    }

    @Override
    public List<MonthlyFee> getUserFeeDetails(Long userId){
        if (entityManager == null) {
            return Collections.emptyList();
        }

        try{
            return entityManager.createQuery("SELECT m FROM MonthlyFee m WHERE m.studentId.id = :studentId ORDER BY m.issueDate DESC", MonthlyFee.class)
                    .setParameter("studentId", userId)
                    .getResultList();
        }catch (NoResultException e){
            return Collections.emptyList();
        }
    }

    @Override
    public MonthlyFee checkAssignFee(Long studentId, String month, int years) {
        if (entityManager == null) {
            return null;
        }

        try{
            return entityManager.createQuery("SELECT m FROM MonthlyFee m WHERE m.studentId.id = :studentId AND m.month = :month AND m.year = :years", MonthlyFee.class)
                    .setParameter("studentId", studentId)
                    .setParameter("month",month).setParameter("years",years)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public double getTotalDueAmount(Long userId) {
        if (entityManager == null) {
            return 0.0;
        }

        try{
            Double result = entityManager.createQuery(
                            "SELECT COALESCE(SUM(m.due), 0.0) FROM MonthlyFee m WHERE m.studentId.id = :studentId",
                            Double.class)
                    .setParameter("studentId", userId)
                    .getSingleResult();

            return result != null ? result : 0.0;
        } catch (NoResultException e) {
            return 0.0;
        }
    }
}