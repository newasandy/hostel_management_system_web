package daoImp;

import daoInterface.UserTypeDAO;
import model.UserType;

import javax.enterprise.context.Dependent;
import java.io.Serializable;

@Dependent
public class UserTypeDAOImp extends BaseDAOImp<UserType> implements UserTypeDAO, Serializable {
    public UserTypeDAOImp(){
        super(UserType.class);
    }
}
