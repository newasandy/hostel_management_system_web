package views;

import daoInterface.UsersDAO;
import model.Users;
import views.stateModel.UserState;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ViewStudentBean implements Serializable {

    @Inject
    private UsersDAO usersDAO;

    private UserState userState;

    @PostConstruct
    public void init(){
        userState = new UserState();
        refreshStudentList();
    }

    public void searchList(){
        if (userState.getSearchItem() == null || userState.getSearchItem().isEmpty()){
            refreshStudentList();
        }else {
            String lowerSearch = userState.getSearchItem().toLowerCase();
            List<Users> originalStudentList = usersDAO.getOnlyStudent();
            userState.setOnlyStudent(originalStudentList.stream().filter(users -> users.getFullName().toLowerCase().contains(lowerSearch) || (users.getEmail() != null && users.getEmail().toLowerCase().contains(lowerSearch))).collect(Collectors.toList()));
        }
    }

    public void refreshStudentList() {
        userState.setOnlyStudent(usersDAO.getOnlyStudent());
    }

    public UserState getUserState() {
        return userState;
    }
}
