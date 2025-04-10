package daoImp;


import model.UserType;

import javax.enterprise.context.Dependent;
import java.io.Serializable;

@Dependent
public class UserTypeDAOImp extends BaseDAOImp<UserType> implements Serializable {
    public UserTypeDAOImp(){
        super(UserType.class);
    }
}
