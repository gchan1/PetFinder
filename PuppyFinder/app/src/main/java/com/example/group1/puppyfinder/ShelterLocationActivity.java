package com.example.group1.puppyfinder;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShelterLocationActivity extends AppCompatActivity {

    private DatabaseReference mShowShelters;
    private LinearLayout verticalLinearLayout;
    Integer shelterLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_location);

        mShowShelters = FirebaseDatabase.getInstance().getReference().child("shelter_id");

        /* Creating Layout in Java */
        ScrollView scrollView = new ScrollView(this);// (ScrollView) findViewById(R.id.scrollView);
        this.setContentView(scrollView);
        verticalLinearLayout = new LinearLayout(this);
        verticalLinearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(verticalLinearLayout);
    }


    @Override
    public void onStart() {
        super.onStart();
        //Step-4: Add ValueEventListener to our database reference
        mShowShelters.addValueEventListener(shelterListener);

    } // end onStart
    //Step-5: Parse the dataSnapshot
    private ShelterInformation[] showShelter (DataSnapshot dataSnapshot){
        Integer count = 0;
        ShelterInformation[] shelterList = new ShelterInformation[(int) dataSnapshot.getChildrenCount()];

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

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
        shelterLength = count;
        return shelterList;
    }

    //for mShowShelters
    ValueEventListener shelterListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ShelterInformation[] _shelterList = showShelter(dataSnapshot);
            // for items in db:
            for (int i = 0; i < shelterLength; i++) {
                // add new horizontalLinearLayout
                LinearLayout horizontalLinearLayout = new LinearLayout(ShelterLocationActivity.this);
                horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                // add name of shelter to view
                String name = _shelterList[i].getName();
                TextView textView = new TextView(ShelterLocationActivity.this);
                textView.setText(name);
                horizontalLinearLayout.addView(textView);

                // Add button for a view of all of the pets at the shelter
                String[] petList = _shelterList[i].getListOfPets();
                Button button = new Button(ShelterLocationActivity.this);
                button.setText("Puppy List");
                setOnClick(button, petList);
                horizontalLinearLayout.addView(button);

                // add address to view
                String address = _shelterList[i].getAddress();
                textView = new TextView(ShelterLocationActivity.this);
                textView.setText(address);
                horizontalLinearLayout.addView(textView);

                // Add button for map of this shelter to view
                Float latitude = _shelterList[i].getLatitude();
                Float longitude = _shelterList[i].getLongitude();
                button = new Button(ShelterLocationActivity.this);
                button.setText("Map");
                setOnClick(button, latitude, longitude);
                horizontalLinearLayout.addView(button);

                // add contact info to view
                String contactInfo = _shelterList[i].getContact();
                textView = new TextView(ShelterLocationActivity.this);
                textView.setText(contactInfo);
                horizontalLinearLayout.addView(textView);

                verticalLinearLayout.addView(horizontalLinearLayout);
            } // end for
        } // end onDataChange

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
            Toast.makeText(ShelterLocationActivity.this, "Failed to load post.",
                    Toast.LENGTH_SHORT).show();
        }
    };

    // You can use lat/lon in any onClick now. lat/lon can be different for multiple buttons
    private void setOnClick(final Button button, final Float lat, final Float lon){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: use Lat/Lon for Map
               // Toast.makeText(ShelterLocationActivity.this, lat.toString(), Toast.LENGTH_SHORT).show(); // will give you a different lat every time

            }
        });
    } // end setOnClick

    private void setOnClick(final Button button, final String[] petList){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Do things with petlist
            }
        });
    } // end setOnClick

}
