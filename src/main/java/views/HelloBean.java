package views;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class HelloBean {
    private String username;
    private String password;
    private String message;
private int number;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public int getNumber(){
        return number;
    }
    public String sayHello() {
        if ("sandy".equals(username) && "pass".equals(password)){
            return "Users/home.xhtml?faces-redirect=true";
        }
        message = "Invalid Username and Password";
        return null;
    }
    public void increment(){
        number++;
    }
    public String logOut(){
        username = null;
        password = null;
        return "/index.xhtml?faces-redirect=true";
    }
    public String loginUser(){
        return "/login.xhtml?faces-redirect=true";
    }
}
