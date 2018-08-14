package org.vp.databases;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.MongoClient;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.vp.cache.JedisMain;
import org.vp.vc.profile.*;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mrahman on 7/20/18.
 */
public class ReactiveVCDBServices {
    public ReactiveConnectMongo connectMongo = null;
    public MongoClient mongoClient = null;
    public MongoDatabase mongoDatabase = null;
    public MongoCollection mongoCollection = null;
    public MongoCollection<Document> coll = null;
    public BasicDBObject basicDBObject = null;
    public FindPublisher<Document> iterable = null;
    public static VCFields vcFields = new VCFields();
    public static VCInfo vcInfo = null;
    public static SocialData socialData = null;
    public static FundingHistory fundingHistory = null;
    public static FundRaised fundRaised = null;
    public static IpoNAcquisitions ipoNAcquisitions = null;
    public List<FundingHistory> fundingHistoryList = null;
    public List<FundRaised> fundRaisedList = null;
    public List<IpoNAcquisitions> ipoNAcquisitionsList = null;
    public VCProfile vcProfile = null;
    String databaseName = "PROD_VC_PROFILE";

    public boolean insertVCProfileNReturn(VCProfile profile) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try {
            String st = profile.getVcInfo().getVcName() + " " + "is Inserted";
            connectMongo = new ReactiveConnectMongo();
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            mongoDatabase = mongoClient.getDatabase(databaseName);
            mongoCollection = mongoDatabase.getCollection("profile");
            Document vcInfoDocument = documentVCInfoData(profile);
            Document socialDataDocument = documentVCSocialData(profile);
            List<Document> fundingHistoryDocument = documentVCFundingHistoryData(profile);
            List<Document> fundRaisedDocument = documentVCFundRaisedData(profile);
            List<Document> ipoNAcquisitionsDocument = documentVCIpoNAcquisitionsData(profile);

            Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument)
                    .append("fundingHistory", fundingHistoryDocument).append("fundRaised", fundRaisedDocument)
                    .append("ipoNAcquisitions", ipoNAcquisitionsDocument);

