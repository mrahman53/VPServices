package org.vp.databases;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.vp.vc.profile.*;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by mrahman on 7/17/16.
 */
public class VCDatabaseServices {
    public ConnectMongo connectMongo = new ConnectMongo();
    public MongoClient mongoClient = null;
    public static VCFields vcFields = new VCFields();
    public static VCInfo vcInfo = null;
    public static SocialData socialData = null;
    public static FundingHistory fundingHistory = null;
    public List<FundingHistory> fundingHistoryList = new ArrayList<FundingHistory>();
    public VCProfile vcProfile = null;

    public boolean insertVCProfileNReturn(VCProfile profile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try {
            String st = profile.getVcInfo().getVcName() + " " + "is Inserted";
            MongoClient mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("devVcProfile");
            MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
            Document vcInfoDocument = documentVCInfoDataDelta(profile);
            Document socialDataDocument = documentVCSocialData(profile);
            List<Document> fundingHistoryDocument = documentVCFundingHistoryData(profile);

            Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument)
                    .append("fundingHistory", fundingHistoryDocument);

            mongoCollection.insertOne(preparedDocument);
            mongoClient.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }finally {
                if (connectMongo.mongoClient != null) {

                    connectMongo.mongoClient = null;
                }
            }

        return true;
    }

    public boolean updateVCProfileNReturn(VCProfile profile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try{
            //String filter = profile.getVcInfo().getVcName();
            mongoClient = connectMongo.connectMongoClientSSLToAtlasWithMinimalSecurity();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("devVcProfile");
            MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
            Document vcInfoDocument = documentVCInfoDataDelta(profile);
            Document socialDataDocument = documentVCSocialData(profile);
            List<Document> fundingHistoryDocument = documentVCFundingHistoryData(profile);
            Document filter = new Document("vcInfo.vcName", profile.getVcInfo().getVcName());
            Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument)
                    .append("fundingHistory", fundingHistoryDocument);

            mongoCollection.updateMany(filter,new Document("$set",preparedDocument));
            mongoClient.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if (mongoClient != null) {

                mongoClient = null;
            }
        }
        return true;
    }
    public boolean deleteVCProfileNReturn(String profile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try{
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("devVcProfile");
            MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
            Document filter = new Document("vcInfo.vcName", profile);
            mongoCollection.findOneAndDelete(filter);

            mongoClient.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if (mongoClient != null) {

                mongoClient = null;
            }
        }
        return true;
    }
    public boolean deleteVCProfileNReturn(VCProfile profile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try{
        String st = profile.getVcInfo().getVcName()+" "+ "is Inserted";
        mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase("devVcProfile");
        MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
        Document vcInfoDocument = documentVCInfoDataDelta(profile);
        Document socialDataDocument = documentVCSocialData(profile);
        List<Document> fundingHistoryDocument = documentVCFundingHistoryData(profile);
        Document filter = new Document("vcInfo.vcName", profile.getVcInfo().getVcName());
        Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument)
                .append("fundingHistory", fundingHistoryDocument);

        mongoCollection.findOneAndDelete(filter);

        mongoClient.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if (mongoClient != null) {

                mongoClient = null;
            }
        }
            return true;
        }

    public String insertVCProfile(VCProfile profile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String st = profile.getVcInfo().getVcName() + " " + "is Inserted";
        try {
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("devVcProfile");
            MongoCollection mongoCollection = mongoDatabase.getCollection("vc");
            Document vcInfoDocument = documentVCInfoDataDelta(profile);
            Document socialDataDocument = documentVCSocialData(profile);
            List<Document> fundingHistoryDocument = documentVCFundingHistoryData(profile);

            Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument)
                    .append("fundingHistory", fundingHistoryDocument);

            mongoCollection.insertOne(preparedDocument);

            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            if (mongoClient != null) {
                mongoClient = null;
            }
        }

        return st;
    }

    public static Document documentVCInfoDataDelta(VCProfile profile){
        String vcLocation = "vcLocation";
        Document document = new Document().append(vcFields.vcName, profile.getVcInfo().getVcName())
                .append(vcFields.vcType, profile.getVcInfo().getVcType()).append(vcLocation,vcLocationDocument(profile))
                .append(vcFields.numberOfDeals, profile.getVcInfo().getNumberOfDeals()).append(vcFields.vcUrl,
                        profile.getVcInfo().getVcUrl()).append(vcFields.vcEmail,profile.getVcInfo().getVcEmail())
                .append(vcFields.vcFoundedYear, profile.getVcInfo().getVcFoundedYear());

        return document;
    }

    public static Document vcLocationDocument(VCProfile profile){
        Document document = new Document().append(vcFields.city, profile.getVcInfo()
                .getVcLocation().getCity()).append(vcFields.state, profile.getVcInfo().getVcLocation().getState());

        return document;
    }

    public static Document documentVCInfoData(VCProfile profile){
        Document document = new Document().append(vcFields.vcName, profile.getVcInfo().getVcName())
                .append(vcFields.vcType, profile.getVcInfo().getVcType()).append(vcFields.city, profile.getVcInfo()
                .getVcLocation().getCity()).append(vcFields.state, profile.getVcInfo().getVcLocation().getState())
                .append(vcFields.numberOfDeals, profile.getVcInfo().getNumberOfDeals()).append(vcFields.vcUrl,
                 profile.getVcInfo().getVcUrl()).append(vcFields.vcEmail,profile.getVcInfo().getVcEmail())
                .append(vcFields.vcFoundedYear, profile.getVcInfo().getVcFoundedYear());

        return document;
    }
    public static Document documentVCSocialData(VCProfile profile){
        Document document = new Document().append(vcFields.facebookUrl, profile.getSocialData().getFacebookUrl())
                .append(vcFields.twitterUrl, profile.getSocialData().getTwitterUrl()).append(vcFields.linkedinUrl,
                profile.getSocialData().getLinkedinUrl());

        return document;
    }

    public static List<Document> documentVCFundingHistoryData(VCProfile profile){
        List<Document> fundingHistoryData = new ArrayList<>();
        Document document = null;
        for(FundingHistory pr:profile.getFundingHistory()) {
            document = new Document().append(vcFields.fundingDate, pr.getFundingDate()).append(vcFields.companyName,
            pr.getCompanyName()).append(vcFields.fundingAmount,pr.getFundingAmount()).append(vcFields.fundingRound,
            pr.getFundingRound()).append(vcFields.categories, pr.getCategories());

            fundingHistoryData.add(document);
        }

        return fundingHistoryData;
    }

    public VCProfile findOneVCProfile(String vcId)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException{
        Map<Integer, Document> sData = new LinkedHashMap<>();
        Map<Integer, Object> pData = new LinkedHashMap<>();
        Map<Integer, Object> data = new LinkedHashMap<>();
        try{

            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("devVcProfile");
            MongoCollection<Document> coll = mongoDatabase.getCollection("vp");
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("vcInfo.vcName", vcId);
            FindIterable<Document> iterable = coll.find(basicDBObject);
            iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {

                Document vcInfoDocument = (Document) document.get("vcInfo");
                Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                Document socialDataDocument = (Document)document.get("socialData");
                List<Document> fundingHistoryDocument = (List<Document>)document.get("fundingHistory");


                String vcName = (String)vcInfoDocument.get("vcName");
                String vcType = (String)vcInfoDocument.get("vcType");
                String vcLocationCity = (String)vcLocationDocument.get("city");
                String vcLocationState = (String)vcLocationDocument.get("state");
                Location vcLocation = new Location(vcLocationCity, vcLocationState);
                String numberOfDeals = (String)vcInfoDocument.get("numberOfDeals");
                String vcUrl = (String)vcInfoDocument.get("vcUrl");
                String vcEmail = (String)vcInfoDocument.get("vcEmail");
                String vcFoundedYear = (String)vcInfoDocument.get("vcFoundedYear");

                vcInfo = new VCInfo(vcName,vcType,vcLocation,numberOfDeals, vcUrl,vcEmail,vcFoundedYear);

                String facebookUrl = (String)socialDataDocument.get("facebookUrl");
                String twitterUrl  = (String)socialDataDocument.get("twitterUrl");
                String linkedinUrl = (String)socialDataDocument.get("linkedinUrl");

                socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);

                for(int i=0; i<fundingHistoryDocument.size(); i++) {
                    String fundingDate = (String) fundingHistoryDocument.get(i).get("fundingDate");
                    String companyName = (String) fundingHistoryDocument.get(i).get("companyName");
                    String fundingAmount = (String) fundingHistoryDocument.get(i).get("fundingAmount");
                    String fundingRound = (String) fundingHistoryDocument.get(i).get("fundingRound");
                    String categories = (String) fundingHistoryDocument.get(i).get("categories");

                    fundingHistory = new FundingHistory(fundingDate, companyName, fundingAmount, fundingRound, categories);
                    fundingHistoryList.add(fundingHistory);
                }

                vcProfile = new VCProfile(vcInfo,socialData,fundingHistoryList);


            }


        });

            mongoClient.close();

        }catch(Exception ex){
            ex.printStackTrace();
        }finally {

            if (mongoClient != null) {

                mongoClient = null;
            }
        }

        return vcProfile;
    }

    public List<VCProfile> queryListOfCompany()throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        try{

            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("devVcProfile");
            MongoCollection<Document> coll = mongoDatabase.getCollection("profile");
            FindIterable<Document> iterable = coll.find();
            iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {

                Document vcInfoDocument = (Document) document.get("vcInfo");
                Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                Document socialDataDocument = (Document)document.get("socialData");
                List<Document> fundingHistoryDocument = (List<Document>)document.get("fundingHistory");

                String vcName = (String)vcInfoDocument.get("vcName");
                String vcType = (String)vcInfoDocument.get("vcType");
                String vcLocationCity = (String)vcLocationDocument.get("city");
                String vcLocationState = (String)vcLocationDocument.get("state");
                Location vcLocation = new Location(vcLocationCity, vcLocationState);
                String numberOfDeals = (String)vcInfoDocument.get("numberOfDeals");
                String vcUrl = (String)vcInfoDocument.get("vcUrl");
                String vcEmail = (String)vcInfoDocument.get("vcEmail");
                String vcFoundedYear = (String)vcInfoDocument.get("vcFoundedYear");

                vcInfo = new VCInfo(vcName,vcType,vcLocation,numberOfDeals, vcUrl,vcEmail,vcFoundedYear);

                String facebookUrl = (String)socialDataDocument.get("facebookUrl");
                String twitterUrl  = (String)socialDataDocument.get("twitterUrl");
                String linkedinUrl = (String)socialDataDocument.get("linkedinUrl");

                socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);

                for(int i=0; i<fundingHistoryDocument.size(); i++) {
                    String fundingDate = (String) fundingHistoryDocument.get(i).get("fundingDate");
                    String companyName = (String) fundingHistoryDocument.get(i).get("companyName");
                    String fundingAmount = (String) fundingHistoryDocument.get(i).get("fundingAmount");
                    String fundingRound = (String) fundingHistoryDocument.get(i).get("fundingRound");
                    String categories = (String) fundingHistoryDocument.get(i).get("categories");
                    fundingHistory = new FundingHistory(fundingDate, companyName, fundingAmount, fundingRound, categories);
                    fundingHistoryList.add(fundingHistory);
                }


                vcProfile = new VCProfile(vcInfo,socialData,fundingHistoryList);
                vcList.add(vcProfile);

            }

        });

            mongoClient.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {

            if (mongoClient != null) {

                mongoClient = null;
            }
        }
        return vcList;
    }

    public List<VCProfile> queryListOfCompany(String vcId) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        List<VCProfile> vcList = new ArrayList<VCProfile>();
        try{

        mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase("devVcProfile");
        MongoCollection<Document> coll = mongoDatabase.getCollection("profile");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("vcInfo.vcName", vcId);
        FindIterable<Document> iterable = coll.find(basicDBObject);
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {

                Document vcInfoDocument = (Document) document.get("vcInfo");
                Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                Document socialDataDocument = (Document)document.get("socialData");
                List<Document> fundingHistoryDocument = (List<Document>)document.get("fundingHistory");

                String vcName = (String)vcInfoDocument.get("vcName");
                String vcType = (String)vcInfoDocument.get("vcType");
                String vcLocationCity = (String)vcLocationDocument.get("city");
                String vcLocationState = (String)vcLocationDocument.get("state");
                Location vcLocation = new Location(vcLocationCity, vcLocationState);
                String numberOfDeals = (String)vcInfoDocument.get("numberOfDeals");
                String vcUrl = (String)vcInfoDocument.get("vcUrl");
                String vcEmail = (String)vcInfoDocument.get("vcEmail");
                String vcFoundedYear = (String)vcInfoDocument.get("vcFoundedYear");

                vcInfo = new VCInfo(vcName,vcType,vcLocation,numberOfDeals, vcUrl,vcEmail,vcFoundedYear);

                String facebookUrl = (String)socialDataDocument.get("facebookUrl");
                String twitterUrl  = (String)socialDataDocument.get("twitterUrl");
                String linkedinUrl = (String)socialDataDocument.get("linkedinUrl");

                socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);

                for(int i=0; i<fundingHistoryDocument.size(); i++) {
                    String fundingDate = (String) fundingHistoryDocument.get(i).get("fundingDate");
                    String companyName = (String) fundingHistoryDocument.get(i).get("companyName");
                    String fundingAmount = (String) fundingHistoryDocument.get(i).get("fundingAmount");
                    String fundingRound = (String) fundingHistoryDocument.get(i).get("fundingRound");
                    String categories = (String) fundingHistoryDocument.get(i).get("categories");
                    fundingHistory = new FundingHistory(fundingDate, companyName, fundingAmount, fundingRound, categories);
                    fundingHistoryList.add(fundingHistory);
                }


                vcProfile = new VCProfile(vcInfo,socialData,fundingHistoryList);
                vcList.add(vcProfile);

            }

        });

        mongoClient.close();

        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if (mongoClient != null) {

                mongoClient = null;
            }
        }
        return vcList;
    }

    public static void main(String[] args) {
        VCProfile profile = new VCProfile();
        //VCDatabaseServices.updateVCProfileNReturn("Uber");
    }
}
