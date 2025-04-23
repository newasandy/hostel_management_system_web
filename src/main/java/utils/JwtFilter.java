package utils;


import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Cookie accessCookie = requestContext.getCookies().get("access_token");
        if (accessCookie == null || !JwtUtils.isTokenValid(accessCookie.getValue())) {
            abortRequest(requestContext);
        }
        if (accessCookie != null){
            String email = JwtUtils.getUserEmail(accessCookie.getValue());
            requestContext.setProperty("email", email);
        }
    }

    private void abortRequest(ContainerRequestContext context) {
        context.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\": \"Invalid or expired token\"}")
                .build());
    }
}