package org.vp.databases;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrahman on 2/7/16.
 */
public class ConnectDB {

    public MongoClient mongoClient = null;
    public static MongoDatabase mongoDatabase = null;
    public static DB db = null;

    public static Map<String, String> list = new LinkedHashMap<String,String>();
    public static List<Document> docList = new ArrayList<>();
    public static List<Document> documentList = new ArrayList<>();
    public static List<Object> dbObjectList = new ArrayList<>();


    public MongoDatabase connectRemoteMongoClientDB(){
        String host= "mongodb://moonuser:startup123@ds011238.mongolab.com:11238/vp";
        String port = "27017";
        MongoClientURI mongoClientURI = new MongoClientURI(host);
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        System.out.println("MongoDB Remote Connection Eastablished");
        mongoDatabase = mongoClient.getDatabase("test");
        System.out.println("Database Connected");

        return mongoDatabase;
    }
    public MongoDatabase connectDBClient(){
        MongoClient mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase("test");
        System.out.println("Database Connected");

        return mongoDatabase;
    }

    public void createCollection(MongoDatabase mongoDatabase, String collectionName){
        mongoDatabase.createCollection(collectionName);

    }
    public void getCollection(MongoDatabase mongoDatabase, String collectionName){
        mongoDatabase.getCollection(collectionName);

    }

    public void insertData(){
        MongoCollection mongoCollection = mongoDatabase.getCollection("startup");
        Document doc = new Document().append("name", "Uber").append("Year", "2009");
        mongoCollection.insertOne(doc);

    }

    public void insertDataBOBJ(){
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("Jet", "2014");
        MongoCollection mongoCollection = mongoDatabase.getCollection("startup");
        mongoCollection.insertOne(basicDBObject);

    }
    public Map<String,String> queryData(){
        mongoDatabase = connectDBClient();
        MongoCollection coll = mongoDatabase.getCollection("startup");
        FindIterable<Document> iterable = coll.find();

        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
                list.put(document.toJson(), document.toJson());
            }
        });

      return list;
    }
    public List<Document> queryDataList(){
        mongoDatabase = connectDBClient();
        MongoCollection coll = mongoDatabase.getCollection("startup");
        FindIterable<Document> iterable = coll.find();

        iterable.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document){
                System.out.println(document.toJson());
                docList.add(document);
            }
        });

        return docList;
    }
    public List<Document> queryDocumentList(){
        mongoDatabase = connectDBClient();
        MongoCollection coll = mongoDatabase.getCollection("startup");
        List<Document> list = (List<Document>)coll.find().into(new ArrayList<Document>());
        for(Document doc:list){
            documentList.add(doc);
        }

        return documentList;
    }
    public List<Object> queryOneCompany(String companyId){
        mongoDatabase = connectDBClient();
        MongoCollection coll = mongoDatabase.getCollection("startup");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("name", companyId);
        /*FindIterable<Document>  iterable = coll.find(new Document("name",companyId));
         for(Document doc:iterable){
            docList.add(doc);
        } */
        MongoCursor cursor = coll.find(basicDBObject).iterator();
        //DBCursor iterable = (DBCursor)coll.find(new Document("name", companyId));
        while(cursor.hasNext()){
            dbObjectList.add(cursor.next());
       }

        return dbObjectList;
    }



    public static void main(String[] args) {
        //mongoDatabase = connectMongoClientDB();
        //insertDataBOBJ();
        //mongoDatabase.getCollection("startup").deleteOne(new Document("name", "Uber"));
//        List<CompanyP> yearList = queryListOfCompanyByFundingAmount("$100000");
//        for(CompanyP company:yearList){
//            System.out.println(company.getCompanyName()+" "+company.getYearFounded()+" "+company.getLocation()+" "+company.fundingAmount);
//        }

       // registration("mrahman", "mrahmanww@gmail.com", "mrahman123", "347-503-9266");
//        User user = login("mrahmanww@gmail.com");
//        if(user.getEmail().equals("rahmanww@gmail.com")&& user.getPassword().equals("rahman12")){
//            System.out.println("Authenticated");
//        }else{
//            System.out.println("not authenticated");
//        }

       // MongoDatabase md = connectRemoteMongoClientDB();
    }
}
