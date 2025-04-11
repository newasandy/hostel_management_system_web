package views;

import daoImp.UserTypeDAOImp;
import daoInterface.UsersDAO;
import model.*;
import service.ActiveUserService;
import service.AuthenticationService;
import service.CooldownService;
import service.UserService;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.StatusMessageModel;
import views.stateModel.UserState;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class UserBean implements Serializable{

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private UserTypeDAOImp userTypeDAOImp;

    @Inject
    private UserService userService;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private ActiveUserService activeUserService;

    @Inject
    private CooldownService cooldownService;

    private StatusMessageModel statusMessageModel;
    private UserState userState;
    private HttpServletRequest request;

    @PostConstruct
    public void init() {
        try{
            statusMessageModel = new StatusMessageModel();
            userState = new UserState();
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            setUserRoleFromSession();
            refreshStudentList();
            refreshActiveUserList();
            getAllUserType();
        } catch (Exception e) {
            throw new RuntimeException("Failed ti init",e);
        }

    }

    public void getAllUserType(){
        userState.setUserTypes(userTypeDAOImp.getAll());
    }

    public void searchList(){
        if (userState.getSearchItem() == null || userState.getSearchItem().isEmpty()){
            refreshStudentList();
        }else {
            String lowerSearch = userState.getSearchItem().toLowerCase();
            List<Users> originalStudentList = usersDAO.getOnlyStudent();
            userState.setOnlyStudent(originalStudentList.stream().filter(users -> users.getFullName()
                    .toLowerCase()
                    .contains(lowerSearch) || (users.getEmail() != null && users.getEmail()
                    .toLowerCase()
                    .contains(lowerSearch)))
                    .collect(Collectors.toList()));
        }
    }

    public void refreshStudentList() {
        userState.setOnlyStudent(usersDAO.getOnlyStudent());

    }

    public void refreshActiveUserList(){
        userState.setActiveUserlist(userState.getOnlyStudent()
                .stream()
                .filter(users -> activeUserService.containsUser(users.getEmail()))
                .collect(Collectors.toList()));
    }

    public void registrationUser(){
        statusMessageModel = userService.registerNewStudent(userState.getName(),userState.getEmail(),userState.getPassword(),userState.getSelectUserType(),userState.getCountry(),userState.getDistrict(),userState.getRmcMc(),userState.getWardNumber(), userState.getSelectRoom());
        userState.resetFields();
        try {
            if (statusMessageModel.isStatus()){
                refreshStudentList();
                showMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage());
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage());
            }
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to register user.");
        }

    }

    public void deactivateStudent(Users student) {
        student.setStatus(false);
        try {
            if (userService.updateStudent(student)){
                showMessage(FacesMessage.SEVERITY_INFO, "Success", "User Deactivated successfully!");
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Deactivated");
            }
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to Deactivated user.");
        }
    }

    public void activateStudent(Users student) {
        student.setStatus(true);
        try {
            if (userService.updateStudent(student)){
                showMessage(FacesMessage.SEVERITY_INFO, "Success", "User Activated successfully!");
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Activated");
            }
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to Activated user.");
        }
    }

    public void prepareUpdateStudent(Users student) {
        this.userState.setSelectUser(student);
    }

    public void updateUser(){
        try {
            if (userService.updateStudent(userState.getSelectUser())){
                showMessage(FacesMessage.SEVERITY_INFO, "Success", "Update Successfully");
            }else {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update");
            }
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update user.");
        }
    }

    public void prepareUserSessionKill(){
        userState.setEmail(FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("studentEmail"));
    }


    public void killUserSession(){
        HttpSession session = activeUserService.getuserSession(userState.getEmail());
        if (session != null){
            session.invalidate();
            cooldownService.setUserSessionCooldown(userState.getEmail(), userState.getCdTime());
            activeUserService.removeUser(userState.getEmail());
            refreshActiveUserList();
        }
    }

    private void showMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    private void setUserRoleFromSession() {
        String token = SessionUtils.getToken(request);
        if (token != null){
            userState.setUserRole(JwtUtils.getUserRole(token));
        }else {
            userState.setUserRole("GUEST");
        }
    }

    public UserState getUserState() {
        return userState;
    }

    public void cancelBtn(){
        userState.resetFields();
    }


}