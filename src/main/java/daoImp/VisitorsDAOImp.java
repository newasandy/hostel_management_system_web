package daoImp;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import daoInterface.VisitorsDAO;
import model.Visitors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Dependent
public class VisitorsDAOImp extends BaseDAOImp<Visitors> implements VisitorsDAO, Serializable {

    @Inject
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
    public Visitors getRecentUserVisitor(Long userId) {
        if (entityManager == null) {
            return null;
        }

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
