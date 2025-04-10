package daoImp;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import daoInterface.VisitorsDAO;
import model.Visitors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Dependent
public class VisitorsDAOImp extends BaseDAOImp<Visitors> implements VisitorsDAO, Serializable {

    @PersistenceContext(unitName = "hostelmanagement")
    private EntityManager entityManager;


    public VisitorsDAOImp(){
        super(Visitors.class);
    }

    @Override
    public List<Visitors> getUserVisitedBy(Long userId){
        if (entityManager == null) {
            return Collections.emptyList();
        }

        try{
            return entityManager.createQuery("SELECT v FROM Visitors v WHERE v.studentId.id = :studentId ORDER BY v.entryDatetime DESC",Visitors.class)
                    .setParameter("studentId", userId)
                    .getResultList();
        }catch (NoResultException e){
            return Collections.emptyList();
        }
    }

    @Override
    public Long userTotalVisitor(Long userId) {
        if (entityManager == null) {
            return 0L;
        }
        try{
            Long result = entityManager.createQuery("SELECT COUNT(v) FROM Visitors v WHERE v.studentId.id = :userId", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            return 0L;
        }
    }
}
