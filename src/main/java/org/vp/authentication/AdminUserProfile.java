package org.vp.authentication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by mrahman on 8/24/16.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AdminUserProfile {

    String username;
    String password;
    String email;
    boolean value ;

    public AdminUserProfile() {

    }

    public AdminUserProfile(String username, String password, String email, String lastName, boolean value) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.value = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }



}
