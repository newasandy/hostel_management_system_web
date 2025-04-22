package configRest;

import api.*;
import cors.CORSFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<>();
        s.add(FeeAPI.class);
        s.add(AuthAPI.class);
        s.add(UsersAPI.class);
        s.add(CORSFilter.class);
        return s;
    }
}
