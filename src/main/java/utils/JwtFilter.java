package utils;


import service.RefreshTokenStore;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    @Inject
    private RefreshTokenStore refreshTokenStore;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Cookie accessCookie = requestContext.getCookies().get("access_token");
        if (accessCookie == null || !JwtUtils.isTokenValid(accessCookie.getValue())) {
            abortRequest(requestContext);
        }
        if (accessCookie != null){
            String email = JwtUtils.getUserEmail(accessCookie.getValue());
            String role = JwtUtils.getUserRole(accessCookie.getValue());

            requestContext.setProperty("email", email);

            SecurityContext original = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {
                @Override public Principal getUserPrincipal()      { return () -> email; }
                @Override public boolean    isUserInRole(String r){ return r.equals(role); }
                @Override public boolean    isSecure()            { return original.isSecure(); }
                @Override public String     getAuthenticationScheme() { return "JWT"; }
            });

            Method method = resourceInfo.getResourceMethod();
            if (method != null) {
                JWTTokenNeeded ann = method.getAnnotation(JWTTokenNeeded.class);
                if (ann != null && ann.allowed().length > 0) {
                    // build set of allowed role names
                    Set<String> allowed = Arrays.stream(ann.allowed())
                            .map(Enum::name)
                            .collect(Collectors.toSet());
                    if (!allowed.contains(role)) {
                        requestContext.abortWith(
                                Response.status(Response.Status.FORBIDDEN)
                                        .entity("{\"error\":\"User Role not match\"}")
                                        .build()
                        );
                    }
                }
            }
        }
    }

    private void abortRequest(ContainerRequestContext context) {
        context.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\": \"Invalid or expired token\"}")
                .build());
    }
}