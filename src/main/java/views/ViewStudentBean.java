package views;

import daoImp.UserDAOImpl;
import daoInterface.UsersDAO;
import model.Users;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class ViewStudentBean implements Serializable {
    private UsersDAO usersDAO = new UserDAOImpl();
    private List<Users> onlyStudent = usersDAO.getOnlyStudent();

    public List<Users> getOnlyStudent() {
        return onlyStudent;
    }
}
