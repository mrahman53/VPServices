package org.vp.databases;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.vp.vc.profile.*;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrahman on 3/6/17.
 */
public class VCDatabasePotentialUse {

    public ConnectMongo connectMongo = new ConnectMongo();
    public MongoClient mongoClient = null;
    public static VCFields vcFields = new VCFields();
    public static VCInfo vcInfo = null;
    public static SocialData socialData = null;
    public static FundingHistory fundingHistory = null;
    public List<FundingHistory> fundingHistoryList = null;
    public VCProfile vcProfile = null;

    public boolean insertVCProfileNReturn(List<VCProfile> profile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try {

            MongoClient mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD");
            MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
            Document vcInfoDocument = documentVCInfoDataDelta(profile.get(1));
            Document socialDataDocument = documentVCSocialData(profile.get(1));
            List<Document> fundingHistoryDocument = documentVCFundingHistoryData(profile.get(1));

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
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD");
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
    public VCProfile findOneVCProfile(String vcId)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException{
        Map<Integer, Document> sData = new LinkedHashMap<>();
        Map<Integer, Object> pData = new LinkedHashMap<>();
        Map<Integer, Object> data = new LinkedHashMap<>();
        List<String> categoriesList = new ArrayList<String>();
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
                    String vcLocationCountry = (String)vcLocationDocument.get("country");
                    Location vcLocation = new Location(vcLocationCity, vcLocationState, vcLocationCountry);
                    String numberOfDeals = (String)vcInfoDocument.get("numberOfDeals");
                    String numberOfExits = (String)vcInfoDocument.get("numberOfExits");
                    String vcUrl = (String)vcInfoDocument.get("vcUrl");
                    String vcEmail = (String)vcInfoDocument.get("vcEmail");
                    String vcFoundedYear = (String)vcInfoDocument.get("vcFoundedYear");

                    vcInfo = new VCInfo(vcName,vcType,vcLocation,numberOfDeals, numberOfExits, vcUrl,vcEmail,vcFoundedYear);

                    String facebookUrl = (String)socialDataDocument.get("facebookUrl");
                    String twitterUrl  = (String)socialDataDocument.get("twitterUrl");
                    String linkedinUrl = (String)socialDataDocument.get("linkedinUrl");

                    socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
                    fundingHistoryList = new ArrayList<FundingHistory>();
                    for(int i=0; i<fundingHistoryDocument.size(); i++) {
                        String fundingDate = (String) fundingHistoryDocument.get(i).get("fundingDate");
                        String companyName = (String) fundingHistoryDocument.get(i).get("companyName");
                        String fundingAmount = (String) fundingHistoryDocument.get(i).get("fundingAmount");
                        String fundingRound = (String) fundingHistoryDocument.get(i).get("fundingRound");
                        List<Document> categoriesDocumentList = (List<Document>)document.get("categories");
                        for(int j=0; j<categoriesDocumentList.size(); j++) {
                            String category = (String) categoriesDocumentList.get(j).get("categories");
                            categoriesList.add(category);
                        }

                        fundingHistory = new FundingHistory(fundingDate, companyName, fundingAmount, fundingRound, categoriesList);

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


    public static Document documentVCInfoDataDelta(VCProfile profile){
        Document document = new Document().append(vcFields.vcName, profile.getVcInfo().getVcName())
                .append(vcFields.vcType, profile.getVcInfo().getVcType()).append(vcFields.vcLocation,vcLocationDocument(profile))
                .append(vcFields.numberOfDeals, profile.getVcInfo().getNumberOfDeals())
                .append(vcFields.numberOfExits, profile.getVcInfo().getNumberOfExits()).append(vcFields.vcUrl,
                        profile.getVcInfo().getVcUrl()).append(vcFields.vcEmail,profile.getVcInfo().getVcEmail())
                .append(vcFields.vcFoundedYear, profile.getVcInfo().getVcFoundedYear());

        return document;
    }

    public static Document vcLocationDocument(VCProfile profile){
        Document document = new Document().append(vcFields.city, profile.getVcInfo()
                .getVcLocation().getCity()).append(vcFields.state, profile.getVcInfo().getVcLocation().getState())
                .append(vcFields.country, profile.getVcInfo().getVcLocation().getCountry());

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
    public boolean deleteVCProfileNReturn(String profile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try{
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD");
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
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD");
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
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD");
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

}
