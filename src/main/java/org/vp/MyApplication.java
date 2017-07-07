package org.vp;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;

/**
 * Created by mrahman on 12/6/16.
 */
//@ApplicationPath("/")
//public class MyApplication extends ResourceConfig {
//
//    public MyApplication() {
//        // Register resources and providers using package-scanning.
//        packages("org.vp");
//
//        // Register my custom provider - not needed if it's in my.package.
//        // register(SecurityRequestFilter.class);
//        // Register an instance of LoggingFilter.
//        //register(new LoggingFilter(LOGGER, true));
//
//        // Enable Tracing support.
//        property(ServerProperties.TRACING, "ALL");
//    }
//}