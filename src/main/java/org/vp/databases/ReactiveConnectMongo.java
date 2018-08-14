package org.vp.databases;

import com.mongodb.*;

import com.mongodb.client.FindIterable;

import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.reactivestreams.client.*;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.selector.ReadPreferenceServerSelector;
import com.mongodb.selector.ServerSelector;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.bson.Document;

import static akka.stream.impl.EmptyPublisher.subscribe;
import static com.mongodb.client.model.Filters.eq;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.vp.vc.profile.VCProfile;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.File;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mrahman on 7/20/18.
 */
public class ReactiveConnectMongo {

    public MongoClient mongoClient = null;
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
    public static String dataBaseName = "vp";

    public MongoDatabase connectRecommendedSSLAtlas(String dataBaseName) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        final TrustManager defaultTm = Arrays.stream(trustManagerFactory.getTrustManagers())
                .filter(tm -> tm instanceof X509TrustManager)
                .findFirst()
                .get();
        final SSLContext context = SSLContext.getInstance("TLS");
        context.init(new KeyManager[0], new TrustManager[]{defaultTm}, null);
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        MongoClient mongoClient = MongoClients.create("mongodb+srv://vpcluster0:vpdatahosting0@cluster0-b2mbe.mongodb.net/test?streamType=netty");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("LOAD_5_VC_PROFILE");
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

        mongoClient =  MongoClients.create();
        return mongoClient;
    }

    public MongoClient connectMongoClientSSLToAtlasWithMinimalSecurity() {
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

        mongoClient = MongoClients.create();
                /*Arrays.asList(
                        new ServerAddress("cluster0-shard-00-00-b2mbe.mongodb.net", 27017),
                        new ServerAddress("cluster0-shard-00-01-b2mbe.mongodb.net", 27017),
                        new ServerAddress("cluster0-shard-00-02-b2mbe.mongodb.net", 27017)
                ),
                Arrays.asList(credential), options); */


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

        mongoClient = MongoClients.create();
                /*Arrays.asList(
                        new ServerAddress("cluster0-shard-00-00-b2mbe.mongodb.net", 27017),
                        new ServerAddress("cluster0-shard-00-01-b2mbe.mongodb.net", 27017),
                        new ServerAddress("cluster0-shard-00-02-b2mbe.mongodb.net", 27017)
                ),
                Arrays.asList(credential), options); */
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

    public void createCollection(MongoDatabase mongoDatabase, String collectionName) {
        mongoDatabase.createCollection(collectionName);

    }

    public void getCollection(MongoDatabase mongoDatabase, String collectionName) {
        mongoDatabase.getCollection(collectionName);

    }

    public static void main(String[] args)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException,
            Throwable  {
        ReactiveConnectMongo connect = new ReactiveConnectMongo();
        MongoDatabase database = connect.connectRecommendedSSLAtlas(dataBaseName);
        MongoCollection<Document> profileCollection = database.getCollection("profile");
        profileCollection.find().subscribe(new SubscriberHelpers.PrintDocumentSubscriber());

        //SubscriberHelpers.PrintDocumentSubscriber subscriber = new SubscriberHelpers.PrintDocumentSubscriber();
        //collection.find().subscribe(new SubscriberHelpers.PrintSubscriber<String>(""));
        //subscriber.await();

    }

}
