package org.vp.databases;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.vp.authentication.User;

/**
 * Created by mrahman on 2/9/16.
 */
public class UserDatabaseServices {

    public ConnectDB connectDB = new ConnectDB();
    public MongoDatabase mongoDatabase = null;

    public String adminRegistration(User user){
        String profile = user.getName();
        mongoDatabase = connectDB.connectMongoClientDB();
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        Document document = new Document().append("name",user.getName()).append("email",
                user.getEmail()).append("password", user.getPassword()).append("phoneNumber",
                user.getPhoneNumber());
        collection.insertOne(document);
        return profile + " has been registered";
    }
    public String updateAdminUserProfile(User user){
        String profile = user.getName();
        mongoDatabase = connectDB.connectMongoClientDB();
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        Document document = new Document().append("name",user.getName()).append("email",
                user.getEmail()).append("password", user.getPassword()).append("phoneNumber",
                user.getPhoneNumber());
        //collection.update(document);

        return profile + " has been updated";
    }
    public User login(String email){
        User user = new User();
        mongoDatabase = connectDB.connectDBClient();
        BasicDBObject basicDBObject = new BasicDBObject().append("email", email);
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String namePosted = (String)doc.get("name");
            String emailPosted = (String)doc.get("email");
            String passwordPosted = (String)doc.get("password");
            String phoneNumberPosted = (String)doc.get("phoneNumber");
            user.setName(namePosted);
            user.setEmail(emailPosted);
            user.setPassword(passwordPosted);
            user.setPhoneNumber(phoneNumberPosted);
        }
        return user;
    }



}
