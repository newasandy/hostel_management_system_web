package views;

import daoInterface.UsersDAO;
import model.Users;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ViewStudentBean implements Serializable {

    @Inject
    private UsersDAO usersDAO;

    private List<Users> onlyStudent;
    private List<Users> originalStudentList;
    private String searchItem;

    @PostConstruct
    public void init(){
        originalStudentList = usersDAO.getOnlyStudent();
        onlyStudent = new ArrayList<>(originalStudentList);
    }

    public void searchList(){
        if (searchItem == null || searchItem.isEmpty()){
            refreshStudentList();
        }else {
            String lowerSearch = searchItem.toLowerCase();
            onlyStudent = originalStudentList.stream().filter(users -> users.getFullName().toLowerCase().contains(lowerSearch) || (users.getEmail() != null && users.getEmail().toLowerCase().contains(lowerSearch))).collect(Collectors.toList());
        }
    }

    public void refreshStudentList() {
        originalStudentList = usersDAO.getOnlyStudent();
        onlyStudent = new ArrayList<>(originalStudentList);
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
