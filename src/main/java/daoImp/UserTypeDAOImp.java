package daoImp;

import daoInterface.UserTypeDAO;
import model.UserType;

public class UserTypeDAOImp extends BaseDAOImp<UserType> implements UserTypeDAO {
    public UserTypeDAOImp(){
        super(UserType.class);
    }
}
