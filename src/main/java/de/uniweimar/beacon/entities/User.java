package de.uniweimar.beacon.entities;

import com.orm.SugarRecord;

import java.util.Date;

public class User extends SugarRecord {

String userName;
String deviceId;
String gender;
String birthDate;
String textEmail;

    public User(String userName,String deviceId,String gender,String birthDate,String textEmail) {
        this.birthDate=birthDate;
        this.deviceId=deviceId;
        this.gender=gender;
        this.userName=userName;
        this.textEmail=textEmail;
    }
    public User() {
    }

    public String getUserName() {
        return userName;
    }
}