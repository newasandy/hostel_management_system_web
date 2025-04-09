package views;

import daoInterface.UsersDAO;
import model.Users;
import service.AuthenticationService;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.UserState;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@SessionScoped
public class AuthBean implements Serializable {

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private AuthenticationService authenticationService;

    private HttpServletRequest request;
    private UserState authState;

    @PostConstruct
    public void init(){
        authState = new UserState();
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        setUserRoleFromSession();
    }

    public String login(){
        Users user = authenticationService.loginService(authState.getEmail(), authState.getPassword());
        if (user != null){
            if(user.isStatus()){
                authState.resetFields();
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

    public String logout(){
        SessionUtils.removeToken(request);
        setUserRoleFromSession();
        return "/index.xhtml?faces-redirect=true";
    }

    @PreDestroy
    public void destroySession(){
        setUserRoleFromSession();
    }

    private void setUserRoleFromSession() {
        String token = SessionUtils.getToken(request);
        if (token != null){
            authState.setUserRole(JwtUtils.getUserRole(token));
        }else {
            authState.setUserRole("GUEST");
        }
    }

    public UserState getAuthState() {
        return authState;
    }
}
