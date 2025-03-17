package daoImp;

import daoInterface.BaseDAO;
import utils.EntityManageUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public abstract class BaseDAOImp <T> implements BaseDAO<T> {
    private final Class<T> entityClass;

    public BaseDAOImp(Class<T> entity){
        this.entityClass=entity;
    }

//    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hostelmanagement");
    private EntityManager entityManager = EntityManageUtils.getEntityManager();


    @Override
    public boolean add(T entity){
        boolean status = false;
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();
            entityManager.persist(entity);
            transaction.commit();
            status = true;
        }catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        return status;
    }
    @Override
    public boolean update(T entity){
        boolean status = false;
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();
            entityManager.merge(entity);
            entityManager.flush();
            entityManager.clear();
            transaction.commit();
            status = true;
        }catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        return status;
    }
    @Override
    public boolean delete(Long id){
        EntityTransaction transaction = entityManager.getTransaction();
        boolean status = false;
        try{
            transaction.begin();
            T entity = entityManager.find(entityClass , id);
            if (entity != null){
                entityManager.remove(entity);
            }
            transaction.commit();
            status = true;
        }catch (Exception e){
            transaction.rollback();
            e.printStackTrace();
        }
        return status;
    }
    @Override
    public T getById(Long id){
        return entityManager.find(entityClass ,id);
    }

    @Override
    public List<T> getAll() {
        return entityManager.createQuery("SELECT e FROM "+ entityClass.getName() + " e",entityClass)
                .getResultList();
    }
}
