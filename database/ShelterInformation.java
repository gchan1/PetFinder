package com.example.justin_c.firebaseexample;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Justin_C on 5/3/17.
 */

public class ShelterInformation {
    private String Name;
    private String Address;
    private String Contact;
    private String Hours;
    private Float Latitude;
    private Float Longitude;
    private Integer Number;
    //see http://stackoverflow.com/questions/39508681/how-to-fetch-data-in-firebase-when-the-child-is-nested-from-a-table
    //for explanation
    private HashMap<String, Object> pet_ids;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getHours() {
        return Hours;
    }

    public void setHours(String hours) {
        Hours = hours;
    }

    public Float getLatitude() {
        return Latitude;
    }

    public void setLatitude(Float latitude) {
        Latitude = latitude;
    }

    public Float getLongitude() {
        return Longitude;
    }

    public void setLongitude(Float longitude) {
        Longitude = longitude;
    }

    public Integer getNumber() {
        return Number;
    }

    public void setNumber(Integer number) {
        Number = number;
    }

    public HashMap<String, Object> getPet_ids() {
        return pet_ids;
    }

    public void setPet_ids(HashMap<String, Object> pet_ids) {
        this.pet_ids = pet_ids;
    }
}
