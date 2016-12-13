package org.vp.databases;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.vp.authentication.AdminUserProfile;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


/**
 * Created by mrahman on 8/24/16.
 */
public class AdminUserDatabaseServices {
    public ConnectMongo connectMongo = new ConnectMongo();
    public MongoClient mongoClient = null;
    public MongoDatabase mongoDatabase = null;

    public boolean adminRegistration(AdminUserProfile user)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try{
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("AdminProfileDB");
            MongoCollection<Document> collection = mongoDatabase.getCollection("admin_login");
            Document document = new Document().append("email",user.getEmail()).append("password", user.getPassword());
            collection.insertOne(document);
                mongoClient.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }finally {
                if (mongoClient != null) {

                    mongoClient = null;
                }
            }

        return true;
    }

    public String updateAdminUserProfile(AdminUserProfile user){
        String profile = user.getEmail();
        try{
        mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
        mongoDatabase = mongoClient.getDatabase("AdminProfileDB");
        MongoCollection<Document> collection = mongoDatabase.getCollection("admin_login");
        Document document = new Document().append("email",user.getEmail()).append("password", user.getPassword());
        mongoClient.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if (mongoClient != null) {

                mongoClient = null;
            }
        }

        return profile + " has been updated";
    }


    public AdminUserProfile register(String email){
        AdminUserProfile user = new AdminUserProfile();

        try{
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("AdminProfileDB");
            MongoCollection<Document> collection = mongoDatabase.getCollection("admin_login");
            Document document = new Document().append("email",user.getEmail()).append("password", user.getPassword());
            collection.insertOne(document);
            connectMongo.mongoClient.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if (mongoClient != null) {

                mongoClient = null;
            }
        }

        return user;
    }


    public AdminUserProfile login(String email){
        AdminUserProfile user = new AdminUserProfile();
        try{
        mongoDatabase = connectMongo.connectLocalMongoDBClient();
        //BasicDBObject basicDBObject = new BasicDBObject().append("username", username);
        MongoCollection<Document> collection = mongoDatabase.getCollection("login");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("email", email);
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String emailPosted = (String)doc.get("email");
            String passwordPosted = (String)doc.get("password");
            user.setEmail(emailPosted);
            user.setPassword(passwordPosted);
            user.setValue(true);
        }
            connectMongo.mongoClient.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if (connectMongo.mongoClient != null) {

                connectMongo.mongoClient = null;
            }
        }
        return user;
    }

    public boolean loginVerify(String email, String password)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        AdminUserProfile user = new AdminUserProfile();
        boolean verify = false;
        try {

            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("AdminProfileDB");
            MongoCollection<Document> collection = mongoDatabase.getCollection("admin_login");
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("email", email);
            FindIterable<Document> iterable = collection.find(basicDBObject);
            for (Document doc : iterable) {
                String emailPosted = (String) doc.get("email");
                String passwordPosted = (String) doc.get("password");
                user.setEmail(emailPosted);
                user.setPassword(passwordPosted);
                user.setValue(true);
            }
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                verify = true;
            } else {
                verify = false;
            }

            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (mongoClient != null) {

                mongoClient = null;
            }
        }
    return verify;

    }

    public boolean loginVerify(String email){
        AdminUserProfile user = new AdminUserProfile();
        mongoDatabase = connectMongo.connectLocalMongoDBClient();
        //BasicDBObject basicDBObject = new BasicDBObject().append("username", username);
        MongoCollection<Document> collection = mongoDatabase.getCollection("login");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("email", email);
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String emailPosted = (String)doc.get("email");
            String passwordPosted = (String)doc.get("password");
           // String firstNamePosted = (String)doc.get("firstName");
           // String lastNamePosted = (String)doc.get("lastName");
            user.setEmail(emailPosted);
            user.setPassword(passwordPosted);
            user.setValue(true);
        }
        return user.isValue();
    }


}
