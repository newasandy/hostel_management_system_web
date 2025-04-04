package daoImp;

import daoInterface.BaseDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;


public abstract class BaseDAOImp <T> implements BaseDAO<T>, Serializable {
    private final Class<T> entityClass;

    public BaseDAOImp(Class<T> entity){
        this.entityClass=entity;
    }

    @Inject
    private EntityManager entityManager;


    @Override
    public boolean add(T entity){
        boolean status = false;
        if (entityManager == null) {
            return status;
        }
        try{
            entityManager.persist(entity);
            status = true;
        }catch (Exception e){
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

        try{
            entityManager.merge(entity);
            status = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public boolean delete(Long id){
        boolean status = false;
        if (entityManager == null) {
            return status;
        }

        try{
            T entity = entityManager.find(entityClass , id);
            if (entity != null){
                entityManager.remove(entity);
            }
            status = true;
        }catch (Exception e){
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
