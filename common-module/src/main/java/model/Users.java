package model;


import model.enums.Gender;
import model.enums.Status;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Users implements Serializable {

    private Long id;
    private String phoneNumber;
    private String name;
    private String email;
    private String picturePath;
    private String password;
    private Gender gender;
    private String country;
    private LocalDate dob;
    private String bio;
    private Status status;
    private LocalDateTime lastSeen;

    public Users() {
    }

    public Users(Long id, String phoneNumber, String name, String email,
                 String picturePath, String password, Gender gender,
                 String country, LocalDate dob, String bio,
                 Status status, LocalDateTime lastSeen) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.picturePath = picturePath;
        this.password = password;
        this.gender = gender;
        this.country = country;
        this.dob = dob;
        this.bio = bio;
        this.status = status;
        this.lastSeen = lastSeen;
    }

    public Users(String phoneNumber, String name, String email,
                 String picturePath, String password, Gender gender,
                 String country, LocalDate dob, String bio,
                 Status status, LocalDateTime lastSeen) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.picturePath = picturePath;
        this.password = password;
        this.gender = gender;
        this.country = country;
        this.dob = dob;
        this.bio = bio;
        this.status = status;
        this.lastSeen = lastSeen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", country='" + country + '\'' +
                ", dob=" + dob +
                ", bio='" + bio + '\'' +
                ", status=" + status +
                ", lastSeen=" + lastSeen +
                '}';
    }
}
