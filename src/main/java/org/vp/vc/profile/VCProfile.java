package org.vp.vc.profile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.*;

/**
 * Created by mrahman on 7/26/16.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VCProfile implements Comparable<VCProfile>, Serializable {

    public String _id;
    public VCInfo vcInfo;
    public SocialData socialData;
    public List<FundingHistory> fundingHistory;
    public List<FundRaised> fundRaised;
    public List<IpoNAcquisitions> ipoNAcquisitions;

    public VCProfile(){

    }

    public VCProfile(String _id,VCInfo vcInfo){
        this._id = _id;
        this.vcInfo = vcInfo;
    }
    public VCProfile(VCInfo vcInfo, SocialData socialData) {
        this.vcInfo = vcInfo;
        this.socialData = socialData;
    }

    public VCProfile(String _id,VCInfo vcInfo, SocialData socialData) {
        this._id = _id;
        this.vcInfo = vcInfo;
        this.socialData = socialData;
    }

    public VCProfile(VCInfo vcInfo, SocialData socialData, List<FundingHistory> fundingHistory) {
        this.vcInfo = vcInfo;
        this.socialData = socialData;
        this.fundingHistory = fundingHistory;
    }
    public VCProfile(List<FundingHistory> fundingHistory) {
        this.fundingHistory = fundingHistory;
    }

    public VCProfile(String _id,VCInfo vcInfo, SocialData socialData, List<FundingHistory> fundingHistory) {
        this._id = _id;
        this.vcInfo = vcInfo;
        this.socialData = socialData;
        this.fundingHistory = fundingHistory;
    }
    public VCProfile(VCInfo vcInfo, SocialData socialData, List<FundingHistory> fundingHistory, List<FundRaised> fundRaised, List<IpoNAcquisitions> ipoNAcquisitions) {
        this.vcInfo = vcInfo;
        this.socialData = socialData;
        this.fundingHistory = fundingHistory;
        this.fundRaised = fundRaised;
        this.ipoNAcquisitions = ipoNAcquisitions;
    }
    public VCProfile(String _id, VCInfo vcInfo, SocialData socialData, List<FundingHistory> fundingHistory, List<FundRaised> fundRaised, List<IpoNAcquisitions> ipoNAcquisitions) {
        this._id = _id;
        this.vcInfo = vcInfo;
        this.socialData = socialData;
        this.fundingHistory = fundingHistory;
        this.fundRaised = fundRaised;
        this.ipoNAcquisitions = ipoNAcquisitions;
    }

    public int compareTo(VCProfile vcProfile){
        int numberOfDeals = 0;
        if(vcProfile.getVcInfo().getNumberOfDeals().equals("")){
             numberOfDeals = 0;
        }else {
             numberOfDeals = Integer.parseInt(((VCProfile) vcProfile).getVcInfo().getNumberOfDeals().replace(",",""));
        }

        int updateNumberOfDeals = 0;
        if(this.vcInfo.numberOfDeals.equals("")){
            updateNumberOfDeals = 0;
        }else{
            updateNumberOfDeals = Integer.parseInt(this.vcInfo.numberOfDeals.replace(",",""));
        }
        return numberOfDeals - updateNumberOfDeals;
    }
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public VCInfo getVcInfo() {
        return vcInfo;
    }

    public void setVcInfo(VCInfo vcInfo) {
        this.vcInfo = vcInfo;
    }

    public SocialData getSocialData() {
        return socialData;
    }

    public void setSocialData(SocialData socialData) {
        this.socialData = socialData;
    }

    public List<FundingHistory> getFundingHistory() {
        return fundingHistory;
    }

    public void setFundingHistory(List<FundingHistory> fundingHistory) {
        this.fundingHistory = fundingHistory;
    }

    public List<FundRaised> getFundRaised() {
        return fundRaised;
    }

    public void setFundRaised(List<FundRaised> fundRaised) {
        this.fundRaised = fundRaised;
    }

    public List<IpoNAcquisitions> getIpoNAcquisitions() {
        return ipoNAcquisitions;
    }

    public void setIpoNAcquisitions(List<IpoNAcquisitions> ipoNAcquisitions) {
        this.ipoNAcquisitions = ipoNAcquisitions;
    }

}
