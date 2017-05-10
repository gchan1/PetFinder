package com.example.group1.puppyfinder;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static com.example.group1.puppyfinder.R.attr.colorPrimaryDark;
import static com.example.group1.puppyfinder.R.attr.layout;

public class EventActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mShowEvents;
    private LinearLayout verticalLinearLayout, currentView, bigView;
    Integer shelterLength, eventLength;
    boolean isOnScreen = false; // if the data is onScreen, then don't grab it again
    ScrollView scrollView;
    EditText addressEditText, nameEditText;
    boolean isDataReady = false;
    DataSnapshot data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_event);

        mShowEvents = FirebaseDatabase.getInstance().getReference().child("shelter_id");

        /* Creating Layout in Java */
        scrollView = new ScrollView(this);// (ScrollView) findViewById(R.id.scrollView);
        this.setContentView(scrollView);


        bigView = new LinearLayout(this);
        bigView.setOrientation(LinearLayout.VERTICAL);
        bigView.setBackgroundColor(0xFF06BDCB);
        scrollView.addView(bigView);

        verticalLinearLayout = new LinearLayout(this);
        verticalLinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(140, 398);
        //layoutParams.setMargins(24, 0, 24, 0);
        bigView.addView(verticalLinearLayout);

        // Used later
        currentView = new LinearLayout(this);
        currentView.setOrientation(LinearLayout.VERTICAL);
        bigView.addView(currentView);

        addHeader(); // Add the header and header rows to the activity
    }

    public void addHeader(){
        // Title
        TextView textView = new TextView(this);
        textView.setText("Sponsors & Events");
        textView.setTextSize(24f);
        textView.setPadding(8,8,8,8);
        textView.setGravity(Gravity.CENTER);
        verticalLinearLayout.addView(textView);

        // add new horizontalLinearLayout
        LinearLayout horizontalLinearLayout = new LinearLayout(this);
        horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        // Headings for editText searches
        textView = new TextView(this);
        textView.setText("Zip Code/Location \t");
        horizontalLinearLayout.addView(textView);
        textView = new TextView(this);
        textView.setText("\t Sponsor Name");
        horizontalLinearLayout.addView(textView);


        verticalLinearLayout.addView(horizontalLinearLayout);

        // Adding Button and editText searches
        LinearLayout horizontalLinearLayout2 = new LinearLayout(this);
        horizontalLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);

        addressEditText = new EditText(this);
        nameEditText = new EditText(this);
        Button button = new Button(this);
        button.setText("Go!");
        button.setOnClickListener(this);

        horizontalLinearLayout2.addView(addressEditText);
        horizontalLinearLayout2.addView(nameEditText);
        horizontalLinearLayout2.addView(button);
        verticalLinearLayout.addView(horizontalLinearLayout2);

        // add new horizontalLinearLayout
        LinearLayout horizontalLinearLayout3 = new LinearLayout(this);
        horizontalLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
        String title = "";
        for(int i = 0; i < 4; i++){
            if(i == 0){
                title = "Sponsors";
            }
            else if(i == 1){
                title = "Events";
            }
            else if(i == 2){
                title = "Location/Date";
            }
            else if(i == 3){
                title = "Map";
            }
            textView = new TextView(this);
            textView.setText(title);
            horizontalLinearLayout3.addView(textView);
        }
        verticalLinearLayout.addView(horizontalLinearLayout3);
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
        mShowEvents.addValueEventListener(shelterEventListener);
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

    //for mShowEvents
    ValueEventListener shelterEventListener = new ValueEventListener() {
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
            Toast.makeText(EventActivity.this, "Failed to load post.",
                    Toast.LENGTH_SHORT).show();
        }
    };

    private void showData(final String addressSearch, final String nameSearch) {
        ShelterInformation[] _shelterList = showShelter(data);
        bigView.removeView(currentView);
        currentView = new LinearLayout(this);
        currentView.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < shelterLength; i++) { // go through all shelters
            String shelterName = _shelterList[i].getName();
            EventInformation[] events = _shelterList[i].getListOfEvents();
            for (int j = 0; j < _shelterList[i].getNumEvents(); j++) { // go through all events
                String address = events[j].getAddress();
                if(!(shelterName.toLowerCase().contains(nameSearch.toLowerCase())) || !(address.toLowerCase().contains(addressSearch.toLowerCase()))){
                    continue;
                }

                // add new horizontalLinearLayout
                LinearLayout horizontalLinearLayout = new LinearLayout(EventActivity.this);
                horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                // TODO: NEED IMAGE OF THE SHELTER

                LinearLayout columns = new LinearLayout(EventActivity.this);
                columns.setOrientation(LinearLayout.VERTICAL);

                LinearLayout miniLayout = new LinearLayout(EventActivity.this);
                miniLayout.setOrientation(LinearLayout.HORIZONTAL);
                miniLayout.setDividerPadding(18);
                // add name of event to view
                String name = events[j].getName();
                TextView textView = new TextView(EventActivity.this);
                textView.setTextSize(18f);
                textView.setBackgroundColor(0xFDED1464);
                textView.setText(name);
                columns.addView(textView);

                // add moreInfo to view
                String moreInfo = events[j].getMoreInfo();
                Button button = new Button(EventActivity.this);
                button.setText("More Info");
                setOnClick(button, moreInfo);
                miniLayout.addView(button);

                columns.addView(miniLayout);


                miniLayout = new LinearLayout(EventActivity.this);
                miniLayout.setOrientation(LinearLayout.VERTICAL);

                // Add date of event to view
                String date = events[j].getDate();
                textView = new TextView(EventActivity.this);
                textView.setText(date);
                miniLayout.addView(textView);

                // add address to view
                textView = new TextView(EventActivity.this);
                textView.setText(address);
                miniLayout.addView(textView);

                // add start/end times to view
                String startTime = events[j].getStart();
                String endTime = events[j].getEnd();
                if(startTime != null && endTime != null){ // only add if there are specific times that the event takes place
                    textView = new TextView(EventActivity.this);
                    textView.setText(startTime + "-" + endTime);
                    miniLayout.addView(textView);
                }
                columns.addView(miniLayout);


                // Add button for map of this shelter to view
                Float latitude = events[j].getLatitude();
                Float longitude = events[j].getLongitude();
                button = new Button(EventActivity.this);
                button.setText("Map");
                setOnClick(button, latitude, longitude, name, address);
                columns.addView(button);
                horizontalLinearLayout.addView(columns);

                currentView.addView(horizontalLinearLayout);
            } // end inner for
        } // end outer for
        bigView.addView(currentView);

    }

    // You can use lat/lon in any onClick now. lat/lon can be different for multiple buttons
    private void setOnClick(final Button button, final Float lat, final Float lon, final String name, final String address){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: use Lat/Lon for Map
                Intent intent = new Intent(getBaseContext(), ShelterMarkerActivity.class);
                intent.putExtra("lastActivity", 2);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                startActivity(intent);

            }
        });
    } // end setOnClick

    private void setOnClick(final Button button, final String moreInfo){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(moreInfo);
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

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
