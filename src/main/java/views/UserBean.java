package views;

import daoImp.UserTypeDAOImp;
import daoInterface.UsersDAO;
import model.*;
import service.ActiveUserService;
import service.AuthenticationService;
import service.CooldownService;
import service.UserService;
import utils.JwtUtils;
import utils.PasswordUtils;
import utils.SessionUtils;
import views.stateModel.GenericLazyDataModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<String, Object> matchFilter;
    private HttpServletRequest request;

    @PostConstruct
    public void init() {
        try{
            matchFilter = new HashMap<>();
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

    public void refreshStudentList() {
        matchFilter.put("roles.userTypes","USER");
        userState.setOnlyStudent(new GenericLazyDataModel<>(usersDAO,matchFilter, false));
    }

    public void getAllUserType(){
        userState.setUserTypes(userTypeDAOImp.getAll());
    }

    public void searchList(){

    }

    public void refreshActiveUserList(){
        List<Users> usersList = usersDAO.getOnlyStudent();
        userState.setActiveUserlist(usersList
                .stream()
                .filter(users -> activeUserService.containsUser(users.getEmail()))
                .collect(Collectors.toList()));
    }

    public void registrationUser(){
        Users regUser = new Users();

        regUser.setFullName(userState.getName());
        regUser.setEmail(userState.getEmail());
        regUser.setPasswords(PasswordUtils.getHashPassword(userState.getPassword()));
        regUser.setRoles(userState.getSelectUserType());
        regUser.setStatus(true);

        Address regUserAddress = new Address();
        regUserAddress.setCountry(userState.getCountry());
        regUserAddress.setDistrict(userState.getDistrict());
        regUserAddress.setRmcMc(userState.getRmcMc());
        regUserAddress.setWardNo(userState.getWardNumber());
        regUserAddress.setUser(regUser);

        regUser.setAddress(regUserAddress);
        statusMessageModel = userService.registerNewStudent(regUser, userState.getSelectRoom());
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