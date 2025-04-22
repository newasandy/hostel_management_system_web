package service;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ActiveUserService {

    private ConcurrentHashMap<String, HttpSession> activeUsers = new ConcurrentHashMap<>();

    public void addUser(String loginUser, HttpSession session){
        activeUsers.put(loginUser,session);
    }

    public void removeUser(String loginUser){
        activeUsers.remove(loginUser);
    }

    public boolean containsUser(String userEmail){
        return activeUsers.containsKey(userEmail);
    }

    public HttpSession getuserSession(String user){
        return activeUsers.get(user);
    }

    public ConcurrentHashMap<String, HttpSession> getAllUsers() { return activeUsers; }
}
