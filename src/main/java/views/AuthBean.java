package views;

import daoInterface.UsersDAO;
import model.Users;
import service.ActiveUserService;
import service.AuthenticationService;
import service.CooldownService;
import utils.JwtUtils;
import utils.SessionUtils;
import views.stateModel.UserState;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Named
@SessionScoped
public class AuthBean implements Serializable {

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private ActiveUserService activeUserService;

    @Inject
    private CooldownService cooldownService;

    private HttpServletRequest request;
    private UserState authState;

    @PostConstruct
    public void init(){
        authState = new UserState();
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        setUserRoleFromSession();
    }

    public String login(){
        if (activeUserService.containsUser(authState.getEmail())){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User Already login in another device"));
            return null;
        }
        LocalDateTime cooldownTime = cooldownService.getUserInCooldown(authState.getEmail());
        if (cooldownTime != null && cooldownTime.isAfter(LocalDateTime.now())){
            Duration remaining = Duration.between(LocalDateTime.now(),cooldownTime);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cooldown Active",
                            "Please wait " + getFormattedTime(remaining) + " before trying again"));
            return null;
        }

        Users user = authenticationService.loginService(authState.getEmail(), authState.getPassword());
        if (user == null){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid email or password"));
            return null;
        }
        if(user.isStatus()){
            authState.resetFields();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Welcome,"+user.getFullName()));
            HttpSession session = SessionUtils.storeToken(request,JwtUtils.generateToken(user.getEmail(), user.getRoles().getUserTypes()));
            activeUserService.addUser(user.getEmail(),session);
            setUserRoleFromSession();
            if ("USER".equals(user.getRoles().getUserTypes())) {
                return "/users/userDashboard.xhtml?faces-redirect=true";
            } else if ("ADMIN".equals(user.getRoles().getUserTypes())) {
                return "/admin/adminDashboard.xhtml?faces-redirect=true";
            }
            return "/index.xhtml?faces-redirect=true";
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User is Deactivated"));
            return null;
        }
    }

    public String logout(){
        activeUserService.removeUser(JwtUtils.getUserEmail(SessionUtils.getToken(request)));
        SessionUtils.removeToken(request);
        setUserRoleFromSession();
        return "/index.xhtml?faces-redirect=true";
    }

    public String getFormattedTime(Duration duration){
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        long hours = absSeconds / 3600;
        long minutes = (absSeconds % 3600) / 60;
        long secs = absSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    private void setUserRoleFromSession() {
        if (SessionUtils.isSessionValid(request)){
            String token = SessionUtils.getToken(request);
            if (JwtUtils.isTokenValid(token)){
                authState.setUserRole(JwtUtils.getUserRole(token));
            }else {
                authState.setUserRole("GUEST");
            }
        }
        if (!SessionUtils.isSessionValid(request) || !JwtUtils.isTokenValid(SessionUtils.getToken(request))) {
            authState.setUserRole("GUEST");
        }
    }

    public UserState getAuthState() {
        return authState;
    }
}
