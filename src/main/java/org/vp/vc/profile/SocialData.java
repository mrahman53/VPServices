package org.vp.vc.profile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by mrahman on 7/26/16.
 */


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SocialData {

    public String facebookUrl;
    public String twitterUrl;
    public String linkedinUrl;

    public SocialData(){

    }

    public SocialData(String facebookUrl, String twitterUrl, String linkedinUrl) {
        this.facebookUrl = facebookUrl;
        this.twitterUrl = twitterUrl;
        this.linkedinUrl = linkedinUrl;
    }



    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }



}
