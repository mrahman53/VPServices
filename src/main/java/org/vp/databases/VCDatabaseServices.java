package org.vp.databases;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.vp.vc.profile.*;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public List<FundingHistory> fundingHistoryList = null;
    public VCProfile vcProfile = null;

    public boolean insertVCProfileNReturn(VCProfile profile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try {
            String st = profile.getVcInfo().getVcName() + " " + "is Inserted";
            MongoClient mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
            MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
            Document vcInfoDocument = documentVCInfoData(profile);
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


    public boolean updateVCProfileByIDNReturn(VCProfile profile)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try{
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
            MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
            Document vcInfoDocument = documentVCInfoData(profile);
            Document socialDataDocument = documentVCSocialData(profile);
            List<Document> fundingHistoryDocument = documentVCFundingHistoryData(profile);
            Document filter = new Document("_id", profile.getId());
            String id = filter.values().toString().replace("[","").replace("]","");
            Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument)
                    .append("fundingHistory", fundingHistoryDocument);

            mongoCollection.updateOne(new BasicDBObject("_id",new ObjectId(id)),new BasicDBObject("$set",new BasicDBObject(preparedDocument)));
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

    public void deleteVCProfileByIDNReturn(String vcId)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try{
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
            MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
            BasicDBObject basicDBObject = new BasicDBObject("_id", new ObjectId(vcId));
            mongoCollection.deleteOne(basicDBObject);
            mongoClient.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if (mongoClient != null) {

                mongoClient = null;
            }
        }
    }

    public static Document documentVCInfoData(VCProfile profile){
        Document document = new Document().append(vcFields.vcName, profile.getVcInfo().getVcName())
                .append(vcFields.vcType, profile.getVcInfo().getVcType()).append(vcFields.vcLocation, documentVCLocationData(profile))
                .append(vcFields.numberOfDeals, profile.getVcInfo().getNumberOfDeals())
                .append(vcFields.numberOfExits, profile.getVcInfo().getNumberOfExits()).append(vcFields.vcUrl,
                 profile.getVcInfo().getVcUrl()).append(vcFields.vcEmail,profile.getVcInfo().getVcEmail())
                .append(vcFields.vcFoundedYear, profile.getVcInfo().getVcFoundedYear());

        return document;
    }

    public static Document documentVCLocationData(VCProfile profile){
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


    public List<VCProfile> queryListOfCompany()throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        List<VCProfile> vcList = new ArrayList<VCProfile>();
        vcList = readData();
        return vcList;
    }
    public List<VCProfile> readData()throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException{
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        try{
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
            MongoCollection<Document> coll = mongoDatabase.getCollection("profile");
            FindIterable<Document> iterable = coll.find();
            iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId)document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    Document socialDataDocument = (Document)document.get("socialData");
                    List<Document> fundingHistoryDocument = (List<Document>)document.get("fundingHistory");

                    String vcID = idDocument.toString();
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

                    vcInfo = new VCInfo(vcName,vcType,vcLocation,numberOfDeals,numberOfExits,vcUrl,vcEmail,vcFoundedYear);

                    String facebookUrl = (String)socialDataDocument.get("facebookUrl");
                    String twitterUrl  = (String)socialDataDocument.get("twitterUrl");
                    String linkedinUrl = (String)socialDataDocument.get("linkedinUrl");

                    socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
                    fundingHistoryList = new ArrayList<FundingHistory>();
                    if(fundingHistoryDocument!=null) {
                        for (int i = 0; i < fundingHistoryDocument.size(); i++) {
                            String fundingDate = (String) fundingHistoryDocument.get(i).get("fundingDate");
                            String companyName = (String) fundingHistoryDocument.get(i).get("companyName");
                            String fundingAmount = (String) fundingHistoryDocument.get(i).get("fundingAmount");
                            String fundingRound = (String) fundingHistoryDocument.get(i).get("fundingRound");
                            List<String> categoriesDocumentList = (List<String>) fundingHistoryDocument.get(i).get("categories");
                            List<String> categoriesList = new ArrayList<String>();
                            for (int j = 0; j < categoriesDocumentList.size(); j++) {
                                String category = categoriesDocumentList.get(j);
                                categoriesList.add(category);
                            }

                            fundingHistory = new FundingHistory(fundingDate, companyName, fundingAmount, fundingRound, categoriesList);

                            fundingHistoryList.add(fundingHistory);

                        }
                        vcProfile = new VCProfile(vcID, vcInfo, socialData, fundingHistoryList);
                        vcList.add(vcProfile);

                    }else{
                        vcProfile = new VCProfile(vcID,vcInfo,socialData);
                        vcList.add(vcProfile);
                    }
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
    public List<VCProfile> readDataFundingHistoryConnectWithNumberOfDeals()throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException{
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        try{
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
            MongoCollection<Document> coll = mongoDatabase.getCollection("profile");
            FindIterable<Document> iterable = coll.find().limit(200);
            iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId)document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    Document socialDataDocument = (Document)document.get("socialData");
                    List<Document> fundingHistoryDocument = (List<Document>)document.get("fundingHistory");

                    String vcID = idDocument.toString();
                    String vcName = (String)vcInfoDocument.get("vcName");
                    String vcType = (String)vcInfoDocument.get("vcType");
                    String vcLocationCity = (String)vcLocationDocument.get("city");
                    String vcLocationState = (String)vcLocationDocument.get("state");
                    String vcLocationCountry = (String)vcLocationDocument.get("country");
                    Location vcLocation = new Location(vcLocationCity, vcLocationState, vcLocationCountry);
//                    String numberOfDeals = (String)vcInfoDocument.get("numberOfDeals");
                    String numberOfExits = (String)vcInfoDocument.get("numberOfExits");
                    String vcUrl = (String)vcInfoDocument.get("vcUrl");
                    String vcEmail = (String)vcInfoDocument.get("vcEmail");
                    String vcFoundedYear = (String)vcInfoDocument.get("vcFoundedYear");

                    String facebookUrl = (String)socialDataDocument.get("facebookUrl");
                    String twitterUrl  = (String)socialDataDocument.get("twitterUrl");
                    String linkedinUrl = (String)socialDataDocument.get("linkedinUrl");

                    socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
                    fundingHistoryList = new ArrayList<FundingHistory>();
                    if(fundingHistoryDocument!=null) {
                        for (int i = 0; i < fundingHistoryDocument.size(); i++) {
                            String fundingDate = (String) fundingHistoryDocument.get(i).get("fundingDate");
                            String companyName = (String) fundingHistoryDocument.get(i).get("companyName");
                            String fundingAmount = (String) fundingHistoryDocument.get(i).get("fundingAmount");
                            String fundingRound = (String) fundingHistoryDocument.get(i).get("fundingRound");
                            List<String> categoriesDocumentList = (List<String>) fundingHistoryDocument.get(i).get("categories");
                            List<String> categoriesList = new ArrayList<String>();
                            for (int j = 0; j < categoriesDocumentList.size(); j++) {
                                String category = categoriesDocumentList.get(j);
                                categoriesList.add(category);
                            }

                            fundingHistory = new FundingHistory(fundingDate, companyName, fundingAmount, fundingRound, categoriesList);

                            fundingHistoryList.add(fundingHistory);

                        }
                        String numberOfDeals = Integer.toString(fundingHistoryList.size());
                        vcInfo = new VCInfo(vcName,vcType,vcLocation,numberOfDeals,numberOfExits,vcUrl,vcEmail,vcFoundedYear);
                        vcProfile = new VCProfile(vcID, vcInfo, socialData, fundingHistoryList);
                        vcList.add(vcProfile);

                    }else{
                        vcProfile = new VCProfile(vcID,vcInfo,socialData);
                        vcList.add(vcProfile);
                    }
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
    public List<VCProfile> queryListOfCompanyByPagination(int start, int size)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        try{

            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
            MongoCollection<Document> coll = mongoDatabase.getCollection("profile");
            FindIterable<Document> iterable = coll.find();
            iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId)document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    Document socialDataDocument = (Document)document.get("socialData");
                    List<Document> fundingHistoryDocument = (List<Document>)document.get("fundingHistory");

                    String vcID = idDocument.toString();
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

                    vcInfo = new VCInfo(vcName,vcType,vcLocation,numberOfDeals,numberOfExits,vcUrl,vcEmail,vcFoundedYear);

                    String facebookUrl = (String)socialDataDocument.get("facebookUrl");
                    String twitterUrl  = (String)socialDataDocument.get("twitterUrl");
                    String linkedinUrl = (String)socialDataDocument.get("linkedinUrl");

                    socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
                    fundingHistoryList = new ArrayList<FundingHistory>();
                    if(fundingHistoryDocument!=null) {
                        for (int i = 0; i < fundingHistoryDocument.size(); i++) {
                            String fundingDate = (String) fundingHistoryDocument.get(i).get("fundingDate");
                            String companyName = (String) fundingHistoryDocument.get(i).get("companyName");
                            String fundingAmount = (String) fundingHistoryDocument.get(i).get("fundingAmount");
                            String fundingRound = (String) fundingHistoryDocument.get(i).get("fundingRound");
                            List<String> categoriesDocumentList = (List<String>) fundingHistoryDocument.get(i).get("categories");
                            List<String> categoriesList = new ArrayList<String>();
                            for (int j = 0; j < categoriesDocumentList.size(); j++) {
                                String category = categoriesDocumentList.get(j);
                                categoriesList.add(category);
                            }

                            fundingHistory = new FundingHistory(fundingDate, companyName, fundingAmount, fundingRound, categoriesList);

                            fundingHistoryList.add(fundingHistory);

                        }
                        vcProfile = new VCProfile(vcID, vcInfo, socialData, fundingHistoryList);
                        vcList.add(vcProfile);

                    }else{
                        vcProfile = new VCProfile(vcID,vcInfo,socialData);
                        vcList.add(vcProfile);
                    }
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
        if(start + size >vcList.size()) return vcList;
        return vcList.subList(start, start + size);
    }
    public List<VCProfile> queryListOfCompanyByID(String vcId) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        List<VCProfile> vcList = new ArrayList<VCProfile>();
        try{

            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
            MongoCollection<Document> coll = mongoDatabase.getCollection("profile");
            BasicDBObject basicDBObject = new BasicDBObject("_id", new ObjectId(vcId));
            FindIterable<Document> iterable = coll.find(basicDBObject);
            iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId)document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    Document socialDataDocument = (Document)document.get("socialData");
                    List<Document> fundingHistoryDocument = (List<Document>)document.get("fundingHistory");

                    String vcID = idDocument.toString();
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
                    if(fundingHistoryDocument != null) {
                        for (int i = 0; i < fundingHistoryDocument.size(); i++) {
                            String fundingDate = (String) fundingHistoryDocument.get(i).get("fundingDate");
                            String companyName = (String) fundingHistoryDocument.get(i).get("companyName");
                            String fundingAmount = (String) fundingHistoryDocument.get(i).get("fundingAmount");
                            String fundingRound = (String) fundingHistoryDocument.get(i).get("fundingRound");
                            List<String> categoriesDocumentList = (List<String>) fundingHistoryDocument.get(i).get("categories");
                            List<String> categoriesList = new ArrayList<String>();
                            //categoriesList.clear();
                            for (int j = 0; j < categoriesDocumentList.size(); j++) {
                                String category = categoriesDocumentList.get(j);
                                categoriesList.add(category);
                            }
                            fundingHistory = new FundingHistory(fundingDate, companyName, fundingAmount, fundingRound, categoriesList);
                            fundingHistoryList.add(fundingHistory);

                        }


                        vcProfile = new VCProfile(vcID, vcInfo, socialData, fundingHistoryList);
                        vcList.add(vcProfile);
                    }else{
                        vcProfile = new VCProfile(vcID,vcInfo, socialData);
                        vcList.add(vcProfile);
                    }

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

    public List<VCProfile> queryListOfCompanyByName(String vcId) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        List<VCProfile> vcList = new ArrayList<VCProfile>();
        try{

        mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
        MongoCollection<Document> coll = mongoDatabase.getCollection("profile");
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("vcInfo.vcName", vcId);
        FindIterable<Document> iterable = coll.find(basicDBObject);
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                ObjectId idDocument = (ObjectId)document.get("_id");
                Document vcInfoDocument = (Document) document.get("vcInfo");
                Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                Document socialDataDocument = (Document)document.get("socialData");
                List<Document> fundingHistoryDocument = (List<Document>)document.get("fundingHistory");

                String vcID = idDocument.toString();
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
                if(fundingHistoryDocument!=null) {
                    for (int i = 0; i < fundingHistoryDocument.size(); i++) {
                        String fundingDate = (String) fundingHistoryDocument.get(i).get("fundingDate");
                        String companyName = (String) fundingHistoryDocument.get(i).get("companyName");
                        String fundingAmount = (String) fundingHistoryDocument.get(i).get("fundingAmount");
                        String fundingRound = (String) fundingHistoryDocument.get(i).get("fundingRound");
                        List<String> categoriesDocumentList = (List<String>) fundingHistoryDocument.get(i).get("categories");
                        List<String> categoriesList = new ArrayList<String>();
                        for (int j = 0; j < categoriesDocumentList.size(); j++) {
                            String category = categoriesDocumentList.get(j);
                            categoriesList.add(category);
                        }

                        fundingHistory = new FundingHistory(fundingDate, companyName, fundingAmount, fundingRound, categoriesList);

                        fundingHistoryList.add(fundingHistory);

                    }
                    vcProfile = new VCProfile(vcID, vcInfo, socialData, fundingHistoryList);
                    vcList.add(vcProfile);

                }else{
                    vcProfile = new VCProfile(vcID,vcInfo,socialData);
                    vcList.add(vcProfile);
                }
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

    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        VCProfile profile = new VCProfile();
        VCDatabaseServices vp = new VCDatabaseServices();
        vp.queryListOfCompanyByName("Edison Partners");
        //vp.deleteVCProfileByIDNReturn("58785d65394dce196a1bd0ff");
    }
}
