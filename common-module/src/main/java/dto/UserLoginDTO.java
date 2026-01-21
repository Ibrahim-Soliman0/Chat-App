package dto;

import java.io.Serializable;

public class UserLoginDTO implements Serializable {

    private String phoneNumber, password;

    public UserLoginDTO() {}

    public UserLoginDTO(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserLoginDTO(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}