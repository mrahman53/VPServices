package org.vp.filter;

import org.glassfish.jersey.internal.util.Base64;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by mrahman on 7/28/16.
 */

@Provider
public class CORSResponseFilter implements ContainerResponseFilter, ContainerRequestFilter{

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
    private static final String SECURED_URL_PREFIX = "secured";


    public void authFilter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) {
            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
            if (authHeader != null && authHeader.size() > 0) {
                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
                String decodedString = Base64.decodeAsString(authToken);
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
                String userName = tokenizer.nextToken();
                String passWord = tokenizer.nextToken();
                if ("vpdb".equals(userName) && "kmhr@1234".equals(passWord)) {
                    return;
                }
            }
            Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).
                    entity("You can not access the resources").build();
            requestContext.abortWith(unauthorizedStatus);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        System.out.println("Cors is hit");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE, PUT");
        headers.add("Access-Control-Allow-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Headers, Origin,Accept," +
                "Authorization,X-Requested-With,Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,X-Codingpedia");
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException{

        MultivaluedMap<String, String> request = requestContext.getHeaders();
            if(requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
                request.add("Access-Control-Allow-Origin", "*");
                request.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
                request.add("Access-Control-Allow-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Headers, Origin,Accept," +
                        "Authorization,X-Requested-With,Content-Type,Access-Control-Request-Method, Access-Control-Request-Headers,X-Codingpedia");
            }else if(requestContext.getMethod().equalsIgnoreCase("GET")) {
                if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) {
                    List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
                    if (authHeader != null && authHeader.size() > 0) {
                        authFilter(requestContext);
                        request.add("Access-Control-Allow-Origin", "*");
                        request.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
                        request.add("Access-Control-Allow-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Headers, Origin,Accept," +
                                "Authorization,X-Requested-With,Content-Type,Access-Control-Request-Method, Access-Control-Request-Headers,X-Codingpedia");
                    }else{
                        Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).
                                entity("You can not access the resources").build();
                        requestContext.abortWith(unauthorizedStatus);
                    }
                }
            }
        }
}