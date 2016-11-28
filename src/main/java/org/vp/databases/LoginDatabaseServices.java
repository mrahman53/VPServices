package org.vp.databases;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.vp.authentication.LoginUser;


/**
 * Created by mrahman on 8/24/16.
 */
public class LoginDatabaseServices {
    public ConnectDB connectDB = new ConnectDB();
    public MongoDatabase mongoDatabase = null;

    public String adminRegistration(LoginUser user){
        String profile = user.getUsername();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        Document document = new Document().append("username",user.getUsername()).append("password", user.getPassword());
        collection.insertOne(document);
        return profile + " has been registered";
    }
    public String updateAdminUserProfile(LoginUser user){
        String profile = user.getUsername();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
        MongoCollection<Document> collection = mongoDatabase.getCollection("registration");
        Document document = new Document().append("username",user.getUsername()).append("password", user.getPassword());

        return profile + " has been updated";
    }
    public LoginUser register(String username){
        LoginUser user = new LoginUser();
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
            user.setFirstName(firstNamePosted);
            user.setLastName(lastNamePosted);
        }
        return user;
    }
    public LoginUser login(String username){
        LoginUser user = new LoginUser();
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
            user.setFirstName(firstNamePosted);
            user.setLastName(lastNamePosted);
            user.setValue(true);
        }
        return user;
    }
    public boolean loginVerify(String username, String password){
        LoginUser user = new LoginUser();
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
            user.setFirstName(firstNamePosted);
            user.setLastName(lastNamePosted);
            user.setValue(true);
        }
        if(user.getUsername().equals(username) && user.getPassword().equals(password)){
            return true;
        }else{
            return false;
        }

    }
    public boolean loginVerify(String username){
        LoginUser user = new LoginUser();
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
            user.setFirstName(firstNamePosted);
            user.setLastName(lastNamePosted);
            user.setValue(true);
        }
        return user.isValue();
    }
//    public VCProfile findOneVCProfile(String vcId){
//        Map<Integer, Document> sData = new LinkedHashMap<>();
//        Map<Integer, Object> pData = new LinkedHashMap<>();
//        Map<Integer, Object> data = new LinkedHashMap<>();
//        StartupProfile profile = new StartupProfile();
//        mongoDatabase = connectDB.connectMongoClientDB();
//        MongoCollection<Document> coll = mongoDatabase.getCollection("vc");
//        BasicDBObject basicDBObject = new BasicDBObject();
//        basicDBObject.put("vcInfo.vcName", vcId);
//        FindIterable<Document> iterable = coll.find(basicDBObject);
//        iterable.forEach(new Block<Document>() {
//            @Override
//            public void apply(final Document document) {
//
//                Document vcInfoDocument = (Document) document.get("vcInfo");
//                Document socialDataDocument = (Document)document.get("socialData");
//                List<Document> fundingHistoryDocument = (List<Document>)document.get("fundingHistory");
//
//
//                String vcName = (String)vcInfoDocument.get("vcName");
//                String vcType = (String)vcInfoDocument.get("vcType");
//                String vcLocationCity = (String)vcInfoDocument.get("city");
//                String vcLocationState = (String)vcInfoDocument.get("state");
//                Location vcLocation = new Location(vcLocationCity, vcLocationState);
//                String numberOfDeals = (String)vcInfoDocument.get("numberOfDeals");
//                String vcUrl = (String)vcInfoDocument.get("vcUrl");
//                String vcEmail = (String)vcInfoDocument.get("vcEmail");
//                String vcFoundedYear = (String)vcInfoDocument.get("vcFoundedYear");
//
//                vcInfo = new VCInfo(vcName,vcType,vcLocation,numberOfDeals, vcUrl,vcEmail,vcFoundedYear);
//
//                String facebookUrl = (String)socialDataDocument.get("facebookUrl");
//                String twitterUrl  = (String)socialDataDocument.get("twitterUrl");
//                String linkedinUrl = (String)socialDataDocument.get("linkedinUrl");
//
//                socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
//
//                String fundingDate = (String)fundingHistoryDocument.get(1).get("fundingDate");
//                String companyName = (String)fundingHistoryDocument.get(1).get("companyName");
//                String fundingAmount = (String)fundingHistoryDocument.get(1).get("fundingAmount");
//                String fundingRound = (String)fundingHistoryDocument.get(1).get("fundingRound");
//                String categories = (String)fundingHistoryDocument.get(1).get("categories");
//
//                fundingHistory = new FundingHistory(fundingDate,companyName,fundingAmount,fundingRound,categories);
//                fundingHistoryList.add(fundingHistory);
//
//
//                vcProfile = new VCProfile(vcInfo,socialData,fundingHistoryList);
//
//            }
//
//        });
//
//
//        return vcProfile;
//    }

}
