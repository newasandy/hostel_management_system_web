package utils;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Dependent
public class EntityManageUtils {

    @PersistenceContext(unitName = "hostelmanagement")
    private EntityManager entityManager;

    @Produces
    @Dependent
    public EntityManager produceEntityManager(){
        return entityManager;
    }

}
