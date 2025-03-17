package daoImp;

import daoInterface.UsersDAO;
import model.Users;
import utils.EntityManageUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.security.PrivateKey;

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
}
