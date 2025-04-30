package cors;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
            res.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173");
            res.getHeaders().add("Access-Control-Allow-Credentials", "true");
            res.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

}
