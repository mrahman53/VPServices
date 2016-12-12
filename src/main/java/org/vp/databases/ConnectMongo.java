package org.vp.databases;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoClientOptions;
import org.vp.vc.profile.VCProfile;
import javax.net.*;
import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by mrahman on 2/7/16.
 */
public class ConnectMongo {

    public  MongoClient mongoClient = null;
    public static MongoClientURI mongoClientURI = null;
    public static MongoClientOptions mongoClientOptions = null;
    public static MongoDatabase mongoDatabase = null;
    public static DB db = null;
    public static VCDatabaseServices vc = new VCDatabaseServices();

    public static Map<String, String> list = new LinkedHashMap<String, String>();
    public static List<Document> docList = new ArrayList<>();
    public static List<Document> documentList = new ArrayList<>();
    public static List<Object> dbObjectList = new ArrayList<>();

    private static SocketFactory _sf = null;

    public MongoDatabase connectRecommendedSSLAtlas(String dataBaseName) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {


        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        final TrustManager defaultTm = Arrays.stream(trustManagerFactory.getTrustManagers())
                .filter(tm -> tm instanceof X509TrustManager)
                .findFirst()
                .get();
        final SSLContext context = SSLContext.getInstance("TLS");
        context.init(new KeyManager[0], new TrustManager[]{defaultTm}, null);

        String userName = "vpcluster0";
        String authDB = "admin";
        char[] password = new char[]{'v', 'p', 'd', 'a', 't', 'a', 'h', 'o', 's', 't', 'i', 'n', 'g', '0'};
        MongoCredential credential = MongoCredential.createCredential(userName, authDB, password);

        MongoClientOptions.Builder optionBuilder = new MongoClientOptions.Builder();
        optionBuilder.sslEnabled(true);
        optionBuilder.socketFactory(context.getSocketFactory());
        MongoClientOptions options = optionBuilder.build();

        mongoClient = new MongoClient(Arrays.asList(
                        new ServerAddress("cluster0-shard-00-00-b2mbe.mongodb.net", 27017),
                        new ServerAddress("cluster0-shard-00-01-b2mbe.mongodb.net", 27017),
                        new ServerAddress("cluster0-shard-00-02-b2mbe.mongodb.net", 27017)
                ),
                Arrays.asList(credential), options);
        mongoDatabase = mongoClient.getDatabase(dataBaseName);
        return mongoDatabase;
    }

    public MongoClient connectToRecommendedSSLAtlasMongoClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {


        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        final TrustManager defaultTm = Arrays.stream(trustManagerFactory.getTrustManagers())
                .filter(tm -> tm instanceof X509TrustManager)
                .findFirst()
                .get();
        final SSLContext context = SSLContext.getInstance("TLS");
        context.init(new KeyManager[0], new TrustManager[]{defaultTm}, null);

        String userName = "vpcluster0";
        String authDB = "admin";
        char[] password = new char[]{'v', 'p', 'd', 'a', 't', 'a', 'h', 'o', 's', 't', 'i', 'n', 'g', '0'};
        MongoCredential credential = MongoCredential.createCredential(userName, authDB, password);

        MongoClientOptions.Builder optionBuilder = new MongoClientOptions.Builder();
        optionBuilder.sslEnabled(true);
        optionBuilder.socketFactory(context.getSocketFactory());
        MongoClientOptions options = optionBuilder.build();

        mongoClient = new MongoClient(Arrays.asList(
                new ServerAddress("cluster0-shard-00-00-b2mbe.mongodb.net", 27017),
                new ServerAddress("cluster0-shard-00-01-b2mbe.mongodb.net", 27017),
                new ServerAddress("cluster0-shard-00-02-b2mbe.mongodb.net", 27017)
        ),
                Arrays.asList(credential), options);
        return mongoClient;
    }

