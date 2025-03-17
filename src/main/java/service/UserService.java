package service;

import daoInterface.UsersDAO;
import model.StatusMessageModel;
import model.Users;

public class UserService {
    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    private UsersDAO usersDAO;

    public UserService(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public StatusMessageModel registerNewStudent(Users registerStudent){
        Users checkUser = usersDAO.getByEmail(registerStudent.getEmail());
        if (checkUser == null){
            usersDAO.add(registerStudent);
            statusMessageModel.setStatus(true);
            statusMessageModel.setMessage("User Register Successfully");
        }else {
            statusMessageModel.setStatus(false);
            statusMessageModel.setMessage("User Already Exist");
        }
        return statusMessageModel;
    }
}
