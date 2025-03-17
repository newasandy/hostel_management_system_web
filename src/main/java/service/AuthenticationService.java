package service;


import daoInterface.UsersDAO;
import model.Users;
import utils.PasswordUtils;

public class AuthenticationService {
    private UsersDAO usersDAO;

    public AuthenticationService(UsersDAO userDAO) {
        this.usersDAO = userDAO;
    }

    public Users loginService(String email, String password){
        Users user = usersDAO.getByEmail(email);
        if (user != null){
            if (PasswordUtils.verifyPassword(password,user.getPasswords())){
                return user;

            }else {
                return null;
            }
        }else
            return null;
        }

    }
