package org.vp.databases;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.vp.authentication.AdminUserProfile;


/**
 * Created by mrahman on 8/24/16.
 */
public class AdminUserDatabaseServices {
    public ConnectDB connectDB = new ConnectDB();
    public MongoDatabase mongoDatabase = null;

    public String adminRegistration(AdminUserProfile user){
        String profile = user.getUsername();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        Document document = new Document().append("username",user.getUsername()).append("password", user.getPassword());
        collection.insertOne(document);
        return profile + " has been registered";
    }
    public String updateAdminUserProfile(AdminUserProfile user){
        String profile = user.getUsername();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        Document document = new Document().append("username",user.getUsername()).append("password", user.getPassword());

        return profile + " has been updated";
    }
    public AdminUserProfile register(String username){
        AdminUserProfile user = new AdminUserProfile();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        BasicDBObject basicDBObject = new BasicDBObject().append("username", username);
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String namePosted = (String)doc.get("username");
            String passwordPosted = (String)doc.get("password");
            String firstNamePosted = (String)doc.get("firstName");
            String lastNamePosted = (String)doc.get("lastName");
            user.setUsername(namePosted);
            user.setPassword(passwordPosted);
        }
        return user;
    }
    public AdminUserProfile login(String username){
        AdminUserProfile user = new AdminUserProfile();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        //BasicDBObject basicDBObject = new BasicDBObject().append("username", username);
        MongoCollection<Document> collection = mongoDatabase.getCollection("login");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("username", username);
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String namePosted = (String)doc.get("username");
            String passwordPosted = (String)doc.get("password");
            String firstNamePosted = (String)doc.get("firstName");
            String lastNamePosted = (String)doc.get("lastName");
            user.setUsername(namePosted);
            user.setPassword(passwordPosted);
            user.setValue(true);
        }
        return user;
    }
    public boolean loginVerify(String username, String password){
        AdminUserProfile user = new AdminUserProfile();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        //BasicDBObject basicDBObject = new BasicDBObject().append("username", username);
        MongoCollection<Document> collection = mongoDatabase.getCollection("login");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("username", username);
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String namePosted = (String)doc.get("username");
            String passwordPosted = (String)doc.get("password");
            String firstNamePosted = (String)doc.get("firstName");
            String lastNamePosted = (String)doc.get("lastName");
            user.setUsername(namePosted);
            user.setPassword(passwordPosted);
            user.setValue(true);
        }
        if(user.getUsername().equals(username) && user.getPassword().equals(password)){
            return true;
        }else{
            return false;
        }

    }
    public boolean loginVerify(String username){
        AdminUserProfile user = new AdminUserProfile();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        //BasicDBObject basicDBObject = new BasicDBObject().append("username", username);
        MongoCollection<Document> collection = mongoDatabase.getCollection("login");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("username", username);
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String namePosted = (String)doc.get("username");
            String passwordPosted = (String)doc.get("password");
            String firstNamePosted = (String)doc.get("firstName");
            String lastNamePosted = (String)doc.get("lastName");
            user.setUsername(namePosted);
            user.setPassword(passwordPosted);
            user.setValue(true);
        }
        return user.isValue();
    }


}
