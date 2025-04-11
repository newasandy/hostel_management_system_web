package service;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CooldownService {

    private ConcurrentHashMap<String, LocalDateTime> cooldownUser = new ConcurrentHashMap<>();

    public void setUserSessionCooldown(String userEmail, int minutes){
        cooldownUser.put(userEmail, LocalDateTime.now().plusMinutes(minutes));
    }

    public LocalDateTime getUserInCooldown(String userEmail){
        return cooldownUser.get(userEmail);
    }
}
