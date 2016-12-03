package org.vp.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

/**
 * Created by mrahman on 7/28/16.
 */
public class CORSResponseFilter implements ContainerResponseFilter, ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {

        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        System.out.println("Cors is hit");
        headers.add("Access-Control-Allow-Origin", "*");
        //headers.add("Access-Control-Allow-Origin", "http://podcastpedia.org"); //allows CORS requests only coming from podcastpedia.org
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        //headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
        headers.add("Access-Control-Allow-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Headers, Origin,Accept," +
                "Authorization,X-Requested-With,Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,X-Requested-With," +
                "Content-Type, X-Codingpedia");
    }

    @Override
        public void filter(ContainerRequestContext requestContext){

        MultivaluedMap<String, String> request = requestContext.getHeaders();

        request.add("Access-Control-Allow-Origin", "*");
        //headers.add("Access-Control-Allow-Origin", "http://podcastpedia.org"); //allows CORS requests only coming from podcastpedia.org
        request.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        //headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
        request.add("Access-Control-Allow-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Headers, Origin,Accept," +
                "Authorization,X-Requested-With,Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,X-Requested-With," +
                "Content-Type, X-Codingpedia");
    }

}