package views;

import controller.UserController;
import model.StatusMessageModel;
import model.Users;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class RegistrationBean implements Serializable {
    private final UserController userController = new UserController();
    private StatusMessageModel statusMessageModel = new StatusMessageModel();

    private String name;
    private String email;
    private String password;
    private String role;

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
        Users status = userController.loginUser(email,password);
        if (status != null){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Welcome,"+status.getFullName()));
            return "/Users/home.xhtml?faces-redirect=true";
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid email or password"));
            return "login.xhtml?faces-redirect=true";
        }
    }

    public String logout(){
        return "/login.xhtml?faces-redirect=true";
    }
}
