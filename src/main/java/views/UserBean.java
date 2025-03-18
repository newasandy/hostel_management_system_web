package views;

import controller.UserController;
import model.StatusMessageModel;
import model.Users;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class UserBean implements Serializable{
    private final UserController userController = new UserController();
    private StatusMessageModel statusMessageModel = new StatusMessageModel();
    private String userRole = "GUEST";

    private String name;
    private String email;
    private String password;
    private String role;


    public String getUserRole() {
        return userRole;
    }

    public boolean isGuest() {
        return "GUEST".equals(userRole);
    }

    public boolean isUser() {
        return "USER".equals(userRole);
    }

    public boolean isAdmin() {
        return "ADMIN".equals(userRole);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String registrationUser(){
        statusMessageModel = userController.registerUser(name,email,password,role);
        if (statusMessageModel.isStatus()){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", statusMessageModel.getMessage()));
            return "login.xhtml?faces-redirect=true";
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", statusMessageModel.getMessage()));
            return "registration.xhtml?faces-redirect=true";
        }
    }

    public String login(){
        Users user = userController.loginUser(email,password);
        if (user != null){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Welcome,"+user.getFullName()));
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

            Cookie userCookie = new Cookie("email", user.getEmail());
            userCookie.setMaxAge(3600);
            userCookie.setPath("/");
            response.addCookie(userCookie);
            userRole= user.getRoles();
            return "/Users/userDashboard.xhtml?faces-redirect=true";
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid email or password"));
            return "login.xhtml?faces-redirect=true";
        }
    }

    public String logout(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("email".equals(cookie.getName())) {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        facesContext.getExternalContext().invalidateSession();
        userRole = "GUEST";
        return "/index.xhtml?faces-redirect=true";
    }
}
