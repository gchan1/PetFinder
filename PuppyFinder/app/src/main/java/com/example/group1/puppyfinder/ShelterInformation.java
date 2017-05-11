package com.example.group1.puppyfinder;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Justin_C on 5/3/17.
 */

public class ShelterInformation implements Serializable {
    private String Name;
    private String Address;
    private String Contact;
    private Float Latitude;
    private Float Longitude;
    private Long Number;
    private String[] petList;
    private EventInformation[] eventList;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Integer numEvents;

    //these are only needed for parsing, will not be used after
    private HashMap<String, EventInformation> events;
    //see http://stackoverflow.com/questions/39508681/how-to-fetch-data-in-firebase-when-the-child-is-nested-from-a-table
    //for explanation
    private HashMap<String, Object> pet_ids;




    public void setPetList( String[] foo){
        petList = foo;

    }
    public void setEventList(EventInformation [] foo){
        eventList = foo;

    }

    public String getName() {
        return Name;
    }

    public String setName(String name) {
        Name = name;
        return name;
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

    public Float getLatitude() {
        return Latitude;
    }

    public double setLatitude(Float latitude) {
        Latitude = latitude;
        return Latitude;
    }

    public Float getLongitude() {
        return Longitude;
    }

    public double setLongitude(Float longitude) {
        Longitude = longitude;
        return Longitude;
    }

    public Long getNumber() {
        return Number;
    }

    public void setNumber(Long number) {
        Number = number;
    }

    public HashMap<String, Object> getPet_ids() {
        return pet_ids;
    }

    public void setPet_ids(HashMap<String, Object> pet_ids) {
        this.pet_ids = pet_ids;
    }

    public HashMap<String, EventInformation> getEvents() {return events; }

    public void setEvents(HashMap<String, EventInformation> events) {this.events = events;}

    //parses datasnapshot to get list of pets
    public String[] getListOfPets(){
        HashMap<String, Object> pet_ids = this.pet_ids;
        Integer count = 0;
        String petList[] = new String[pet_ids.size()];
        for (Object value: pet_ids.values()){
            petList[count] = (String) value;
            count += 1;

        }
        return petList;

    }

    //Creates class object EventInformation from the datasnapshot
    public EventInformation[] getListOfEvents(){
        HashMap<String, EventInformation> events = this.events;
        Integer count = 0;
        EventInformation[] eventList = new EventInformation[events.size()];
        for (EventInformation value: events.values()){
            EventInformation event = new EventInformation();
            event.setAddress(value.getAddress());
            event.setLongitude(value.getLongitude());
            event.setLatitude(value.getLatitude());
            event.setDate(value.getDate());
            event.setEnd(value.getEnd());
            event.setMoreInfo(value.getMoreInfo());
            event.setStart(value.getStart());
            event.setName(value.getName());

            eventList[count] = event;
            count+= 1;
        }
        numEvents = count;

        return eventList;
    }

    public Integer getNumEvents() {
        return numEvents;
    }


}
