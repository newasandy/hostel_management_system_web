package daoImp;

import daoInterface.BaseDAO;
import utils.EntityManageUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Named
@ApplicationScoped
public abstract class BaseDAOImp <T> implements BaseDAO<T>  {
    private final Class<T> entityClass;

    public BaseDAOImp(Class<T> entity){
        this.entityClass=entity;
    }


    private EntityManager entityManager = EntityManageUtils.getEntityManager();


    @Override
    public boolean add(T entity){
        boolean status = false;
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();
            entityManager.persist(entity);
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

    @Override
    public Long getCount(){
        return entityManager.createQuery("SELECT COUNT(*) FROM "+ entityClass.getName() +" e",Long.class)
                .getSingleResult();
    }
}
