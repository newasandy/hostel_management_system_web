package daoImp;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import daoInterface.VisitorsDAO;
import model.LeaveRequest;
import model.Visitors;
import utils.EntityManageUtils;

import java.util.List;

public class VisitorsDAOImp extends BaseDAOImp<Visitors> implements VisitorsDAO {

    private EntityManager entityManager = EntityManageUtils.getEntityManager();


    public VisitorsDAOImp(){
        super(Visitors.class);
    }

    @Override
    public List<Visitors> getUserVisitedBy(Long userId){
        try{
            return entityManager.createQuery("SELECT v FROM Visitors v WHERE v.studentId.id = :studentId ORDER BY v.entryDatetime DESC",Visitors.class)
                    .setParameter("studentId", userId)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Visitors getRecentUserVisitor(Long userId) {
        try{
            return entityManager.createQuery("SELECT v FROM Visitors v WHERE v.studentId.id = :studentId ORDER BY v.entryDatetime DESC", Visitors.class)
                    .setParameter("studentId", userId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
