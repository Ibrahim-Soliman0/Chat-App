package org.client.chatapp.ui.utils;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SavedUserUtil {
    private String name;
    private String phoneNumber;
    private String encryptedPassword;

    public SavedUserUtil() {

    }

    public SavedUserUtil(String name, String phoneNumber, String encryptedPassword) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.encryptedPassword = encryptedPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
