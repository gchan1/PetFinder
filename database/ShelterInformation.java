package com.example.justin_c.firebaseexample;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
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
    private String[] petList;
    private EventInformation[] eventList;

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

            eventList[count] = event;
            count+= 1;
        }


        return eventList;
    }


}
