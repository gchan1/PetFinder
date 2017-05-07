package com.example.justin_c.firebaseexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Step-1: Declare variables
    private DatabaseReference mShowPets;
    private DatabaseReference mShowShelters;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Step-2: Set which part of the database the listener will refer to
        mShowPets = FirebaseDatabase.getInstance().getReference().child("pet_id");
        mShowShelters = FirebaseDatabase.getInstance().getReference().child("shelter_id");
    }

    @Override
    public void onStart(){
        super.onStart();
        //Step-3: Declare each Value event Listener (need one for each database reference)

        //for mShowPets
        ValueEventListener showPetsListener = new ValueEventListener() {
            @Override
            //This is called once our app boots up since this is placed in onStart
            //This will be called again if our database reference changes (i.e. someone writes new data)
            public void onDataChange(DataSnapshot dataSnapshot) {
                //This cleans the dataSnapshot of the Firebase Reference
                //into the Strings, Floats, and Integers that we stored

                //Step-6: Use data
                PetInformation[] petList = showPets(dataSnapshot);

            }
            @Override
            //This is only called if there is an error within our data retrieval
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        //for mShowShelters
        ValueEventListener shelterListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShelterInformation[] shelterList = showShelter(dataSnapshot);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        //Step-4: Add ValueEventListener to our database reference
        mShowPets.addValueEventListener(showPetsListener);
        mShowShelters.addValueEventListener(shelterListener);
    }

    //Step-5: Parse the dataSnapshot
    private ShelterInformation[] showShelter(DataSnapshot dataSnapshot){
        Integer count = 0;
        ShelterInformation[] shelterList = new ShelterInformation[(int) dataSnapshot.getChildrenCount()];

        for(DataSnapshot ds: dataSnapshot.getChildren()){

            //set variables with only one value
            ShelterInformation sInfo = new ShelterInformation();
            sInfo.setAddress(ds.getValue(ShelterInformation.class).getAddress());
            sInfo.setContact(ds.getValue(ShelterInformation.class).getContact());
            sInfo.setHours(ds.getValue(ShelterInformation.class).getHours());
            sInfo.setLatitude(ds.getValue(ShelterInformation.class).getLatitude());
            sInfo.setLongitude(ds.getValue(ShelterInformation.class).getLongitude());
            sInfo.setName(ds.getValue(ShelterInformation.class).getName());
            sInfo.setNumber(ds.getValue(ShelterInformation.class).getNumber());

            //set variable with multiple values (nested data)
            sInfo.setPet_ids(ds.getValue(ShelterInformation.class).getPet_ids());
            String[] petList = sInfo.getListOfPets();

            sInfo.setPetList(petList);

            sInfo.setEvents(ds.getValue(ShelterInformation.class).getEvents());
            EventInformation[] eventList = sInfo.getListOfEvents();

            sInfo.setEventList(eventList);
            shelterList[count] = sInfo;
            count += 1;

        }
        return shelterList;
    }
    private PetInformation[] showPets(DataSnapshot dataSnapshot){
        //iterate through all the Pets
        Integer count = 0;
        PetInformation[] petList = new PetInformation[(int) dataSnapshot.getChildrenCount()];
        for(DataSnapshot ds: dataSnapshot.getChildren()){
            PetInformation pInfo = new PetInformation();
            pInfo.setName(ds.getValue(PetInformation.class).getName());
            pInfo.setAge(ds.getValue(PetInformation.class).getAge());
            pInfo.setBreed(ds.getValue(PetInformation.class).getBreed());
            pInfo.setDescription(ds.getValue(PetInformation.class).getDescription());
            pInfo.setGender(ds.getValue(PetInformation.class).getGender());
            pInfo.setShelter_id(ds.getValue(PetInformation.class).getShelter_id());

            petList[count] = pInfo;
        }

        return petList;

    }
}
