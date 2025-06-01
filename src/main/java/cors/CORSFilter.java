package cors;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    private static final List<String> allowedOrigins = Arrays.asList(
            "http://localhost:5173",
            "http://localhost:3000"

    );
    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
        String origin = req.getHeaderString("Origin");
        if (origin != null && allowedOrigins.contains(origin)) {
            res.getHeaders().add(
                    "Access-Control-Allow-Origin", origin);
            res.getHeaders().add(
                    "Access-Control-Allow-Credentials", "true");
        }
//            res.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173","http://localhost:3000");
//            res.getHeaders().add("Access-Control-Allow-Credentials", "true");
            res.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

}
