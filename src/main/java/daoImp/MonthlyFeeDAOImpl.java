package daoImp;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import daoInterface.MonthlyFeeDAO;
import model.MonthlyFee;
import utils.EntityManageUtils;

import java.util.List;

public class MonthlyFeeDAOImpl extends BaseDAOImp<MonthlyFee> implements MonthlyFeeDAO {

    private EntityManager entityManager = EntityManageUtils.getEntityManager();

    public MonthlyFeeDAOImpl(){
        super(MonthlyFee.class);
    }

    @Override
    public List<MonthlyFee> getUserFeeDetails(Long userId){
        try{
            return entityManager.createQuery("SELECT m FROM MonthlyFee m WHERE m.studentId.id = :studentId ORDER BY m.issueDate DESC", MonthlyFee.class)
                    .setParameter("studentId", userId)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<MonthlyFee> getUserUnPaidFee(Long userId){
        try{
            return entityManager.createQuery("SELECT m FROM MonthlyFee m WHERE m.studentId.id = :studentId AND m.paid < m.feeAmount", MonthlyFee.class)
                    .setParameter("studentId", userId)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<MonthlyFee> getAllUserUnPaidFee(){
        try{
            return entityManager.createQuery("SELECT m FROM MonthlyFee m WHERE m.paid < m.feeAmount", MonthlyFee.class)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public MonthlyFee checkAssignFee(Long studentId, String month, int years) {
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