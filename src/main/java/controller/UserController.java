package controller;

import daoImp.UserDAOImpl;
import daoInterface.UsersDAO;
import model.Users;

public class UserController {

    public boolean registerUser(String name, String email , String password, String role){
        UsersDAO usersDAO = new UserDAOImpl();
        Users regUser = new Users();
        regUser.setFullName(name);
        regUser.setEmail(email);
        regUser.setPasswords(password);
        regUser.setRoles(role);
        return usersDAO.add(regUser);
    }
}
