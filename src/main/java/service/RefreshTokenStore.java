package service;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class RefreshTokenStore {

    private ConcurrentHashMap<String,Object> refreshToken = new ConcurrentHashMap<>();

    public void addToken(String loginUser, String session){
        refreshToken.put(loginUser,session);
    }

    public void removeToken(String loginUser){
        refreshToken.remove(loginUser);
    }

    public String getUserToken(String user){
        Object token = refreshToken.get(user);
        return token != null ? token.toString() : null;
    }

}
