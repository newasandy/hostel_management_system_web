package utils;
import service.ActiveUserService;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        ActiveUserService activeUserService = CDI.current().select(ActiveUserService.class).get();

        String userToRemove = null;
        for (Map.Entry<String, HttpSession> entry : activeUserService.getAllUsers().entrySet()) {
            if (entry.getValue().equals(se.getSession())) {
                userToRemove = entry.getKey();
                break;
            }
        }

        if (userToRemove != null) {
            activeUserService.removeUser(userToRemove);
            System.out.println("Session expired and removed for user: " + userToRemove);
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // Optional logging or monitoring
    }
}