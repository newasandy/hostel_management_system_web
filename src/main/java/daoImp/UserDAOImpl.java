package daoImp;

import daoInterface.UsersDAO;
import model.Users;
import utils.EntityManageUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class UserDAOImpl extends BaseDAOImp<Users> implements UsersDAO {
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
            return entityManager.createQuery("SELECT u FROM Users u WHERE u.roles = :roles AND u.id NOT IN (SELECT ra.studentId.id FROM RoomAllocation ra WHERE ra.unallocationDate IS NULL)", Users.class)
                    .setParameter("roles", "USER")
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Users> getOnlyStudent(){
        try{
            return entityManager.createQuery("SELECT u FROM Users u LEFT JOIN FETCH u.address WHERE u.roles = :roles",Users.class)
                    .setParameter("roles", "USER")
                    .getResultList();
        }catch (Exception e){
            return null;
        }
    }
}
