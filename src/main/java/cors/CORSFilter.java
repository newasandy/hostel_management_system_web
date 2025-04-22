package cors;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext reqCtx,
                       ContainerResponseContext respCtx) throws IOException {
        respCtx.getHeaders().add("Access-Control-Allow-Origin", "*");
        respCtx.getHeaders().add("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");
        respCtx.getHeaders().add("Access-Control-Allow-Credentials", "true");
        respCtx.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        respCtx.getHeaders().add("Access-Control-Max-Age", "1209600");
    }
}
