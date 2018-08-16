package org.vp.databases;

import akka.NotUsed;
import akka.http.scaladsl.server.directives.Credentials;
import akka.stream.javadsl.JavaFlowSupport;
import akka.stream.javadsl.Source;
import com.mongodb.*;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.Observables;
import com.mongodb.reactivestreams.client.*;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.selector.ReadPreferenceServerSelector;
import com.mongodb.selector.ServerSelector;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.bson.Document;

import static akka.stream.impl.EmptyPublisher.subscribe;
import static com.mongodb.client.model.Filters.eq;

import org.eclipse.persistence.jaxb.Crate;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.vp.vc.profile.VCProfile;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;
import rx.RxReactiveStreams.*;

/**
 * Created by mrahman on 7/20/18.
 */
public class ReactiveConnectMongo {

    public MongoClient mongoClient = null;
    public static MongoClientURI mongoClientURI = null;
    public static MongoClientOptions mongoClientOptions = null;
    public static MongoDatabase mongoDatabase = null;
    public static MongoCollection<Document> profileCollection = null;
    public static DB db = null;
    public static VCDatabaseServices vc = new VCDatabaseServices();

    public static Map<String, String> list = new LinkedHashMap<String, String>();
    public static List<Document> docList = new ArrayList<>();
    public static List<Document> documentList = new ArrayList<>();
    public static List<Object> dbObjectList = new ArrayList<>();

    private static SocketFactory _sf = null;
    public static String dataBaseName = "LOAD_5_VC_PROFILE";

    public ReactiveConnectMongo()throws Throwable{
        final MongoDatabase db = connectRecommendedSSLAtlas(dataBaseName);
        profileCollection = db.getCollection("profile");
    }

    public MongoDatabase connectRecommendedSSLAtlas(String dataBaseName)throws Throwable {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        MongoClient mongoClient = MongoClients.create("mongodb+srv://vpcluster0:vpdatahosting0@cluster0-b2mbe.mongodb.net/test?streamType=netty");
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dataBaseName);
        return mongoDatabase;
    }

    public void readProfileCollection()throws Throwable {
        MongoCollection<Document> profileCollection = mongoDatabase.getCollection("profile");
        SubscriberHelpers.PrintDocumentSubscriber subscriber = new SubscriberHelpers.PrintDocumentSubscriber();
        profileCollection.find().subscribe(subscriber);
        subscriber.await();
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
    /*
    private SingleResultCallback<VCProfile> _findUserByName(final String name) {
        return profileCollection.find().subscribe()
                .map(doc -> new User(doc));
    }
    private Source<String, NotUsed> sourceData() {
        return JavaFlowSupport.Source.fromPublisher(ReactiveConnectMongo.c(credentials.username))
                .map(user -> checkUserLoggedIn(user, credentials))
                .map(user -> user.name);
    } */
    /*
    Observables.observe(collection.find()).subscribe(new Observer<Document>(){
        private long batchSize = 10;
        private long seen = 0;
        private Subscription subscription;

        @Override
        void onSubscribe(final Subscription subscription) {
            this.subscription = subscription;
            subscription.request(batchSize);
        }

        @Override
        void onNext(final Document document) {
            System.out.println(document.toJson());
            seen += 1;
            if (seen == batchSize) {
                seen = 0;
                subscription.request(batchSize);
            }
        }

        @Override
        void onError(final Throwable e) {
            System.out.println("There was an error: " + e.getMessage());
        }

        @Override
        void onComplete() {
            System.out.println("Finished iterating all documents");
        }
    }); */

}
