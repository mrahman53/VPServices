package org.vp.test;

import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.vp.databases.ReactiveConnectMongo;
import org.vp.databases.VCDatabaseServices;
import org.vp.vc.profile.VCProfile;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by mrahman on 7/6/18.
 */
public class TestGround {
    public static void main(String[] args)throws KeyStoreException,Throwable, NoSuchAlgorithmException, KeyManagementException {
        /*VCDatabaseServices vcDatabaseServices = new VCDatabaseServices();
        List<VCProfile> profile = vcDatabaseServices.readAllVcData(); */
        ReactiveConnectMongo connect = new ReactiveConnectMongo();
        MongoDatabase database = connect.connectRecommendedSSLAtlas("pnt");
        MongoCollection collection = database.getCollection("profile");
        FindPublisher<Document> data = collection.find();
    }
}
