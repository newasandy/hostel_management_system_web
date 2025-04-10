package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    private static final String tokenKey = "jwtToken";


    public static void storeToken(HttpServletRequest request ,String token) {

        HttpSession session = request.getSession(false);

        if (session == null || !isSessionValid(request)) {
            session = request.getSession(true);
        }
        session.setAttribute(tokenKey, token);
        session.setMaxInactiveInterval(60*60);
    }

    public static String getToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object token = session.getAttribute(tokenKey);
            return token != null ? token.toString() : null;
        }
        return null;
    }

    public static void removeToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(tokenKey);
            session.invalidate();
        }
    }

    public static boolean isSessionValid(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                return false;
            }
            String token = (String) session.getAttribute(tokenKey);
            return token != null && !token.isEmpty();
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
