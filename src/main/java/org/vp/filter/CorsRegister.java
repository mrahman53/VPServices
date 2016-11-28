package org.vp.filter;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by mrahman on 11/4/16.
 */
public class CorsRegister extends ResourceConfig {

    public CorsRegister(){
        register(CORSResponseFilter.class);
    }

}
