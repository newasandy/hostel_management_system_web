package views;

import daoImp.UserDAOImpl;
import daoInterface.UsersDAO;
import model.Users;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ViewStudentBean implements Serializable {
    private UsersDAO usersDAO = new UserDAOImpl();
    private List<Users> onlyStudent;

    @PostConstruct
    public void init(){
        onlyStudent = usersDAO.getOnlyStudent();
    }



    public void refreshStudentList() {
        onlyStudent = usersDAO.getOnlyStudent();
    }

    public List<Users> getOnlyStudent() {
        return onlyStudent;
    }
}
