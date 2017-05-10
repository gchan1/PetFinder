package com.example.group1.puppyfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShelterLocationActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mShowShelters;
    private LinearLayout verticalLinearLayout, currentView, bigView;
    Integer shelterLength;
    boolean isOnScreen = false; // if the data is onScreen, then don't grab it again
    EditText addressEditText, nameEditText;
    boolean isDataReady = false;
    DataSnapshot data;
    ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_location);

        mShowShelters = FirebaseDatabase.getInstance().getReference().child("shelter_id");

        /* Creating Layout in Java */
        scrollView = new ScrollView(this);// (ScrollView) findViewById(R.id.scrollView);
        this.setContentView(scrollView);
        bigView = new LinearLayout(this);
        bigView.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(bigView);

        verticalLinearLayout = new LinearLayout(this);
        verticalLinearLayout.setOrientation(LinearLayout.VERTICAL);
        bigView.addView(verticalLinearLayout);

        // Used later
        currentView = new LinearLayout(this);
        currentView.setOrientation(LinearLayout.VERTICAL);
        bigView.addView(currentView);

        addHeader(); // Add the header and header rows to the activity
    }

    private void addHeader(){
        // Title
        TextView textView = new TextView(this);
        textView.setText("Search for Animal Welfare Groups");
        verticalLinearLayout.addView(textView);

        // add new horizontalLinearLayout
        LinearLayout horizontalLinearLayout = new LinearLayout(this);
        horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Headings for editText searches
        textView = new TextView(this);
        textView.setText("Zip Code/Location");
        horizontalLinearLayout.addView(textView);

        textView = new TextView(this);
        textView.setText("Group Name");
        horizontalLinearLayout.addView(textView);
        verticalLinearLayout.addView(horizontalLinearLayout);

        // Adding Button and editText searches
        horizontalLinearLayout = new LinearLayout(this);
        horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        addressEditText = new EditText(this);
        nameEditText = new EditText(this);
        Button button = new Button(this);
        button.setText("Go!");
        button.setOnClickListener(this);

        horizontalLinearLayout.addView(addressEditText);
        horizontalLinearLayout.addView(nameEditText);
        horizontalLinearLayout.addView(button);
        verticalLinearLayout.addView(horizontalLinearLayout);

        // Adding row headers
        horizontalLinearLayout = new LinearLayout(ShelterLocationActivity.this);
        horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        String title = "";
        for(int i = 0; i < 5; i++){
            if(i == 0){
                title = "Shelter";
            }
            else if(i == 1){
                title = "Puppy List";
            }
            else if(i == 2){
                title = "City/State";
            }
            else if(i == 3){
                title = "Map";
            }
            else if(i == 4){
                title = "Contact";
            }
            textView = new TextView(this);
            textView.setText(title);
            horizontalLinearLayout.addView(textView);
        }
        verticalLinearLayout.addView(horizontalLinearLayout);
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
            isDataReady = true;
            data = dataSnapshot;
            if(!isOnScreen){
                showData("", "");
                isOnScreen = true;
            }
            // else don't do anything
        } // end onDataChange

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
            Toast.makeText(ShelterLocationActivity.this, "Failed to load post.",
                    Toast.LENGTH_SHORT).show();
        }
    };

    // Shows all of the data, with the possible searches
    private void showData(final String addressSearch, final String nameSearch) {
        ShelterInformation[] _shelterList = showShelter(data);
        bigView.removeView(currentView);
        currentView = new LinearLayout(this);
        currentView.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < shelterLength; i++) {
            String name = _shelterList[i].getName();
            String address = _shelterList[i].getAddress();
            if(!(name.toLowerCase().contains(nameSearch.toLowerCase())) || !(address.toLowerCase().contains(addressSearch.toLowerCase()))){
                continue;
            }


            // add new horizontalLinearLayout
            LinearLayout horizontalLinearLayout = new LinearLayout(ShelterLocationActivity.this);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            Long number = _shelterList[i].getNumber();

            // add name of shelter to view
            TextView textView = new TextView(ShelterLocationActivity.this);
            textView.setText(name);
            horizontalLinearLayout.addView(textView);

            // Add button for a view of all of the pets at the shelter
            String[] petList = _shelterList[i].getListOfPets();
            //button = new Button(ShelterLocationActivity.this);
            Button button = new Button(ShelterLocationActivity.this);
            button.setText("Puppy List");
            setOnClick(button, petList);
            horizontalLinearLayout.addView(button);

            // add address to view
            textView = new TextView(ShelterLocationActivity.this);
            textView.setText(address);
            horizontalLinearLayout.addView(textView);


            // Add button for map of this shelter to view
            Float latitude = _shelterList[i].getLatitude();
            Float longitude = _shelterList[i].getLongitude();
            //Button button = new Button(ShelterLocationActivity.this);
            button = new Button(ShelterLocationActivity.this);
            button.setText("Map");
            setOnClick(button, latitude, longitude, name, address, number);
            horizontalLinearLayout.addView(button);

            // add contact info to view
            String contactInfo = _shelterList[i].getContact();
            textView = new TextView(ShelterLocationActivity.this);
            textView.setText(contactInfo);
            horizontalLinearLayout.addView(textView);

            currentView.addView(horizontalLinearLayout);
        } // end for
        bigView.addView(currentView);
    }

    // You can use lat/lon in any onClick now. lat/lon can be different for multiple buttons
    private void setOnClick(final Button button, final Float lat, final Float lon, final String name, final String address, final Long number){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ShelterMarkerActivity.class);
                intent.putExtra("lastActivity", 1);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });
    } // end setOnClick

    private void setOnClick(final Button button, final String[] petList){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Uncomment this with correct activity to pass to
                /*
                //Intent intent = new Intent(getBaseContext(), PuppyActivity.class);
                intent.putExtra("petList", petList);
                startActivity(intent);
                */
            }
        });
    } // end setOnClick

    // GO Button
    @Override
    public void onClick(View v) {
        if(isDataReady){
            String address = addressEditText.getText().toString();
            String name = nameEditText.getText().toString();
            showData(address, name);
        }
        else{
            Toast.makeText(this, "We need to gather the data...", Toast.LENGTH_LONG).show();
        }
    }
}
