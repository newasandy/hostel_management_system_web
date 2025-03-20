package views;

import daoImp.UserDAOImpl;
import daoInterface.UsersDAO;
import model.Users;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ViewStudentBean implements Serializable {
    private UsersDAO usersDAO = new UserDAOImpl();
    private List<Users> onlyStudent;
    private String searchItem;

    @PostConstruct
    public void init(){
        onlyStudent = usersDAO.getOnlyStudent();
    }

    public void searchList(){
        if (searchItem == null || searchItem.isEmpty()){
            refreshStudentList();
        }else {
            String lowerSearch = searchItem.toLowerCase();
            onlyStudent = onlyStudent.stream().filter(users -> users.getFullName().toLowerCase().contains(lowerSearch)).collect(Collectors.toList());
        }
    }

    public void refreshStudentList() {
        onlyStudent = usersDAO.getOnlyStudent();
    }

    public List<Users> getOnlyStudent() {
        return onlyStudent;
    }

    public String getSearchItem() {
        return searchItem;
    }

    public void setSearchItem(String searchItem) {
        this.searchItem = searchItem;
    }
}
