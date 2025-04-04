package service;

import daoInterface.UsersDAO;
import model.Users;
import utils.PasswordUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

@RequestScoped
public class AuthenticationService {

    @Inject
    private UsersDAO usersDAO;

    public Users loginService(String email, String password) {
        try{
            Users user = usersDAO.getByEmail(email);
            if (user == null) {
                return null;
            }
            if (!PasswordUtils.verifyPassword(password, user.getPasswords())) {
                return null;
            }
            return user;
        }catch (PersistenceException e){
            throw new RuntimeException("Database error occurred while logging in.",e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred during login.",e);
        }
    }
}
