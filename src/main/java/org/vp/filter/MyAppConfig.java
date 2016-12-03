package org.vp.filter;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * Created by mrahman on 12/2/16.
 */
public class MyAppConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(CORSResponseFilter.class);
        return classes;
    }
}