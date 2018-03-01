package org.vp.filter;

import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

//@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
    private final String SECURED_URL_PREFIX = "secured";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) {
            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
             if (authHeader != null && authHeader.size() > 0) {
                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
                String decodedString = Base64.decodeAsString(authToken);
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
                String userName = tokenizer.nextToken();
                String passWord = tokenizer.nextToken();

                if ("user".equals(userName) && "password".equals(passWord)) {
                    return;
                }
             }
            Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).
                    entity("User can not access the resources").build();
            requestContext.abortWith(unauthorizedStatus);
        }
    }
}
