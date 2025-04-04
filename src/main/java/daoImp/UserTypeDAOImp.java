package daoImp;

import daoInterface.UserTypeDAO;
import model.UserType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserTypeDAOImp extends BaseDAOImp<UserType> implements UserTypeDAO {
    public UserTypeDAOImp(){
        super(UserType.class);
    }
}
