package daoImp;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import daoInterface.LeaveRequestDAO;
import model.LeaveRequest;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Stateless
public class LeaveRequestDAOImp extends BaseDAOImp<LeaveRequest> implements LeaveRequestDAO, Serializable {

    @PersistenceContext(unitName = "hostelmanagement")
    private EntityManager entityManager;


    public LeaveRequestDAOImp(){
        super(LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> getUserLeaveRequestByUserId(Long userId){
        if (entityManager == null) {
            return Collections.emptyList();
        }

        try{
            return entityManager.createQuery("SELECT lr FROM LeaveRequest lr WHERE lr.studentId.id = :studentId ORDER BY lr.applyDate DESC", LeaveRequest.class)
                    .setParameter("studentId", userId)
                    .getResultList();
        }catch (NoResultException e){
            return Collections.emptyList();
        }
    }

    @Override
    public LeaveRequest checkLeaveRequest(Long userId){
        if (entityManager == null) {
            return null;
        }

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
        if (entityManager == null) {
            return null;
        }

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