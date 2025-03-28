package views;
import daoImp.AddressDAOImp;
import daoImp.UserDAOImpl;
import daoImp.UserTypeDAOImp;
import daoInterface.UsersDAO;
import model.Address;
import model.StatusMessageModel;
import model.UserType;
import model.Users;
import service.AuthenticationService;
import service.UserService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

@Named("userBean")
@SessionScoped
public class UserBean implements Serializable{

    @Inject
    private ViewStudentBean viewStudentBean;

    private UsersDAO usersDAO = new UserDAOImpl();

    private AddressDAOImp addressDAOImp = new AddressDAOImp();

    private UserTypeDAOImp userTypeDAOImp = new UserTypeDAOImp();

    private UserService userService = new UserService(usersDAO,addressDAOImp);

    private AuthenticationService authenticationService = new AuthenticationService(usersDAO);

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
    private UserType selectUserType;

    private List<UserType> userTypes;
    private Users selectUser;
    private Address selectUserAddress;


    @PostConstruct
    public void init() {
        try {
            setUserRoleFromCookieOrSession();
            loadUserTypes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize UserBean", e);
        }
    }

    public void loadUserTypes() {
        userTypes = userTypeDAOImp.getAll();
    }

    public String getUserRole() {
        return userRole;
    }

    public boolean isGuest() {
        return "GUEST".equals(userRole);
    }

    public List<UserType> getUserTypes() {
        return userTypes;
    }

    public boolean isUser() {
        return "USER".equals(userRole);
    }

    public boolean isAdmin() {
        return "ADMIN".equals(userRole);
    }

    public UserType getSelectUserType() {
        return selectUserType;
    }

    public void setSelectUserType(UserType selectUserType) {
        this.selectUserType = selectUserType;
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

    public Users getSelectUser() {
        return selectUser;
    }

    public void setSelectUser(Users selectUser) {
        this.selectUser = selectUser;
    }

    public Address getSelectUserAddress() {
        return selectUserAddress;
    }

    public void setSelectUserAddress(Address selectUserAddress) {
        this.selectUserAddress = selectUserAddress;
    }

    private void setUserRoleFromCookieOrSession() {
        Cookie cookie = (Cookie) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestCookieMap()
                .get("userRole");
        if (cookie != null) {
            userRole = cookie.getValue();
        } else {
            userRole = "GUEST";
        }
    }

    public void registrationUser(){
        statusMessageModel = userService.registerNewStudent(name,email,password,selectUserType,country,district,rmcMc,wardNumber);
        resetFields();
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
        Users user = authenticationService.loginService(email,password);
        if (user != null){
            if(user.isStatus()){
                resetFields();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Welcome,"+user.getFullName()));
                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

                Cookie userCookie = new Cookie("email", user.getEmail());
                userCookie.setMaxAge(60*60*60);
                userCookie.setPath("/");
                response.addCookie(userCookie);

                // Create cookie for user role
                Cookie userRoleCookie = new Cookie("userRole", user.getRoles().getUserTypes());
                userRoleCookie.setMaxAge(60 * 60 * 60); // Set cookie to expire in 60 hours
                userRoleCookie.setPath("/"); // Set the path for which this cookie is valid
                response.addCookie(userRoleCookie);


                userRole= user.getRoles().getUserTypes();
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
            if (usersDAO.update(student)){
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
            if (usersDAO.update(student)){
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
        this.selectUser = student;
    }
    public void updateUser(){
        try {
            if (usersDAO.update(selectUser)){
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
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("email".equals(cookie.getName()) || "userRole".equals(cookie.getName())) {
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

    public void resetFields() {
        this.name = "";
        this.email = "";
        this.password = "";
        this.role = "";
        this.selectUserType = null;
        this.country = "";
        this.district = "";
        this.rmcMc = "";
        this.wardNumber = 1;
        loadUserTypes();
    }
}