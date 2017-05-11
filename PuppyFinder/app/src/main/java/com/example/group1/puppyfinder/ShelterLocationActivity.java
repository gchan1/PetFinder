package com.example.group1.puppyfinder;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import android.view.Gravity;
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
        bigView.setBackgroundColor(0xFFFFFFFF);
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
        textView.setText("Shelter List");
        textView.setTextColor(0xFDED1464);
        textView.setTextSize(24f);
        textView.setPadding(8,75,8,45);
        textView.setGravity(Gravity.CENTER);
        verticalLinearLayout.addView(textView);

        // add new horizontalLinearLayout
        LinearLayout horizontalLinearLayout = new LinearLayout(this);
        horizontalLinearLayout.setOrientation(LinearLayout.VERTICAL);
        horizontalLinearLayout.setGravity(Gravity.CENTER);

        // Headings for editText searches
        LinearLayout tview = new LinearLayout(this);
        tview.setBackgroundColor(0xFF644242);
        tview.setOrientation(LinearLayout.HORIZONTAL);
        textView = new TextView(this);
        textView.setText("\t Zip Code/Location \t");
        textView.setTextColor(0xFF06BDCB);
        //textView.setBackgroundColor(0xFFFFFFFF);
        textView.setTextSize(18f);
        tview.addView(textView);
        addressEditText = new EditText(this);
        addressEditText.setTextColor(0xFD000000);
        addressEditText.setHint("\t location \t");
        addressEditText.setHintTextColor(0xFFFFFFFF);
        tview.addView(addressEditText);

        horizontalLinearLayout.addView(tview);

        verticalLinearLayout.addView(horizontalLinearLayout);

        // Adding Button and editText searches
        horizontalLinearLayout = new LinearLayout(this);
        horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        tview = new LinearLayout(this);
        tview.setOrientation(LinearLayout.HORIZONTAL);
        tview.setBackgroundColor(0xFF644242);
        textView = new TextView(this);
        textView.setText("\t Name of Organization \t \t");
        textView.setTextColor(0xFF06BDCB);
        //textView.setBackgroundColor(0xFFFFFFFF);
        textView.setTextSize(18f);
        tview.addView(textView);
        nameEditText = new EditText(this);
        nameEditText.setTextColor(0xFFFFFFFF);
        nameEditText.setHint("\t name of organization \t");
        nameEditText.setHintTextColor(0xFFFFFFFF);

        tview.addView(nameEditText);
        horizontalLinearLayout.addView(tview);
        verticalLinearLayout.addView(horizontalLinearLayout);

        // Adding Button and editText searches
        LinearLayout horizontalLinearLayout2 = new LinearLayout(this);
        horizontalLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLinearLayout2.setGravity(Gravity.LEFT);

        Button button = new Button(this);
        button.setText("Go!");
        button.setPadding(10,10,10,10);
        button.setOnClickListener(this);

        textView = new TextView(this);
        textView.setTextSize(18f);
        textView.setText("\t                   \t \t");

        horizontalLinearLayout2.addView(button);
        horizontalLinearLayout2.addView(textView);
        verticalLinearLayout.addView(horizontalLinearLayout2);

    }

    @Override
    public void onStart() {
        super.onStart();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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
            sInfo.setId(ds.getValue(ShelterInformation.class).getId());

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
            String id = _shelterList[i].getId();
            String address = _shelterList[i].getAddress();
            if(!(name.toLowerCase().contains(nameSearch.toLowerCase())) || !(address.toLowerCase().contains(addressSearch.toLowerCase()))){
                continue;
            }


            // add new horizontalLinearLayout
            LinearLayout horizontalLinearLayout = new LinearLayout(ShelterLocationActivity.this);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            Long number = _shelterList[i].getNumber();

            LinearLayout columns = new LinearLayout(ShelterLocationActivity.this);
            columns.setOrientation(LinearLayout.VERTICAL);
            columns.setPadding(25,52,25,0);

            // add name of shelter to view
            TextView textView = new TextView(ShelterLocationActivity.this);
            textView.setTextSize(18f);
            textView.setBackgroundColor(0xFDED1464);
            textView.setTextColor(0xFFFFFFFF);
            textView.setText(name);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            columns.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            columns.addView(textView);

            // Add button for a view of all of the pets at the shelter
            //button = new Button(ShelterLocationActivity.this);
            Button button = new Button(ShelterLocationActivity.this);
            button.setText("Puppy List");
            setOnClick(button, id);
            columns.addView(button);

            // add address to view
            textView = new TextView(ShelterLocationActivity.this);
            textView.setText(address);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(0xFF644242);
            columns.addView(textView);


            // Add button for map of this shelter to view
            Float latitude = _shelterList[i].getLatitude();
            Float longitude = _shelterList[i].getLongitude();
            button = new Button(ShelterLocationActivity.this);
            button.setText("Map");
            button.setBackgroundColor(0xFF06BDCB);
            setOnClick(button, latitude, longitude, name, address,number);
            columns.addView(button);


            // add contact info to view
            String contactInfo = _shelterList[i].getContact();
            textView = new TextView(ShelterLocationActivity.this);
            textView.setText(contactInfo);
            columns.addView(textView);

            textView = new TextView(ShelterLocationActivity.this);
            textView.setText("                                 ");
            columns.addView(textView);
            horizontalLinearLayout.addView(columns);
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

    private void setOnClick(final Button button, final String shelterName){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PetList.class);
                intent.putExtra("shelterName", shelterName);
                startActivity(intent);

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
