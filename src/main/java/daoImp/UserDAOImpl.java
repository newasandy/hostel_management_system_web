package daoImp;

import daoInterface.UsersDAO;
import model.Users;
import utils.EntityManageUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class UserDAOImpl extends BaseDAOImp<Users> implements UsersDAO, Serializable {
    private static final long serialVersionUID = 1L;

    public UserDAOImpl(){
        super(Users.class);
    }

    private EntityManager entityManager = EntityManageUtils.getEntityManager();

    @Override
    public Users getByEmail(String email){
        try{
            return entityManager.createQuery("SELECT e FROM Users e WHERE e.email = :email", Users.class)
                    .setParameter("email",email)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Users> getUnallocatedUsers(){
        try{
            return entityManager.createQuery("SELECT u FROM Users u WHERE u.roles.userTypes = :roles AND u.status = TRUE AND u.id NOT IN (SELECT ra.studentId.id FROM RoomAllocation ra WHERE ra.unallocationDate IS NULL)", Users.class)
                    .setParameter("roles", "USER")
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Users> getOnlyStudent(){
        try{
            return entityManager.createQuery("SELECT u FROM Users u LEFT JOIN FETCH u.address WHERE u.roles.userTypes = :roles",Users.class)
                    .setParameter("roles", "USER")
                    .getResultList();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    @Override
    public Long getCountOnlyStudent() {
        return entityManager.createQuery("SELECT COUNT(u) FROM Users u WHERE u.roles.userTypes = :roles", Long.class)
                .setParameter("roles","USER")
                .getSingleResult();
    }
    @Override
    public Long getCountActiveStudent() {
        return entityManager.createQuery("SELECT COUNT(u) FROM Users u WHERE u.roles.userTypes = :roles AND u.status = true", Long.class)
                .setParameter("roles","USER")
                .getSingleResult();
    }
}
