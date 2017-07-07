package org.vp.vc.profile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by mrahman on 4/19/17.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IpoNAcquisitions {

    public String ipoNAcquisitionsDate;
    public String ipoNAcquisitionsCompanyName;
    public List<String> ipoNAcquisitionsExits;

    public IpoNAcquisitions(){}

    public IpoNAcquisitions(String ipoNAcquisitionsDate, String ipoNAcquisitionsCompanyName, List<String> ipoNAcquisitionsExits) {
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

    public List<String> getIpoNAcquisitionsExits() {
        return ipoNAcquisitionsExits;
    }

    public void setIpoNAcquisitionsExits(List<String> ipoNAcquisitionsExits) {
        this.ipoNAcquisitionsExits = ipoNAcquisitionsExits;
    }
}
