package org.vp.vc.profile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by mrahman on 7/26/16.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VCProfile {

    public String _id;
    public VCInfo vcInfo;
    public SocialData socialData;
    public List<FundingHistory> fundingHistory;

    public VCProfile(){

    }

    public VCProfile(VCInfo vcInfo, SocialData socialData) {
        this.vcInfo = vcInfo;
        this.socialData = socialData;
    }

    public VCProfile(String id,VCInfo vcInfo, SocialData socialData) {
        this._id = id;
        this.vcInfo = vcInfo;
        this.socialData = socialData;
    }

    public VCProfile(VCInfo vcInfo, SocialData socialData, List<FundingHistory> fundingHistory) {
        this.vcInfo = vcInfo;
        this.socialData = socialData;
        this.fundingHistory = fundingHistory;
    }

    public VCProfile(String id,VCInfo vcInfo, SocialData socialData, List<FundingHistory> fundingHistory) {
        this._id = id;
        this.vcInfo = vcInfo;
        this.socialData = socialData;
        this.fundingHistory = fundingHistory;
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




}
