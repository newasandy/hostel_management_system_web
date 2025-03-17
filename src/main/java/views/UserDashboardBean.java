package views;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class UserDashboardBean {

    private String email;
    private String name;

    public String getEmail() {
        if (email == null) {
            email = getCookieValue("email");
        }
        return email;
    }
    public String getNames() {
        if (email == null) {
            email = getCookieValue("name");
        }
        return email;
    }

    private String getCookieValue(String email) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(email)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
