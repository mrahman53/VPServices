package org.vp.restservices;

import org.vp.cache.JedisMain;
import org.vp.databases.VCDatabaseServices;
import org.vp.vc.profile.VCProfile;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Path("ResetRedisData")
public class RedisReset {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public boolean reset()throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        boolean value = false;
        List<VCProfile> profile = null;
        Object profileList = null;
        try {
            JedisMain main = new JedisMain();
            if(main.getVcListKeys()) {
                profileList = main.getObjectValue("vcList");
                profile = (List<VCProfile>) profileList;
            }
            if (profile == null) {
                VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();
                List<VCProfile> list = vcDatabaseServices.readData();
                main.setObjectValue("vcList", list);
            }
            profileList = main.getObjectValue("vcList");
            profile = (List<VCProfile>) profileList;
            value = true;
        }catch (Exception ex){
            value = false;
        }
        return value;
    }

    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        List<VCProfile> profile = null;
        Object profileList = null;
        try {
            JedisMain main = new JedisMain();
            if(main.getVcListKeys()){
                profileList = main.getObjectValue("vcList");
                profile = (List<VCProfile>) profileList;
            }
            if (profile == null) {
                VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();
                List<VCProfile> list = vcDatabaseServices.readData();
                main.setObjectValue("vcList", list);
            }
            profileList = main.getObjectValue("vcList");
            profile = (List<VCProfile>) profileList;
        }catch (Exception ex){

        }
    }
}
