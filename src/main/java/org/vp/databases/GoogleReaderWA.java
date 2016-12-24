//package org.vp.databases;
//
//import com.google.gdata.client.spreadsheet.SpreadsheetService;
//import com.google.gdata.data.spreadsheet.CustomElementCollection;
//import com.google.gdata.data.spreadsheet.ListEntry;
//import com.google.gdata.data.spreadsheet.ListFeed;
//import com.google.gdata.util.ServiceException;
//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import org.bson.Document;
//import org.vp.vc.profile.*;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by mrahman on 12/16/16.
// */
//public class GoogleReaderWA {
//    public static VCFields vcFields = new VCFields();
//    public static void main(String[] args) {
//        ConnectMongo connectMongo = new ConnectMongo();
//        VCProfile vcProfile = null;
//        VCInfo vcInfo = new VCInfo();
//        SocialData socialData = new SocialData();
//        FundingHistory fundingHistory = new FundingHistory();
//        List<FundingHistory> fundingHistoryList = new ArrayList<FundingHistory>();
//        Location location = new Location();
//        List<VCProfile> vcProfileList = new ArrayList<VCProfile>();
//        SpreadsheetService service = new SpreadsheetService("Corporate VC");
//        // https://spreadsheets/d/1y_W3qd0YaFw6HdI0udBa-kU4Lrd2WgTIKZlvE3dBsPU/pubhtml
//        try {
//            String sheetUrl = "https://spreadsheets.google.com/feeds/list/1y_W3qd0YaFw6HdI0udBa-kU4Lrd2WgTIKZlvE3dBsPU/default/public/values";
//
//            // Use this String as url
//            URL url = new URL(sheetUrl);
//
//            // Get Feed of Spreadsheet url
//            ListFeed lf = service.getFeed(url, ListFeed.class);
//
//            //Iterate over feed to get cell value
//            for (ListEntry le : lf.getEntries()) {
//                CustomElementCollection cec = le.getCustomElements();
//                //Pass column name to access it's cell values
//                String vcName = cec.getValue("vcName");
//                String vcType = cec.getValue("vcType");
//                String vcLocation = cec.getValue("vcLocation");
//                String [] loc = vcLocation.split(",");
//                location.setCity(loc[0]);
//                location.setState(loc[1]);
//                location.setState(loc[2]);
//
//                String numberOfDeals = cec.getValue("numberOfDeals");
//                String exits = cec.getValue("exits");
//                String vcUrl = cec.getValue("vcUrl");
//                String vcEmail = cec.getValue("vcEmail");
//                String vcFoundedYear = cec.getValue("vcFoundedYear");
//                String facebookUrl = cec.getValue("facebookUrl");
//                String twitterUrl = cec.getValue("twitterUrl");
//                String linkedinUrl = cec.getValue("linkedinUrl");
//                String fundingDate = cec.getValue("fundingDate");
//                String companyName = cec.getValue("companyName");
//                String fundingAmount = cec.getValue("fundingAmount");
//                String fundingRound = cec.getValue("fundingRound");
//                String categories = cec.getValue("categories");
//                fundingHistory.setFundingDate(fundingDate);
//                fundingHistory.setCompanyName(companyName);
//                fundingHistory.setFundingAmount(fundingAmount);
//                fundingHistory.setFundingRound(fundingRound);
//                fundingHistory.setCategories(categories);
//                fundingHistory = new FundingHistory(fundingDate, companyName, fundingAmount, fundingRound, categories);
//                fundingHistoryList.add(fundingHistory);
//
//                vcInfo.setVcName(vcName);
//                vcInfo.setVcType(vcType);
//                vcInfo.setVcLocation(location);
//                vcInfo.setNumberOfDeals(numberOfDeals);
//                vcInfo.setVcUrl(vcUrl);
//                vcInfo.setVcEmail(vcEmail);
//                vcInfo.setVcFoundedYear(vcFoundedYear);
//                socialData.setFacebookUrl(facebookUrl);
//                socialData.setTwitterUrl(twitterUrl);
//                socialData.setLinkedinUrl(linkedinUrl);
//                vcProfile.setVcInfo(vcInfo);
//                vcProfile.setSocialData(socialData);
//                vcProfile = new VCProfile(vcInfo,socialData,fundingHistoryList);
//                vcProfileList.add(vcProfile);
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
//        MongoClient mongoClient = connectMongo.connectMongoClientSSLToAtlasWithMinimalSecurity();
//        MongoDatabase mongoDatabase = mongoClient.getDatabase("devVcProfile");
//        MongoCollection mongoCollection = mongoDatabase.getCollection("profile");
//        Document vcInfoDocument = documentVCInfoDataDelta(vcInfo);
//        Document socialDataDocument = documentVCSocialData(socialData);
//        List<Document> fundingHistoryDocument = documentVCFundingHistoryData(fundingHistory);
//
//        Document preparedDocument = new Document("vcInfo", vcInfoDocument).append("socialData", socialDataDocument)
//                .append("fundingHistory", fundingHistoryDocument);
//        mongoCollection.insertMany(vcProfileList);
//
//
//    }
//
//    public static Document documentVCInfoDataDelta(VCInfo profile){
//        String vcLocation = "vcLocation";
//        Document document = new Document().append(vcFields.vcName, profile.getVcName())
//                .append(vcFields.vcType, profile.getVcType()).append(vcLocation,vcLocationDocument())
//                .append(vcFields.numberOfDeals, profile.getNumberOfDeals()).append(vcFields.vcUrl,
//                        profile.getVcUrl()).append(vcFields.vcEmail,profile.getVcEmail())
//                .append(vcFields.vcFoundedYear, profile.getVcFoundedYear());
//
//        return document;
//    }
//
//    public static Document vcLocationDocument( profile){
//        Document document = new Document().append(vcFields.city, profile.getVcInfo()
//                .getVcLocation().getCity()).append(vcFields.state, profile.getVcInfo().getVcLocation().getState());
//
//        return document;
//    }
//
//    public static Document documentVCInfoData(VCProfile profile){
//        Document document = new Document().append(vcFields.vcName, profile.getVcInfo().getVcName())
//                .append(vcFields.vcType, profile.getVcInfo().getVcType()).append(vcFields.city, profile.getVcInfo()
//                        .getVcLocation().getCity()).append(vcFields.state, profile.getVcInfo().getVcLocation().getState())
//                .append(vcFields.numberOfDeals, profile.getVcInfo().getNumberOfDeals()).append(vcFields.vcUrl,
//                        profile.getVcInfo().getVcUrl()).append(vcFields.vcEmail,profile.getVcInfo().getVcEmail())
//                .append(vcFields.vcFoundedYear, profile.getVcInfo().getVcFoundedYear());
//
//        return document;
//    }
//    public static Document documentVCSocialData(SocialData profile){
//        Document document = new Document().append(vcFields.facebookUrl, profile.getFacebookUrl())
//                .append(vcFields.twitterUrl, profile.getTwitterUrl()).append(vcFields.linkedinUrl,
//                        profile.getLinkedinUrl());
//
//        return document;
//    }
//
//    public static List<Document> documentVCFundingHistoryData(FundingHistory profile){
//        List<Document> fundingHistoryData = new ArrayList<>();
//        Document document = null;
//        for(FundingHistory pr:profile.getFundingHistory()) {
//            document = new Document().append(vcFields.fundingDate, pr.getFundingDate()).append(vcFields.companyName,
//                    pr.getCompanyName()).append(vcFields.fundingAmount,pr.getFundingAmount()).append(vcFields.fundingRound,
//                    pr.getFundingRound()).append(vcFields.categories, pr.getCategories());
//
//            fundingHistoryData.add(document);
//        }
//
//        return fundingHistoryData;
//    }
//}
