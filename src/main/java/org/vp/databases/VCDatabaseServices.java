package org.vp.databases;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.vp.vc.profile.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrahman on 7/17/16.
 */
public class VCDatabaseServices {
    public ConnectDB connectDB = new ConnectDB();
    public MongoDatabase mongoDatabase = null;
    public static VCFields vcFields = new VCFields();
    public static VCInfo vcInfo = null;
    public static SocialData socialData = null;
    public static FundingHistory fundingHistory = null;
    public List<FundingHistory> fundingHistoryList = new ArrayList<FundingHistory>();
    public VCProfile vcProfile = null;

    public boolean insertVCProfileNReturn(VCProfile profile){
        MongoDatabase mongoDatabase1 = null;
        ConnectDB connectDB1 = new ConnectDB();
        String st = profile.getVcInfo().getVcName()+" "+ "is Inserted";
        mongoDatabase1 = ConnectDB.connectAtlasMongoClientDB();
        MongoCollection mongoCollection = mongoDatabase1.getCollection("profile");
        Document vcInfoDocument = documentVCInfoDataDelta(profile);
        Document socialDataDocument = documentVCSocialData(profile);
        List<Document> fundingHistoryDocument = documentVCFundingHistoryData(profile);

        Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument)
                .append("fundingHistory", fundingHistoryDocument);

        mongoCollection.insertOne(preparedDocument);

        return true;
    }

    public String insertVCProfile(VCProfile profile){
        MongoDatabase mongoDatabase1 = null;
        ConnectDB connectDB1 = new ConnectDB();
        String st = profile.getVcInfo().getVcName()+" "+ "is Inserted";
        mongoDatabase1 = connectDB1.connectLocalMongoDBClient();
        MongoCollection mongoCollection = mongoDatabase1.getCollection("vc");
        Document vcInfoDocument = documentVCInfoDataDelta(profile);
        Document socialDataDocument = documentVCSocialData(profile);
        List<Document> fundingHistoryDocument = documentVCFundingHistoryData(profile);

        Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument)
                .append("fundingHistory", fundingHistoryDocument);

        mongoCollection.insertOne(preparedDocument);

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

    public VCProfile findOneVCProfile(String vcId){
        Map<Integer, Document> sData = new LinkedHashMap<>();
        Map<Integer, Object> pData = new LinkedHashMap<>();
        Map<Integer, Object> data = new LinkedHashMap<>();
        mongoDatabase = connectDB.connectLocalMongoDBClient();
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

        return vcProfile;
    }

    public List<VCProfile> queryListOfCompany(){
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        mongoDatabase = connectDB.connectWithSSLToAtlas();
        MongoCollection<Document> coll = mongoDatabase.getCollection("profile");
        BasicDBObject basicDBObject = new BasicDBObject();
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


        return vcList;
    }

    public List<VCProfile> queryListOfCompany(String vcId){
        final List<VCProfile> vcList = new ArrayList<VCProfile>();
        mongoDatabase = connectDB.connectWithSSLToAtlas();
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


        return vcList;
    }
}
