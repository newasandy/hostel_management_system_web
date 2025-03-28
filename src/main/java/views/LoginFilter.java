package views;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        String email = getCookieValue(httpRequest, "email");
        String userRole = getCookieValue(httpRequest, "userRole");

        if (uri.contains("/users/")) {
            if (email == null || userRole == null){
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
                return;
            } else if (!"USER".equals(userRole)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/adminDashboard.xhtml");
                return;
            }
        }else if (uri.contains("/admin/")) {
            if (email == null || userRole == null){
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
                return;
            } else if (!"ADMIN".equals(userRole)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/users/userDashboard.xhtml");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

