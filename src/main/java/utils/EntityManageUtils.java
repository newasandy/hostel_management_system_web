package utils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManageUtils {

    @PersistenceContext(unitName = "hostelmanagement")
    private EntityManager entityManager;

    @Produces
    @ApplicationScoped
    public EntityManager produceEntityManager(){
        return entityManager;
    }

    public void closeEntityManagerFactory(@Disposes EntityManager entityManager) {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
