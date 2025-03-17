package controller;

import daoImp.UserDAOImpl;
import daoInterface.UsersDAO;
import model.StatusMessageModel;
import model.Users;
import service.AuthenticationService;
import service.UserService;
import utils.PasswordUtils;

public class UserController {
    private StatusMessageModel statusMessageModel = new StatusMessageModel();
    private final UsersDAO usersDAO = new UserDAOImpl();
    private final AuthenticationService authenticationService = new AuthenticationService(usersDAO);
    private final UserService userService = new UserService(usersDAO);

    public StatusMessageModel registerUser(String name, String email , String password, String role){

        Users regUser = new Users();
        regUser.setFullName(name);
        regUser.setEmail(email);
        regUser.setPasswords(PasswordUtils.getHashPassword(password));
        regUser.setRoles(role);
        regUser.setStatus(true);
        return userService.registerNewStudent(regUser);
    }

    public Users loginUser(String email, String password){
        return authenticationService.loginService(email,password);
    }
}
