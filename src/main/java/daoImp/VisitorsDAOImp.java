package daoImp;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import daoInterface.VisitorsDAO;
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
            return entityManager.createQuery("SELECT v FROM Visitors v WHERE v.studentId.id = :studentId",Visitors.class)
                    .setParameter("studentId", userId)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Visitors> getAllNotExitVistior(){
        try{
            return entityManager.createQuery("SELECT v FROM Visitors v WHERE v.exitDatetime IS NULL",Visitors.class)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

}
