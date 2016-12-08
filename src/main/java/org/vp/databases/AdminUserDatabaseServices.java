package org.vp.databases;

import com.mongodb.BasicDBObject;
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
    public ConnectDB connectDB = new ConnectDB();
    public MongoDatabase mongoDatabase = null;

    public boolean adminRegistration(AdminUserProfile user)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
       // String profile = user.getUsername();
        mongoDatabase = connectDB.connectRecommendedSSLAtlas("AdminProfileDB");
        MongoCollection<Document> collection = mongoDatabase.getCollection("admin_login");
        Document document = new Document().append("email",user.getEmail()).append("password", user.getPassword());
        collection.insertOne(document);
        return true;
    }

    public String updateAdminUserProfile(AdminUserProfile user){
        String profile = user.getEmail();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        Document document = new Document().append("email",user.getEmail()).append("password", user.getPassword());

        return profile + " has been updated";
    }


    public AdminUserProfile register(String email){
        AdminUserProfile user = new AdminUserProfile();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        BasicDBObject basicDBObject = new BasicDBObject().append("email", email);
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String emailPosted = (String)doc.get("email");
            String passwordPosted = (String)doc.get("password");
            String firstNamePosted = (String)doc.get("firstName");
            String lastNamePosted = (String)doc.get("lastName");
            user.setEmail(emailPosted);
            user.setPassword(passwordPosted);
        }
        return user;
    }


    public AdminUserProfile login(String email){
        AdminUserProfile user = new AdminUserProfile();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        //BasicDBObject basicDBObject = new BasicDBObject().append("username", username);
        MongoCollection<Document> collection = mongoDatabase.getCollection("login");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("email", email);
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String emailPosted = (String)doc.get("email");
            String passwordPosted = (String)doc.get("password");
            //String firstNamePosted = (String)doc.get("firstName");
            //String lastNamePosted = (String)doc.get("lastName");
            user.setEmail(emailPosted);
            user.setPassword(passwordPosted);
            user.setValue(true);
        }
        return user;
    }

    public boolean loginVerify(String email, String password)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        AdminUserProfile user = new AdminUserProfile();
        mongoDatabase = connectDB.connectRecommendedSSLAtlas("AdminProfileDB");
        //BasicDBObject basicDBObject = new BasicDBObject().append("username", username);
        MongoCollection<Document> collection = mongoDatabase.getCollection("admin_login");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("email", email);
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String emailPosted = (String)doc.get("email");
            String passwordPosted = (String)doc.get("password");
            //String firstNamePosted = (String)doc.get("firstName");
            //String lastNamePosted = (String)doc.get("lastName");
            user.setEmail(emailPosted);
            user.setPassword(passwordPosted);
            user.setValue(true);
        }
        if(user.getEmail().equals(email) && user.getPassword().equals(password)){
            return true;
        }else{
            return false;
        }

    }

    public boolean loginVerify(String email){
        AdminUserProfile user = new AdminUserProfile();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
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
