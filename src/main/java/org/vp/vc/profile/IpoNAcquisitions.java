package org.vp.vc.profile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by mrahman on 4/19/17.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IpoNAcquisitions implements Serializable {

    public String ipoNAcquisitionsDate;
    public String ipoNAcquisitionsCompanyName;
    public String ipoNAcquisitionsExits;

    public IpoNAcquisitions(){}

    public IpoNAcquisitions(String ipoNAcquisitionsDate, String ipoNAcquisitionsCompanyName, String ipoNAcquisitionsExits) {
        this.ipoNAcquisitionsDate = ipoNAcquisitionsDate;
        this.ipoNAcquisitionsCompanyName = ipoNAcquisitionsCompanyName;
        this.ipoNAcquisitionsExits = ipoNAcquisitionsExits;
    }

    public String getIpoNAcquisitionsDate() {
        return ipoNAcquisitionsDate;
    }

    public void setIpoNAcquisitionsDate(String ipoNAcquisitionsDate) {
        this.ipoNAcquisitionsDate = ipoNAcquisitionsDate;
    }

    public String getIpoNAcquisitionsCompanyName() {
        return ipoNAcquisitionsCompanyName;
    }

    public void setIpoNAcquisitionsCompanyName(String ipoNAcquisitionsCompanyName) {
        this.ipoNAcquisitionsCompanyName = ipoNAcquisitionsCompanyName;
    }

    public String getIpoNAcquisitionsExits() {
        return ipoNAcquisitionsExits;
    }

    public void setIpoNAcquisitionsExits(String ipoNAcquisitionsExits) {
        this.ipoNAcquisitionsExits = ipoNAcquisitionsExits;
    }
}
