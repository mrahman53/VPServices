package org.vp.vc.profile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by mrahman on 7/17/16.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FundingHistory {

    public String fundingDate;
    public String companyName;
    public String fundingAmount;
    public String fundingRound;
    public String categories;

    public FundingHistory(){

    }

    public FundingHistory(String fundingDate, String companyName, String fundingAmount, String fundingRound, String categories) {
        this.fundingDate = fundingDate;
        this.companyName = companyName;
        this.fundingAmount = fundingAmount;
        this.fundingRound = fundingRound;
        this.categories = categories;
    }

    public String getFundingDate() {
        return fundingDate;
    }

    public void setFundingDate(String fundingDate) {
        this.fundingDate = fundingDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFundingAmount() {
        return fundingAmount;
    }

    public void setFundingAmount(String fundingAmount) {
        this.fundingAmount = fundingAmount;
    }

    public String getFundingRound() {
        return fundingRound;
    }

    public void setFundingRound(String fundingRound) {
        this.fundingRound = fundingRound;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }


}
