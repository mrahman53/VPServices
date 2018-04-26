package org.vp.cache;

import org.vp.databases.VCDatabaseServices;
import org.vp.vc.profile.VCProfile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class JedisMain {

    //address of your redis server
    private static final String redisHost = "localhost";
    private static final Integer redisPort = 6379;

    //the jedis connection pool..
    private static JedisPool pool = null;

    public JedisMain() {
        //configure our pool connection
        pool = new JedisPool(redisHost, redisPort);

    }

    public void addSets() {
        //let us first add some data in our redis server using Redis SET.
        String key = "members";
        String member1 = "Sedarius";
        String member2 = "Richard";
        String member3 = "Joe";

        //get a jedis connection jedis connection pool
        Jedis jedis = pool.getResource();
        try {
            //save to redis
            jedis.sadd(key, member1, member2, member3);

            //after saving the data, lets retrieve them to be sure that it has really added in redis
            Set members = jedis.smembers(key);
            for (Object member : members) {
                System.out.println(member);
            }
        } catch (JedisException e) {
            //if something wrong happen, return it back to the pool
            if (null != jedis) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            ///it's important to return the Jedis instance to the pool once you've finished using it
            if (null != jedis)
                pool.returnResource(jedis);
        }
    }

    public void addHash() {
        //add some values in Redis HASH
        String key = "javapointers";
        Map<String, String> map = new HashMap<>();
        map.put("name", "Java Pointers");
        map.put("domain", "www.javapointers.com");
        map.put("description", "Learn how to program in Java");

        Jedis jedis = pool.getResource();
        try {
            //save to redis
            jedis.hmset(key, map);

            //after saving the data, lets retrieve them to be sure that it has really added in redis
            Map<String, String> retrieveMap = jedis.hgetAll(key);
            for (String keyMap : retrieveMap.keySet()) {
                System.out.println(keyMap + " " + retrieveMap.get(keyMap));
            }

        } catch (JedisException e) {
            //if something wrong happen, return it back to the pool
            if (null != jedis) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            ///it's important to return the Jedis instance to the pool once you've finished using it
            if (null != jedis)
                pool.returnResource(jedis);
        }
    }
    public void addVcProfile(List<VCProfile> list) {
        //add some values in Redis HASH
        String key = "vclist";
        Jedis jedis = pool.getResource();
        try {
            for(VCProfile profile:list) {
                jedis.rpush(key, profile.getVcInfo().vcName);
            }

        } catch (JedisException e) {
            //if something wrong happen, return it back to the pool
            if (null != jedis) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            ///it's important to return the Jedis instance to the pool once you've finished using it
            if (null != jedis)
                pool.returnResource(jedis);
        }
    }
    public List<VCProfile> readVCProfileFromRedis(){
            Jedis jedis = pool.getResource();
            String key = "vclist";
            List<VCProfile> list = new ArrayList<VCProfile>();
            long length = jedis.llen("vclist");
            List<String> readList = jedis.lrange("vclist",0, length);
            for(int i=0; i<readList.size(); i++){
                System.out.println(readList.get(i));
                Object value = (Object)readList.get(i);
                System.out.println(value);
                //list.add((VCProfile)readList.get(i));
            }
            //String profiles = jedis.rpop(key);
            //System.out.println(profiles);
            //VCProfile vcProfile = (VCProfile)profiles;
            //list.add(vcProfile);


        return list;
    }
    public void setObjectValue(String key, Object value) {
        Jedis jedis = pool.getResource();
        jedis.set(key.getBytes(), toBytes(value));
    }
    public Object getObjectValue(String key) {
        Jedis jedis = pool.getResource();
        return fromBytes(jedis.get(key.getBytes()));
    }
    public Object getAllKeys() {
        Jedis jedis = pool.getResource();
        return jedis.keys("*");
    }
    public Object fromBytes(byte key[]) {
        Object obj = null;
        if(key!=null)
        {
            try {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(key));
                obj = ois.readObject();
                ois.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
    public byte[] toBytes(Object object) {
        ByteArrayOutputStream baos;
        ObjectOutputStream oos;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();
        List<VCProfile> list = vcDatabaseServices.readData();
        JedisMain main = new JedisMain();
        main.setObjectValue("vcList", list);
        Object profileList = main.getObjectValue("vcList");
        List<VCProfile> profile = (List<VCProfile>)profileList;
        for(VCProfile pro:profile){
            System.out.println(pro.vcInfo);
        }
        //List<VCProfile> readList = new ArrayList<VCProfile>();
    }
}