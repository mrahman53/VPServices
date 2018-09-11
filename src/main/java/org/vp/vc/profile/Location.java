package org.vp.vc.profile;

import org.bson.Document;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by mrahman on 7/26/16.
 */


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Location implements Serializable {

    public  String city = "city";
    public  String state = "state";
    public  String country = "country";

    public Location(){ }
    public Location(String city, String state, String country) {
        this.city = city;
        this.state = state;
        this.country = country;
    }
    public Location(Document doc){
        //this(doc.getString(city),doc.getString(state),doc.getString(country));
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
