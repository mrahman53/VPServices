package org.vp.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by mrahman on 12/3/16.
 */
//@Provider
//public class CORSFilter implements ContainerResponseFilter {
//
//    @Override
//    public void filter(ContainerRequestContext request,
//                       ContainerResponseContext response) throws IOException {
//        response.getHeaders().add("Access-Control-Allow-Origin", "*");
//        response.getHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, X_Auth-Token," +
//                "authorization, Access-Control-Allow-Origin, Access-Control-Allow-Headers");
//        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
//        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
//        response.getHeaders().add("Content-Type", "application/json");
//        response.getHeaders().add("Accept", "application/json");
//    }
//}
