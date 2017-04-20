package org.vp.vc.profile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by mrahman on 4/19/17.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FundRaised {

    public String fundRaisedDate;
    public String fundRaisedName;
    public String fundRaisedAmount;
    public String fundRaisedSourceName;
    public String fundRaisedSourceURL;

    public FundRaised(){}

    public FundRaised(String fundRaisedDate, String fundRaisedName, String fundRaisedAmount, String fundRaisedSourceName, String fundRaisedSourceURL) {
        this.fundRaisedDate = fundRaisedDate;
        this.fundRaisedName = fundRaisedName;
        this.fundRaisedAmount = fundRaisedAmount;
        this.fundRaisedSourceName = fundRaisedSourceName;
        this.fundRaisedSourceURL = fundRaisedSourceURL;
    }


    public String getFundRaisedDate() {
        return fundRaisedDate;
    }

    public void setFundRaisedDate(String fundRaisedDate) {
        this.fundRaisedDate = fundRaisedDate;
    }

    public String getFundRaisedName() {
        return fundRaisedName;
    }

    public void setFundRaisedName(String fundRaisedName) {
        this.fundRaisedName = fundRaisedName;
    }

    public String getFundRaisedAmount() {
        return fundRaisedAmount;
    }

    public void setFundRaisedAmount(String fundRaisedAmount) {
        this.fundRaisedAmount = fundRaisedAmount;
    }

    public String getFundRaisedSourceName() {
        return fundRaisedSourceName;
    }

    public void setFundRaisedSourceName(String fundRaisedSourceName) {
        this.fundRaisedSourceName = fundRaisedSourceName;
    }

    public String getFundRaisedSourceURL() {
        return fundRaisedSourceURL;
    }

    public void setFundRaisedSourceURL(String fundRaisedSourceURL) {
        this.fundRaisedSourceURL = fundRaisedSourceURL;
    }
}
