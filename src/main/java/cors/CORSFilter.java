package cors;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
//    @Override
//    public void filter(ContainerRequestContext req) throws IOException {
//        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
//            String origin = req.getHeaderString("Origin");
//            if (origin != null) {
//                req.abortWith(
//                        javax.ws.rs.core.Response
//                                .ok()
//                                .header("Access-Control-Allow-Origin", "http://localhost:5173")
//                                .header("Access-Control-Allow-Credentials", "true")
//                                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
//                                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
//                                .header("Vary", "Origin")
//                                .build()
//                );
//            }
//        }
//    }

    // Adds the CORS headers on every non-OPTIONS response
    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
            res.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173");
            res.getHeaders().add("Access-Control-Allow-Credentials", "true");
            res.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

//    @Override
//    public void filter(ContainerRequestContext reqCtx,
//                       ContainerResponseContext respCtx) throws IOException {
//        respCtx.getHeaders().add("Access-Control-Allow-Origin", "*");
//        respCtx.getHeaders().add("Access-Control-Allow-Headers",
//                "origin, content-type, accept, authorization");
//        respCtx.getHeaders().add("Access-Control-Allow-Credentials", "true");
//        respCtx.getHeaders().add("Access-Control-Allow-Methods",
//                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
//        respCtx.getHeaders().add("Access-Control-Max-Age", "1209600");
//    }
}