            mongoCollection.insertOne(preparedDocument);
            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (mongoCollection != null) {
                mongoCollection = null;
            }
            if (mongoDatabase != null) {
                mongoDatabase = null;

            }
            if (mongoClient != null) {
                mongoClient = null;
            }
            if (connectMongo != null) {
                connectMongo = null;
            }
        }

        return true;
    }


    public boolean updateVCProfileByIDNReturnOld(VCProfile profile) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try {
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
            MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
            Document vcInfoDocument = documentVCInfoData(profile);
            Document socialDataDocument = documentVCSocialData(profile);
            List<Document> fundingHistoryDocument = documentVCFundingHistoryData(profile);
            Document filter = new Document("_id", profile.getId());
            String id = filter.values().toString().replace("[", "").replace("]", "");
            Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument)
                    .append("fundingHistory", fundingHistoryDocument);

            mongoCollection.updateOne(new BasicDBObject("_id", new ObjectId(id)), new BasicDBObject("$set", new BasicDBObject(preparedDocument)));
            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (mongoClient != null) {

                mongoClient = null;
            }
        }
        return true;
    }

    public boolean updateVCProfileByIDNReturn(VCProfile profile) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        try {
            connectMongo = new ReactiveConnectMongo();
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            mongoDatabase = mongoClient.getDatabase(databaseName);
            mongoCollection = mongoDatabase.getCollection("profile");
            Document vcInfoDocument = documentVCInfoData(profile);
            Document socialDataDocument = documentVCSocialData(profile);
            List<Document> fundingHistoryDocument = documentVCFundingHistoryData(profile);
            List<Document> fundRaisedDocument = documentVCFundRaisedData(profile);
            List<Document> ipoNAcquisitionsDocument = documentVCIpoNAcquisitionsData(profile);
            Document filter = new Document("_id", profile.getId());
            String id = filter.values().toString().replace("[", "").replace("]", "");
            Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument)
                    .append("fundingHistory", fundingHistoryDocument).append("fundRaised", fundRaisedDocument).
                            append("ipoNAcquisitions", ipoNAcquisitionsDocument);

            mongoCollection.updateOne(new BasicDBObject("_id", new ObjectId(id)), new BasicDBObject("$set", new BasicDBObject(preparedDocument)));
            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (mongoCollection != null) {
                mongoCollection = null;
            }
            if (mongoDatabase != null) {
                mongoDatabase = null;

            }
            if (mongoClient != null) {
                mongoClient = null;
            }
            if (connectMongo != null) {
                connectMongo = null;
            }
        }
        return true;
    }

    public void deleteVCProfileByIDNReturn(String vcId) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        connectMongo = new ReactiveConnectMongo();
        try {
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            mongoDatabase = mongoClient.getDatabase(databaseName);
            mongoCollection = mongoDatabase.getCollection("profile");
            basicDBObject = new BasicDBObject("_id", new ObjectId(vcId));
            mongoCollection.deleteOne(basicDBObject);
            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (basicDBObject != null) {
                basicDBObject = null;
            }
            if (mongoCollection != null) {
                mongoCollection = null;
            }
            if (mongoDatabase != null) {
                mongoDatabase = null;

            }
            if (mongoClient != null) {
                mongoClient = null;
            }
            if (connectMongo != null) {
                connectMongo = null;
            }
        }
    }

    public static Document documentVCInfoData(VCProfile profile) {
        String processedVcTarget = "";
        String vcTarget = profile.getVcInfo().getVcName();
        if (vcTarget.contains("&") || vcTarget.contains(" ") || vcTarget.contains(".") || vcTarget.contains("#") || vcTarget.contains("_")) {
            processedVcTarget = vcTarget.replace("&", "-").replace(".", "-").replace("#", "-")
                    .replace("_", "-");
            processedVcTarget.toLowerCase();
        }
        Document document = new Document().append(vcFields.vcTargetName, processedVcTarget).append(vcFields.vcName, profile.getVcInfo().getVcName().toLowerCase())
                .append(vcFields.vcType, profile.getVcInfo().getVcType()).append(vcFields.vcLocation, documentVCLocationData(profile))
                .append(vcFields.numberOfDeals, profile.getVcInfo().getNumberOfDeals())
                .append(vcFields.numberOfExits, profile.getVcInfo().getNumberOfExits()).append(vcFields.vcUrl,
                        profile.getVcInfo().getVcUrl()).append(vcFields.vcEmail, profile.getVcInfo().getVcEmail())
                .append(vcFields.vcFoundedYear, profile.getVcInfo().getVcFoundedYear()).append(vcFields.vcPhoneNumber,
                        profile.getVcInfo().getVcPhoneNumber());

        return document;
    }

    public static Document documentVCLocationData(VCProfile profile) {
        Document document = new Document().append(vcFields.city, profile.getVcInfo()
                .getVcLocation().getCity()).append(vcFields.state, profile.getVcInfo().getVcLocation().getState())
                .append(vcFields.country, profile.getVcInfo().getVcLocation().getCountry());

        return document;
    }

    public static Document documentVCSocialData(VCProfile profile) {
        Document document = new Document().append(vcFields.facebookUrl, profile.getSocialData().getFacebookUrl())
                .append(vcFields.twitterUrl, profile.getSocialData().getTwitterUrl()).append(vcFields.linkedinUrl,
                        profile.getSocialData().getLinkedinUrl());

        return document;
    }

    public static List<Document> documentVCFundingHistoryData(VCProfile profile) {
        List<Document> fundingHistoryData = new ArrayList<>();
        Document document = null;
        for (FundingHistory pr : profile.getFundingHistory()) {
            document = new Document().append(vcFields.fundingDate, pr.getFundingDate()).append(vcFields.companyName,
                    pr.getCompanyName()).append(vcFields.fundingAmount, pr.getFundingAmount()).append(vcFields.fundingRound,
                    pr.getFundingRound()).append(vcFields.categories, pr.getCategories());

            fundingHistoryData.add(document);
        }

        return fundingHistoryData;
    }

    public static List<Document> documentVCFundRaisedData(VCProfile profile) {
        List<Document> fundRaisedData = new ArrayList<>();
        Document document = null;
        for (FundRaised pr : profile.getFundRaised()) {
            document = new Document().append(vcFields.fundRaisedDate, pr.getFundRaisedDate()).append(vcFields.fundRaisedName,
                    pr.getFundRaisedName()).append(vcFields.fundRaisedAmount, pr.getFundRaisedAmount()).append(vcFields.fundRaisedSourceName,
                    pr.getFundRaisedSourceName()).append(vcFields.fundRaisedSourceURL, pr.getFundRaisedSourceURL());

            fundRaisedData.add(document);
        }

        return fundRaisedData;
    }

    public static List<Document> documentVCIpoNAcquisitionsData(VCProfile profile) {
        List<Document> ipoNAcquisitions = new ArrayList<>();
        Document document = null;
        for (IpoNAcquisitions pr : profile.getIpoNAcquisitions()) {
            document = new Document().append(vcFields.ipoNAcquisitionsDate, pr.getIpoNAcquisitionsDate()).append(vcFields.ipoNAcquisitionsCompanyName,
                    pr.getIpoNAcquisitionsCompanyName()).append(vcFields.ipoNAcquisitionsExits, pr.getIpoNAcquisitionsExits());

            ipoNAcquisitions.add(document);
        }

        return ipoNAcquisitions;
    }

    public List<VCProfile> queryListOfCompanyByID(String vcID) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        List<VCProfile> vcList = new ArrayList<VCProfile>();
        vcList = readDataByVcID(vcID);
        return vcList;
    }

    public List<VCProfile> readDataByVcID(String vcID) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        try {
            connectMongo = new ReactiveConnectMongo();
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            mongoDatabase = mongoClient.getDatabase(databaseName);
            coll = mongoDatabase.getCollection("profile");
            basicDBObject = new BasicDBObject("_id", new ObjectId(vcID));
            iterable = coll.find(basicDBObject);
            /*iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId) document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    Document socialDataDocument = (Document) document.get("socialData");
                    List<Document> fundingHistoryDocument = (List<Document>) document.get("fundingHistory");
                    List<Document> fundRaisedDocument = (List<Document>) document.get("fundRaised");
                    List<Document> ipoNAcquisitionsDocument = (List<Document>) document.get("ipoNAcquisitions");
                    String vcID = idDocument.toString();
                    String vcName = (String) vcInfoDocument.get("vcName");
                    String vcType = (String) vcInfoDocument.get("vcType");
                    String vcLocationCity = (String) vcLocationDocument.get("city");
                    String vcLocationState = (String) vcLocationDocument.get("state");
                    String vcLocationCountry = (String) vcLocationDocument.get("country");
                    Location vcLocation = new Location(vcLocationCity, vcLocationState, vcLocationCountry);
                    String numberOfDeals = (String) vcInfoDocument.get("numberOfDeals");
                    String numberOfExits = (String) vcInfoDocument.get("numberOfExits");
                    String vcUrl = (String) vcInfoDocument.get("vcUrl");
                    String vcEmail = (String) vcInfoDocument.get("vcEmail");
                    String vcFoundedYear = (String) vcInfoDocument.get("vcFoundedYear");
                    String vcPhoneNumber = (String) vcInfoDocument.get("vcPhoneNumber");
                    vcInfo = new VCInfo(vcName, vcType, vcLocation, numberOfDeals, numberOfExits, vcUrl, vcEmail,
                            vcFoundedYear, vcPhoneNumber);
                    String facebookUrl = (String) socialDataDocument.get("facebookUrl");
                    String twitterUrl = (String) socialDataDocument.get("twitterUrl");
                    String linkedinUrl = (String) socialDataDocument.get("linkedinUrl");
                    socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
                    fundingHistoryList = new ArrayList<FundingHistory>();
                    fundingHistoryList = getFundingHistory(fundingHistoryDocument);
                    fundRaisedList = new ArrayList<FundRaised>();
                    fundRaisedList = getListOfFundRaised(fundRaisedDocument);
                    ipoNAcquisitionsList = new ArrayList<IpoNAcquisitions>();
                    ipoNAcquisitionsList = getIpoNAcquisitions(ipoNAcquisitionsDocument);
                    vcProfile = new VCProfile(vcID, vcInfo, socialData, fundingHistoryList, fundRaisedList, ipoNAcquisitionsList);
                    vcList.add(vcProfile);
                }

            }); */

            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (iterable != null) {
                iterable = null;
            }
            if (basicDBObject != null) {
                basicDBObject = null;
            }
            if (coll != null) {
                coll = null;
            }
            if (mongoDatabase != null) {
                mongoDatabase = null;

            }
            if (mongoClient != null) {
                mongoClient = null;
            }
            if (connectMongo != null) {
                connectMongo = null;
            }
        }
        return vcList;
    }

    public List<VCProfile> queryListOfCompany() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        List<VCProfile> vcList = new ArrayList<VCProfile>();
        /*vcList = getProfileListFromRedis();
        if(vcList.size() > 0){
           return vcList;
        }else{

       } */
        vcList = readAllVcData();
        return vcList;
    }

    public List<VCProfile> queryUnsortedListOfCompany() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        List<VCProfile> vcList = new ArrayList<VCProfile>();
        /*Jedis jedis = new Jedis("localhost");
        jedis.set("All", "");
        String all = jedis.get("All");
        */
        vcList = readUnsortedData();
        return vcList;
    }

    public List<VCProfile> queryListOfLandingPageVC() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        List<VCProfile> vcList = new ArrayList<VCProfile>();
        vcList = readDataForLandingPage();
        return vcList;
    }

    public List<VCProfile> readDataForLandingPage() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        try {
            connectMongo = new ReactiveConnectMongo();
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            mongoDatabase = mongoClient.getDatabase(databaseName);
            coll = mongoDatabase.getCollection("profile");
            iterable = coll.find();
            /*iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId) document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    String vcID = idDocument.toString();
                    String vcName = (String) vcInfoDocument.get("vcName");
                    String vcType = (String) vcInfoDocument.get("vcType");
                    String vcLocationCity = (String) vcLocationDocument.get("city");
                    String vcLocationState = (String) vcLocationDocument.get("state");
                    String vcLocationCountry = (String) vcLocationDocument.get("country");
                    Location vcLocation = new Location(vcLocationCity, vcLocationState, vcLocationCountry);
                    String numberOfDeals = (String) vcInfoDocument.get("numberOfDeals");
                    String numberOfExits = (String) vcInfoDocument.get("numberOfExits");
                    vcInfo = new VCInfo(vcName, vcType, vcLocation, numberOfDeals, numberOfExits);
                    vcProfile = new VCProfile(vcID, vcInfo);
                    vcList.add(vcProfile);
                }

            }); */

            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (iterable != null) {
                iterable = null;
            }
            if (coll != null) {
                coll = null;
            }
            if (mongoDatabase != null) {
                mongoDatabase = null;

            }
            if (mongoClient != null) {
                mongoClient = null;
            }
            if (connectMongo != null) {
                connectMongo = null;
            }
        }
        Collections.sort(vcList);
        return vcList;
    }

    public List<VCProfile> readAllVcData() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        try {
            /*SingleResultCallback<Document> printDocument = new SingleResultCallback<Document>() {
            @Override
            public void onResult(final Document document, final Throwable t) {
                System.out.println(document.toJson());
               }
            }; */
            connectMongo = new ReactiveConnectMongo();
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            mongoDatabase = mongoClient.getDatabase(databaseName);
            coll = mongoDatabase.getCollection("profile");
            iterable = coll.find();
            /*iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId) document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    Document socialDataDocument = (Document) document.get("socialData");
                    List<Document> fundingHistoryDocument = (List<Document>) document.get("fundingHistory");
                    List<Document> fundRaisedDocument = (List<Document>) document.get("fundRaised");
                    List<Document> ipoNAcquisitionsDocument = (List<Document>) document.get("ipoNAcquisitions");
                    String vcID = idDocument.toString();
                    String vcName = (String) vcInfoDocument.get("vcName");
                    String vcType = (String) vcInfoDocument.get("vcType");
                    String vcLocationCity = (String) vcLocationDocument.get("city");
                    String vcLocationState = (String) vcLocationDocument.get("state");
                    String vcLocationCountry = (String) vcLocationDocument.get("country");
                    Location vcLocation = new Location(vcLocationCity, vcLocationState, vcLocationCountry);
                    String numberOfDeals = (String) vcInfoDocument.get("numberOfDeals");
                    String numberOfExits = (String) vcInfoDocument.get("numberOfExits");
                    String vcUrl = (String) vcInfoDocument.get("vcUrl");
                    String vcEmail = (String) vcInfoDocument.get("vcEmail");
                    String vcFoundedYear = (String) vcInfoDocument.get("vcFoundedYear");
                    String vcPhoneNumber = (String) vcInfoDocument.get("vcPhoneNumber");
                    vcInfo = new VCInfo(vcName, vcType, vcLocation, numberOfDeals, numberOfExits, vcUrl, vcEmail,
                            vcFoundedYear, vcPhoneNumber);
                    String facebookUrl = (String) socialDataDocument.get("facebookUrl");
                    String twitterUrl = (String) socialDataDocument.get("twitterUrl");
                    String linkedinUrl = (String) socialDataDocument.get("linkedinUrl");
                    socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
                    fundingHistoryList = new ArrayList<FundingHistory>();
                    fundingHistoryList = getFundingHistory(fundingHistoryDocument);
                    fundRaisedList = new ArrayList<FundRaised>();
                    fundRaisedList = getListOfFundRaised(fundRaisedDocument);
                    ipoNAcquisitionsList = new ArrayList<IpoNAcquisitions>();
                    ipoNAcquisitionsList = getIpoNAcquisitions(ipoNAcquisitionsDocument);
                    vcProfile = new VCProfile(vcID, vcInfo, socialData, fundingHistoryList, fundRaisedList, ipoNAcquisitionsList);
                    vcList.add(vcProfile);
                }

            }); */

            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (iterable != null) {
                iterable = null;
            }
            if (coll != null) {
                coll = null;
            }
            if (mongoDatabase != null) {
                mongoDatabase = null;

            }
            if (mongoClient != null) {
                mongoClient = null;
            }
            if (connectMongo != null) {
                connectMongo = null;
            }
        }
        Collections.sort(vcList);
        return vcList;
    }

    public List<VCProfile> readUnsortedData() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        try {
            connectMongo = new ReactiveConnectMongo();
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            mongoDatabase = mongoClient.getDatabase(databaseName);
            coll = mongoDatabase.getCollection("profile");
            iterable = coll.find();
            /*iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId) document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    Document socialDataDocument = (Document) document.get("socialData");
                    List<Document> fundingHistoryDocument = (List<Document>) document.get("fundingHistory");
                    List<Document> fundRaisedDocument = (List<Document>) document.get("fundRaised");
                    List<Document> ipoNAcquisitionsDocument = (List<Document>) document.get("ipoNAcquisitions");
                    String vcID = idDocument.toString();
                    String vcName = (String) vcInfoDocument.get("vcName");
                    String vcType = (String) vcInfoDocument.get("vcType");
                    String vcLocationCity = (String) vcLocationDocument.get("city");
                    String vcLocationState = (String) vcLocationDocument.get("state");
                    String vcLocationCountry = (String) vcLocationDocument.get("country");
                    Location vcLocation = new Location(vcLocationCity, vcLocationState, vcLocationCountry);
                    String numberOfDeals = (String) vcInfoDocument.get("numberOfDeals");
                    String numberOfExits = (String) vcInfoDocument.get("numberOfExits");
                    String vcUrl = (String) vcInfoDocument.get("vcUrl");
                    String vcEmail = (String) vcInfoDocument.get("vcEmail");
                    String vcFoundedYear = (String) vcInfoDocument.get("vcFoundedYear");
                    String vcPhoneNumber = (String) vcInfoDocument.get("vcPhoneNumber");
                    vcInfo = new VCInfo(vcName, vcType, vcLocation, numberOfDeals, numberOfExits, vcUrl, vcEmail,
                            vcFoundedYear, vcPhoneNumber);
                    String facebookUrl = (String) socialDataDocument.get("facebookUrl");
                    String twitterUrl = (String) socialDataDocument.get("twitterUrl");
                    String linkedinUrl = (String) socialDataDocument.get("linkedinUrl");
                    socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
                    fundingHistoryList = new ArrayList<FundingHistory>();
                    fundingHistoryList = getFundingHistory(fundingHistoryDocument);
                    fundRaisedList = new ArrayList<FundRaised>();
                    fundRaisedList = getListOfFundRaised(fundRaisedDocument);
                    ipoNAcquisitionsList = new ArrayList<IpoNAcquisitions>();
                    ipoNAcquisitionsList = getIpoNAcquisitions(ipoNAcquisitionsDocument);
                    vcProfile = new VCProfile(vcID, vcInfo, socialData, fundingHistoryList, fundRaisedList, ipoNAcquisitionsList);
                    vcList.add(vcProfile);
                }

            }); */

            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (iterable != null) {
                iterable = null;
            }
            if (coll != null) {
                coll = null;
            }
            if (mongoDatabase != null) {
                mongoDatabase = null;

            }
            if (mongoClient != null) {
                mongoClient = null;
            }
            if (connectMongo != null) {
                connectMongo = null;
            }
        }
        return vcList;
    }

    public List<FundingHistory> getFundingHistory(List<Document> fundingHistoryDocument) {
        if (fundingHistoryDocument != null) {
            for (int i = 0; i < fundingHistoryDocument.size(); i++) {
                String fundingDate = (String) fundingHistoryDocument.get(i).get("fundingDate");
                String companyName = (String) fundingHistoryDocument.get(i).get("companyName");
                String fundingAmount = (String) fundingHistoryDocument.get(i).get("fundingAmount");
                String fundingRound = (String) fundingHistoryDocument.get(i).get("fundingRound");
                List<String> categoriesDocumentList = (List<String>) fundingHistoryDocument.get(i).get("categories");
                List<String> categoriesList = new ArrayList<String>();
                if (categoriesDocumentList != null) {
                    for (int j = 0; j < categoriesDocumentList.size(); j++) {
                        String category = categoriesDocumentList.get(j);
                        categoriesList.add(category);
                    }
                }
                fundingHistory = new FundingHistory(fundingDate, companyName, fundingAmount, fundingRound, categoriesList);
                fundingHistoryList.add(fundingHistory);
            }
            return fundingHistoryList;

        } else {
            return fundingHistoryList;
        }

    }


    public List<FundRaised> getListOfFundRaised(List<Document> fundRaisedDocument) {
        List<FundRaised> fundRaisedList = new ArrayList<FundRaised>();
        if (fundRaisedDocument != null) {
            for (int i = 0; i < fundRaisedDocument.size(); i++) {
                String fundRaisedDate = (String) fundRaisedDocument.get(i).get("fundRaisedDate");
                String fundRaisedName = (String) fundRaisedDocument.get(i).get("fundRaisedName");
                String fundRaisedAmount = (String) fundRaisedDocument.get(i).get("fundRaisedAmount");
                String fundRaisedSourceName = (String) fundRaisedDocument.get(i).get("fundRaisedSourceName");
                String fundRaisedSourceURL = (String) fundRaisedDocument.get(i).get("fundRaisedSourceURL");
                fundRaised = new FundRaised(fundRaisedDate, fundRaisedName, fundRaisedAmount, fundRaisedSourceName, fundRaisedSourceURL);
                fundRaisedList.add(fundRaised);
            }
            return fundRaisedList;
        } else {
            return fundRaisedList;
        }
    }

    public List<IpoNAcquisitions> getIpoNAcquisitions(List<Document> ipoNAcquisitionsDocument) {
        List<IpoNAcquisitions> ipoNAcquisitionsList = new ArrayList<IpoNAcquisitions>();
        if (ipoNAcquisitionsDocument != null) {
            for (int i = 0; i < ipoNAcquisitionsDocument.size(); i++) {
                String ipoNAcquisitionsDate = (String) ipoNAcquisitionsDocument.get(i).get("ipoNAcquisitionsDate");
                String ipoNAcquisitionsCompanyName = (String) ipoNAcquisitionsDocument.get(i).get("ipoNAcquisitionsCompanyName");
                String ipoNAcquisitionsExits = (String) ipoNAcquisitionsDocument.get(i).get("ipoNAcquisitionsExits");
                ipoNAcquisitions = new IpoNAcquisitions(ipoNAcquisitionsDate, ipoNAcquisitionsCompanyName, ipoNAcquisitionsExits);
                ipoNAcquisitionsList.add(ipoNAcquisitions);
            }
            return ipoNAcquisitionsList;
        } else {
            return ipoNAcquisitionsList;
        }
    }

    public List<VCProfile> readDataFundingHistoryConnectWithNumberOfDeals() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        try {
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
            MongoCollection<Document> coll = mongoDatabase.getCollection("profile");
            iterable = coll.find().limit(200);
            /*iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId) document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    Document socialDataDocument = (Document) document.get("socialData");
                    List<Document> fundingHistoryDocument = (List<Document>) document.get("fundingHistory");

                    String vcID = idDocument.toString();
                    String vcName = (String) vcInfoDocument.get("vcName");
                    String vcType = (String) vcInfoDocument.get("vcType");
                    String vcLocationCity = (String) vcLocationDocument.get("city");
                    String vcLocationState = (String) vcLocationDocument.get("state");
                    String vcLocationCountry = (String) vcLocationDocument.get("country");
                    Location vcLocation = new Location(vcLocationCity, vcLocationState, vcLocationCountry);
//                    String numberOfDeals = (String)vcInfoDocument.get("numberOfDeals");
                    String numberOfExits = (String) vcInfoDocument.get("numberOfExits");
                    String vcUrl = (String) vcInfoDocument.get("vcUrl");
                    String vcEmail = (String) vcInfoDocument.get("vcEmail");
                    String vcFoundedYear = (String) vcInfoDocument.get("vcFoundedYear");
                    String vcPhoneNumber = (String) vcInfoDocument.get("vcPhoneNumber");
                    String facebookUrl = (String) socialDataDocument.get("facebookUrl");
                    String twitterUrl = (String) socialDataDocument.get("twitterUrl");
                    String linkedinUrl = (String) socialDataDocument.get("linkedinUrl");

                    socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
                    fundingHistoryList = new ArrayList<FundingHistory>();
                    if (fundingHistoryDocument != null) {
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
                        vcInfo = new VCInfo(vcName, vcType, vcLocation, numberOfDeals, numberOfExits, vcUrl, vcEmail,
                                vcFoundedYear, vcPhoneNumber);
                        vcProfile = new VCProfile(vcID, vcInfo, socialData, fundingHistoryList);
                        vcList.add(vcProfile);

                    } else {
                        vcProfile = new VCProfile(vcID, vcInfo, socialData);
                        vcList.add(vcProfile);
                    }
                }

            }); */

            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            if (mongoClient != null) {

                mongoClient = null;
            }
        }
        return vcList;
    }

    public List<VCProfile> queryListOfCompanyByPagination(int start, int size) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        try {
            connectMongo = new ReactiveConnectMongo();
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            MongoDatabase mongoDatabase = mongoClient.getDatabase("PROD_VC_PROFILE");
            MongoCollection<Document> coll = mongoDatabase.getCollection("profile");
            iterable = coll.find();
            /*iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId) document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    Document socialDataDocument = (Document) document.get("socialData");
                    List<Document> fundingHistoryDocument = (List<Document>) document.get("fundingHistory");
                    String vcID = idDocument.toString();
                    String vcName = (String) vcInfoDocument.get("vcName");
                    String vcType = (String) vcInfoDocument.get("vcType");
                    String vcLocationCity = (String) vcLocationDocument.get("city");
                    String vcLocationState = (String) vcLocationDocument.get("state");
                    String vcLocationCountry = (String) vcLocationDocument.get("country");
                    Location vcLocation = new Location(vcLocationCity, vcLocationState, vcLocationCountry);
                    String numberOfDeals = (String) vcInfoDocument.get("numberOfDeals");
                    String numberOfExits = (String) vcInfoDocument.get("numberOfExits");
                    String vcUrl = (String) vcInfoDocument.get("vcUrl");
                    String vcEmail = (String) vcInfoDocument.get("vcEmail");
                    String vcFoundedYear = (String) vcInfoDocument.get("vcFoundedYear");
                    String vcPhoneNumber = (String) vcInfoDocument.get("vcPhoneNumber");
                    vcInfo = new VCInfo(vcName, vcType, vcLocation, numberOfDeals, numberOfExits, vcUrl, vcEmail,
                            vcFoundedYear, vcPhoneNumber);
                    String facebookUrl = (String) socialDataDocument.get("facebookUrl");
                    String twitterUrl = (String) socialDataDocument.get("twitterUrl");
                    String linkedinUrl = (String) socialDataDocument.get("linkedinUrl");

                    socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
                    fundingHistoryList = new ArrayList<FundingHistory>();
                    if (fundingHistoryDocument != null) {
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

                    } else {
                        vcProfile = new VCProfile(vcID, vcInfo, socialData);
                        vcList.add(vcProfile);
                    }
                }

            }); */

            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            if (mongoClient != null) {

                mongoClient = null;
                connectMongo = null;
            }
        }
        if (start + size > vcList.size()) return vcList;
        return vcList.subList(start, start + size);
    }

    public List<VCProfile> queryListOfCompanyByName(String vcName) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        List<VCProfile> vcList = new ArrayList<VCProfile>();
        try {
            connectMongo = new ReactiveConnectMongo();
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            mongoDatabase = mongoClient.getDatabase(databaseName);
            coll = mongoDatabase.getCollection("profile");
            basicDBObject = new BasicDBObject();
            basicDBObject.put("vcInfo.vcName", vcName);
            iterable = coll.find(basicDBObject);
            /*iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId) document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    Document socialDataDocument = (Document) document.get("socialData");
                    List<Document> fundingHistoryDocument = (List<Document>) document.get("fundingHistory");

                    String vcID = idDocument.toString();
                    String vcName = (String) vcInfoDocument.get("vcName");
                    String vcType = (String) vcInfoDocument.get("vcType");
                    String vcLocationCity = (String) vcLocationDocument.get("city");
                    String vcLocationState = (String) vcLocationDocument.get("state");
                    String vcLocationCountry = (String) vcLocationDocument.get("country");
                    Location vcLocation = new Location(vcLocationCity, vcLocationState, vcLocationCountry);
                    String numberOfDeals = (String) vcInfoDocument.get("numberOfDeals");
                    String numberOfExits = (String) vcInfoDocument.get("numberOfExits");
                    String vcUrl = (String) vcInfoDocument.get("vcUrl");
                    String vcEmail = (String) vcInfoDocument.get("vcEmail");
                    String vcFoundedYear = (String) vcInfoDocument.get("vcFoundedYear");
                    String vcPhoneNumber = (String) vcInfoDocument.get("vcPhoneNumber");
                    vcInfo = new VCInfo(vcName, vcType, vcLocation, numberOfDeals, numberOfExits, vcUrl, vcEmail,
                            vcFoundedYear, vcPhoneNumber);
                    String facebookUrl = (String) socialDataDocument.get("facebookUrl");
                    String twitterUrl = (String) socialDataDocument.get("twitterUrl");
                    String linkedinUrl = (String) socialDataDocument.get("linkedinUrl");

                    socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
                    fundingHistoryList = new ArrayList<FundingHistory>();
                    if (fundingHistoryDocument != null) {
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

                    } else {
                        vcProfile = new VCProfile(vcID, vcInfo, socialData);
                        vcList.add(vcProfile);
                    }
                }

            }); */

            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (iterable != null) {
                iterable = null;
            }
            if (basicDBObject != null) {
                basicDBObject = null;
            }
            if (coll != null) {
                coll = null;
            }
            if (mongoDatabase != null) {
                mongoDatabase = null;

            }
            if (mongoClient != null) {
                mongoClient = null;
            }
            if (connectMongo != null) {
                connectMongo = null;
            }
        }
        return vcList;
    }

    public List<VCProfile> readDataByVcName(String vcName) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        try {
            connectMongo = new ReactiveConnectMongo();
            mongoClient = connectMongo.connectToRecommendedSSLAtlasMongoClient();
            mongoDatabase = mongoClient.getDatabase(databaseName);
            coll = mongoDatabase.getCollection("profile");
            basicDBObject = new BasicDBObject("vcInfo.vcName", vcName);
            iterable = coll.find(basicDBObject);
            /*iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    ObjectId idDocument = (ObjectId) document.get("_id");
                    Document vcInfoDocument = (Document) document.get("vcInfo");
                    Document vcLocationDocument = (Document) vcInfoDocument.get("vcLocation");
                    Document socialDataDocument = (Document) document.get("socialData");
                    List<Document> fundingHistoryDocument = (List<Document>) document.get("fundingHistory");
                    List<Document> fundRaisedDocument = (List<Document>) document.get("fundRaised");
                    List<Document> ipoNAcquisitionsDocument = (List<Document>) document.get("ipoNAcquisitions");
                    String vcID = idDocument.toString();
                    String vcName = (String) vcInfoDocument.get("vcName");
                    String vcType = (String) vcInfoDocument.get("vcType");
                    String vcLocationCity = (String) vcLocationDocument.get("city");
                    String vcLocationState = (String) vcLocationDocument.get("state");
                    String vcLocationCountry = (String) vcLocationDocument.get("country");
                    Location vcLocation = new Location(vcLocationCity, vcLocationState, vcLocationCountry);
                    String numberOfDeals = (String) vcInfoDocument.get("numberOfDeals");
                    String numberOfExits = (String) vcInfoDocument.get("numberOfExits");
                    String vcUrl = (String) vcInfoDocument.get("vcUrl");
                    String vcEmail = (String) vcInfoDocument.get("vcEmail");
                    String vcFoundedYear = (String) vcInfoDocument.get("vcFoundedYear");
                    String vcPhoneNumber = (String) vcInfoDocument.get("vcPhoneNumber");
                    vcInfo = new VCInfo(vcName, vcType, vcLocation, numberOfDeals, numberOfExits, vcUrl, vcEmail,
                            vcFoundedYear, vcPhoneNumber);
                    String facebookUrl = (String) socialDataDocument.get("facebookUrl");
                    String twitterUrl = (String) socialDataDocument.get("twitterUrl");
                    String linkedinUrl = (String) socialDataDocument.get("linkedinUrl");
                    socialData = new SocialData(facebookUrl, twitterUrl, linkedinUrl);
                    fundingHistoryList = new ArrayList<FundingHistory>();
                    fundingHistoryList = getFundingHistory(fundingHistoryDocument);
                    fundRaisedList = new ArrayList<FundRaised>();
                    fundRaisedList = getListOfFundRaised(fundRaisedDocument);
                    ipoNAcquisitionsList = new ArrayList<IpoNAcquisitions>();
                    ipoNAcquisitionsList = getIpoNAcquisitions(ipoNAcquisitionsDocument);
                    vcProfile = new VCProfile(vcID, vcInfo, socialData, fundingHistoryList, fundRaisedList, ipoNAcquisitionsList);
                    vcList.add(vcProfile);
                }

            }); */

            mongoClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (iterable != null) {
                iterable = null;
            }
            if (basicDBObject != null) {
                basicDBObject = null;
            }
            if (coll != null) {
                coll = null;
            }
            if (mongoDatabase != null) {
                mongoDatabase = null;

            }
            if (mongoClient != null) {
                mongoClient = null;
            }
            if (connectMongo != null) {
                connectMongo = null;
            }
        }
        return vcList;
    }

    public List<VCProfile> getProfileListFromRedis() {
        JedisMain main = new JedisMain();
        Object profileList = main.getObjectValue("vcList");
        List<VCProfile> profileData = (List<VCProfile>) profileList;

        return profileData;
    }

    public Source<ByteString, NotUsed> getData(MongoCollection<Document> mongoClient){
        Source<ByteString,NotUsed> list = null;
        /*ActorSystem system = ActorSystem.create("System");
        Materializer mat = ActorMaterializer.create(system);
        Sink<String, Flow.Publisher<String>> asFlowPublisher = JavaFlowSupport.Sink.asPublisher(AsPublisher.WITH_FANOUT);
        Source<ByteString,NotUsed> list = JavaFlowSupport.Source.fromPublisher(toPublisher().map(s->toString()).map(ByteString.)).
                    intersperse(ByteString("["),ByteString(","),ByteString("]"));*/
        return list;
    }
    /*
    private Observable<VCProfile> _findOrdersByUsername(final String username) {
        return coll.find().toObservable().map(doc -> new VCProfile());
    }*/
    public FindPublisher<Document> findAll(MongoCollection<Document> coll){
        FindPublisher<Document> it = coll.find();
        return it;
    }
    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        /*
        String st = "Test_Moore&";
        if(st.contains("_")||st.contains("&")){
            String newSt = st.replace("_", "-").replace("&", "-");
                    System.out.println(newSt);
        } */

    }
}
