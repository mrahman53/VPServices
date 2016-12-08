package org.vp.databases;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.vp.authentication.ClientUserProfile;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mrahman on 2/9/16.
 */
public class ClientUserDatabaseServices {

    public ConnectDB connectDB = new ConnectDB();
    public MongoDatabase mongoDatabase = null;

    public String adminRegistration(ClientUserProfile clientUserProfile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String profile = clientUserProfile.getName();
        mongoDatabase = connectDB.connectRecommendedSSLAtlas("ClientProfileDB");
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        Document document = new Document().append("name", clientUserProfile.getName()).append("email",
                clientUserProfile.getEmail()).append("password", clientUserProfile.getPassword()).append("phoneNumber",
                clientUserProfile.getPhoneNumber());
        collection.insertOne(document);
        return profile + " has been registered";
    }
    public String updateAdminUserProfile(ClientUserProfile clientUserProfile){
        String profile = clientUserProfile.getName();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        Document document = new Document().append("name", clientUserProfile.getName()).append("email",
                clientUserProfile.getEmail()).append("password", clientUserProfile.getPassword()).append("phoneNumber",
                clientUserProfile.getPhoneNumber());
        //collection.update(document);

        return profile + " has been updated";
    }
    public ClientUserProfile login(String email){
        ClientUserProfile clientUserProfile = new ClientUserProfile();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        BasicDBObject basicDBObject = new BasicDBObject().append("email", email);
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String namePosted = (String)doc.get("name");
            String emailPosted = (String)doc.get("email");
            String passwordPosted = (String)doc.get("password");
            String phoneNumberPosted = (String)doc.get("phoneNumber");
            clientUserProfile.setName(namePosted);
            clientUserProfile.setEmail(emailPosted);
            clientUserProfile.setPassword(passwordPosted);
            clientUserProfile.setPhoneNumber(phoneNumberPosted);
        }
        return clientUserProfile;
    }



}
