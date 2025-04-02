package daoImp;

import daoInterface.BaseDAO;
import utils.EntityManageUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.util.Collections;
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
        if (entityManager == null) {
            return status;
        }

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
        if (entityManager == null) {
            return status;
        }

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
        if (entityManager == null) {
            return status;
        }

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
        if (entityManager == null) {
            return null;
        }

        try{
            return entityManager.find(entityClass ,id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve entity by ID: " + id, e);
        }
    }

    @Override
    public List<T> getAll() {
        if (entityManager == null) {
            return Collections.emptyList();
        }

        try {
            return entityManager.createQuery("SELECT e FROM " + entityClass.getName() + " e", entityClass)
                    .getResultList();
        }catch (PersistenceException e){
            return Collections.emptyList();
        }
    }

    @Override
    public Long getCount(){
        if (entityManager == null) {
            return 0L;
        }
        try{
            Long result = entityManager.createQuery("SELECT COUNT(*) FROM "+ entityClass.getName() +" e",Long.class)
                    .getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {

            return 0L;
        }
    }
}
