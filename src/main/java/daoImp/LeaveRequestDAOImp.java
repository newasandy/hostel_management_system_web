package daoImp;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import daoInterface.LeaveRequestDAO;
import model.LeaveRequest;
import utils.EntityManageUtils;

import java.util.List;


public class LeaveRequestDAOImp extends BaseDAOImp<LeaveRequest> implements LeaveRequestDAO {

    private EntityManager entityManager = EntityManageUtils.getEntityManager();


    public LeaveRequestDAOImp(){
        super(LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> getUserLeaveRequestByUserId(Long userId){
        try{
            return entityManager.createQuery("SELECT lr FROM LeaveRequest lr WHERE lr.studentId.id = :studentId ORDER BY lr.applyDate DESC", LeaveRequest.class)
                    .setParameter("studentId", userId)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public LeaveRequest checkLeaveRequest(Long userId){
        try{
            return entityManager.createQuery("SELECT lr FROM LeaveRequest lr WHERE lr.studentId.id = :studentId and lr.status = :status", LeaveRequest.class)
                    .setParameter("studentId",userId)
                    .setParameter("status", LeaveRequest.Status.PENDING)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }


    @Override
    public LeaveRequest getRecentLeaveRequest(Long userId) {
        try{
            return entityManager.createQuery("SELECT l FROM LeaveRequest l WHERE l.studentId.id = :studentId ORDER BY l.applyDate DESC",LeaveRequest.class)
                    .setParameter("studentId", userId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}