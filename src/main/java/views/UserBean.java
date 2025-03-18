package views;

import daoImp.AddressDAOImp;
import daoImp.UserDAOImpl;
import daoInterface.UsersDAO;
import model.StatusMessageModel;
import model.Users;
import service.AuthenticationService;
import service.UserService;

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
    private final UsersDAO usersDAO = new UserDAOImpl();
    private final AddressDAOImp addressDAOImp = new AddressDAOImp();
    private final UserService userService = new UserService(usersDAO,addressDAOImp);
    private final AuthenticationService authenticationService = new AuthenticationService(usersDAO);
    private StatusMessageModel statusMessageModel = new StatusMessageModel();
    private String userRole = "GUEST";

    private String name;
    private String email;
    private String password;
    private String role;
    private String country;
    private String district;
    private String rmcMc;
    private int wardNumber;



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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRmcMc() {
        return rmcMc;
    }

    public void setRmcMc(String rmcMc) {
        this.rmcMc = rmcMc;
    }

    public int getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(int wardNumber) {
        this.wardNumber = wardNumber;
    }

    public String registrationUser(){
        statusMessageModel = userService.registerNewStudent(name,email,password,role,country,district,rmcMc,wardNumber);
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
        Users user = authenticationService.loginService(email,password);
        if (user != null){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Welcome,"+user.getFullName()));
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

            Cookie userCookie = new Cookie("email", user.getEmail());
            userCookie.setMaxAge(60*60*60);
            userCookie.setPath("/");
            response.addCookie(userCookie);
            userRole= user.getRoles();
            if ("USER".equals(user.getRoles())) {
                return "/Users/userDashboard.xhtml?faces-redirect=true";
            } else if ("ADMIN".equals(user.getRoles())) {
                return "/admin/adminDashboard.xhtml?faces-redirect=true";
            }
            return "login.xhtml?faces-redirect=true";
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
