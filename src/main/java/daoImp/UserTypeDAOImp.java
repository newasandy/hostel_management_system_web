package daoImp;


import model.UserType;

import javax.ejb.Stateless;
import java.io.Serializable;

@Stateless
public class UserTypeDAOImp extends BaseDAOImp<UserType> implements Serializable {
    public UserTypeDAOImp(){
        super(UserType.class);
    }
}
