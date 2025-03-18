package daoImp;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
            return entityManager.createQuery("SELECT m FROM MonthlyFee m WHERE m.studentId.id = :studentId", MonthlyFee.class)
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
}