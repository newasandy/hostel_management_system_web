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
            return entityManager.createQuery("SELECT lr FROM LeaveRequest lr WHERE lr.studentId.id = :studentId", LeaveRequest.class)
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
                    .setParameter("status", "PENDING")
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<LeaveRequest> getAllPendingRequest(){
        try{
            return entityManager.createQuery("SELECT lr FROM LeaveRequest lr WHERE lr.status= :status",LeaveRequest.class)
                    .setParameter("status","PENDING")
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }
}