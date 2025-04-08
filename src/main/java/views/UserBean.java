package views;

import daoImp.UserTypeDAOImp;
import model.*;
import service.AuthenticationService;
import service.UserService;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.StatusMessageModel;
import views.stateModel.UserState;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@SessionScoped
public class UserBean implements Serializable{

    @Inject
    private ViewStudentBean viewStudentBean;

    @Inject
    private UserTypeDAOImp userTypeDAOImp;

    @Inject
    private UserService userService;

    @Inject
    private AuthenticationService authenticationService;

    private StatusMessageModel statusMessageModel;
    private UserState userState;
    private HttpServletRequest request;

    @PostConstruct
    public void init() {
        try {
            statusMessageModel = new StatusMessageModel();
            userState = new UserState();
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            loadUserTypes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize UserBean", e);
        }
    }

    public void loadUserTypes() {
        userState.setUserTypes(userTypeDAOImp.getAll());
    }

    public void registrationUser(){
        statusMessageModel = userService.registerNewStudent(userState.getName(),userState.getEmail(),userState.getPassword(),userState.getSelectUserType(),userState.getCountry(),userState.getDistrict(),userState.getRmcMc(),userState.getWardNumber(), userState.getSelectRoom());
        userState.resetFields();
        try {
            if (statusMessageModel.isStatus()){
                viewStudentBean.refreshStudentList();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage()));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage()));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to register user."));
        }

    }

    public String login(){
        Users user = authenticationService.loginService(userState.getEmail(),userState.getPassword());
        if (user != null){
            if(user.isStatus()){
                userState.resetFields();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Welcome,"+user.getFullName()));

                SessionUtils.storeToken(request,JwtUtils.generateToken(user.getEmail(), user.getRoles().getUserTypes()));

                setUserRoleFromSession();
                if ("USER".equals(user.getRoles().getUserTypes())) {
                    return "/users/userDashboard.xhtml?faces-redirect=true";
                } else if ("ADMIN".equals(user.getRoles().getUserTypes())) {
                    return "/admin/adminDashboard.xhtml?faces-redirect=true";
                }
            }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User is Deactivated"));

            return null;
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid email or password"));
            return null;
        }
    }

    public void deactivateStudent(Users student) {
        student.setStatus(false);
        try {
            if (userService.updateStudent(student)){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User Deactivated successfully!"));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Deactivated"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to Deactivated user."));
        }
    }

    public void activateStudent(Users student) {
        student.setStatus(true);
        try {
            if (userService.updateStudent(student)){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User Activated successfully!"));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Activated"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to Activated user."));
        }
    }

    public void prepareUpdateStudent(Users student) {
        this.userState.setSelectUser(student);
    }

    public void updateUser(){
        try {
            if (userService.updateStudent(userState.getSelectUser())){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Update Successfully"));
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Not Update"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update user."));
        }
    }

    public String logout(){
        SessionUtils.removeToken(request);
        setUserRoleFromSession();
        return "/index.xhtml?faces-redirect=true";
    }

    public UserState getUserState() {
        return userState;
    }



    private void setUserRoleFromSession() {
        String token = SessionUtils.getToken(request);
        if (token != null){
            userState.setUserRole(JwtUtils.getUserRole(token));
        }else {
            userState.setUserRole("GUEST");
        }
    }

    public void cancelBtn(){
        userState.resetFields();
    }


}