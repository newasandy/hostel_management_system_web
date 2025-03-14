package daoImp;

import daoInterface.UsersDAO;
import model.Users;

public class UserDAOImpl extends BaseDAOImp<Users> implements UsersDAO {
    public UserDAOImpl(){
        super(Users.class);
    }
}