    public MongoDatabase connectWithSSLToAtlasWithMinimalSecurity() {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustAllCerts, null);
            _sf = context.getSocketFactory();

        } catch (GeneralSecurityException e) {
            System.out.println(e.getStackTrace());
        }
        String userName = "vpcluster0";
        String authDB = "admin";
        char[] password = new char[]{'v', 'p', 'd', 'a', 't', 'a', 'h', 'o', 's', 't', 'i', 'n', 'g', '0'};
        MongoCredential credential = MongoCredential.createCredential(userName, authDB, password);

        MongoClientOptions.Builder optionBuilder = new MongoClientOptions.Builder();
        optionBuilder.sslEnabled(true);
        optionBuilder.socketFactory(_sf);
        MongoClientOptions options = optionBuilder.build();

        mongoClient = new MongoClient(
                Arrays.asList(
                        new ServerAddress("cluster0-shard-00-00-b2mbe.mongodb.net", 27017),
                        new ServerAddress("cluster0-shard-00-01-b2mbe.mongodb.net", 27017),
                        new ServerAddress("cluster0-shard-00-02-b2mbe.mongodb.net", 27017)
                ),
                Arrays.asList(credential), options);
        mongoDatabase = mongoClient.getDatabase("devVcProfile");

        return mongoDatabase;
    }


    private static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {

        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {

        }
    }};


    public static MongoDatabase connectMLabVpDatabase1MongoDB() {

        String host = "mongodb://vpuser1:vpuser1code@ds159007-a0.mlab.com:59007,ds159007-a1.mlab.com:59007/vpdatabase1?replicaSet=rs-ds159007";
        MongoClientURI mongoClientURI = new MongoClientURI(host);
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        System.out.println("MongoDB Remote Connection Eastablished");
        mongoDatabase = mongoClient.getDatabase("vpdatabase1");
        System.out.println("Database Connected");

        return mongoDatabase;
    }

    public static MongoDatabase connectAtlasMongoClientDB() {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustAllCerts, null);
            _sf = context.getSocketFactory();
        } catch (GeneralSecurityException e) {
            System.out.println(e.getStackTrace());
        }
        mongoClientOptions = new MongoClientOptions.Builder().socketFactory(_sf).build();
        mongoClientURI = new MongoClientURI("mongodb://vpcluster0:vpdatahosting0@cluster0-shard-00-00-b2mbe.mongodb.net:27017,cluster0-shard-00-01-b2mbe.mongodb.net:27017,cluster0-shard-00-02-b2mbe.mongodb.net:27017/admin?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin");//,cluster0-shard-00-01-b2mbe.mongodb.net:27017,cluster0-shard-00-02-b2mbe.mongodb.net:27017
        MongoClient mongoClient = new MongoClient("mongodb://vpcluster0:vpdatahosting0@cluster0-shard-00-00-b2mbe.mongodb.net:27017,cluster0-shard-00-01-b2mbe.mongodb.net:27017,cluster0-shard-00-02-b2mbe.mongodb.net:27017/admin?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin", mongoClientOptions);

        String host = "mongodb://vpcluster0:vpdatahosting0@cluster0-shard-00-00-b2mbe.mongodb.net:27017,cluster0-shard-00-01-b2mbe.mongodb.net:27017,cluster0-shard-00-02-b2mbe.mongodb.net:27017/admin?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin";
        //MongoClient mongoClient = new MongoClient(new ServerAddress(host, 27017), Arrays.asList(credential));

        MongoClientURI mongoClientURI = new MongoClientURI(host);
        //MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
        mongoClient = new MongoClient(mongoClientURI);
        System.out.println("Atlas Client Connection Eastablished");
        mongoDatabase = mongoClient.getDatabase("devVcProfile");
        System.out.println("Atlas Database is Connected");

        return mongoDatabase;
    }

    public MongoDatabase connectMLabMongoStartup123ClientDB() {
        String host = "mongodb://moonuser:startup123@ds011238.mongolab.com:11238/vp";
        String port = "27017";
        MongoClientURI mongoClientURI = new MongoClientURI(host);
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        System.out.println("MongoDB Remote Connection Eastablished");
        mongoDatabase = mongoClient.getDatabase("test");
        System.out.println("Database Connected");

        return mongoDatabase;
    }

    public MongoDatabase connectLocalMongoDBClient() {
        MongoClient mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase("test");
        System.out.println("Database Connected");

        return mongoDatabase;
    }

    public void createCollection(MongoDatabase mongoDatabase, String collectionName) {
        mongoDatabase.createCollection(collectionName);

    }

    public void getCollection(MongoDatabase mongoDatabase, String collectionName) {
        mongoDatabase.getCollection(collectionName);

    }

    public void insertData() {
        MongoCollection mongoCollection = mongoDatabase.getCollection("startup");
        Document doc = new Document().append("name", "Uber").append("Year", "2009");
        mongoCollection.insertOne(doc);

    }

    public void insertDataBOBJ() {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("Jet", "2014");
        MongoCollection mongoCollection = mongoDatabase.getCollection("startup");
        mongoCollection.insertOne(basicDBObject);

    }

    public Map<String, String> queryData() {
        mongoDatabase = connectLocalMongoDBClient();
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

    public List<Document> queryDataList() {
        mongoDatabase = connectLocalMongoDBClient();
        MongoCollection coll = mongoDatabase.getCollection("startup");
        FindIterable<Document> iterable = coll.find();

        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
                docList.add(document);
            }
        });

        return docList;
    }

    public List<Document> queryDocumentList() {
        mongoDatabase = connectLocalMongoDBClient();
        MongoCollection coll = mongoDatabase.getCollection("startup");
        List<Document> list = (List<Document>) coll.find().into(new ArrayList<Document>());
        for (Document doc : list) {
            documentList.add(doc);
        }

        return documentList;
    }

    public List<Object> queryOneCompany(String companyId) {
        mongoDatabase = connectLocalMongoDBClient();
        MongoCollection coll = mongoDatabase.getCollection("startup");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("name", companyId);
        /*FindIterable<Document>  iterable = coll.find(new Document("name",companyId));
         for(Document doc:iterable){
            docList.add(doc);
        } */
        MongoCursor cursor = coll.find(basicDBObject).iterator();
        //DBCursor iterable = (DBCursor)coll.find(new Document("name", companyId));
        while (cursor.hasNext()) {
            dbObjectList.add(cursor.next());
        }

        return dbObjectList;
    }

    public static void main(String[] args)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException  {
        List<VCProfile> list = vc.queryListOfCompany("bonsai");
    }

}
