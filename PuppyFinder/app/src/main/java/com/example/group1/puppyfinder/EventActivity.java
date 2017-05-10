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
        bigView.setBackgroundColor(0xFFFFFFFF);
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
        textView.setTextColor(0xFDED1464);
        textView.setTextSize(24f);
        textView.setPadding(8,45,8,45);
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

        tview = new LinearLayout(this);
        tview.setOrientation(LinearLayout.HORIZONTAL);
        tview.setBackgroundColor(0xFF644242);
        textView = new TextView(this);
        textView.setText("\t Sponsor Name \t \t");
        textView.setTextColor(0xFF06BDCB);
        //textView.setBackgroundColor(0xFFFFFFFF);
        textView.setTextSize(18f);
        tview.addView(textView);
        nameEditText = new EditText(this);
        nameEditText.setTextColor(0xFFFFFFFF);
        nameEditText.setHint("\t name of a sponsor \t");
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
        button.setOnClickListener(this);

        horizontalLinearLayout2.addView(button);
        verticalLinearLayout.addView(horizontalLinearLayout2);

        // add new horizontalLinearLayout
        /*
        LinearLayout horizontalLinearLayout3 = new LinearLayout(this);
        horizontalLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
        String title = "";
        for(int i = 0; i < 4; i++){
            if(i == 0){
                title = "\t \t Sponsors \t \t ";
            }
            else if(i == 1){
                title = "\t \t Events \t \t";
            }
            else if(i == 2){
                title = "\t \t Location/Date \t \t";
            }
            else if(i == 3){
                title = "\t \t Map \t \t";
            }
            textView = new TextView(this);
            textView.setText(title);
            textView.setTextColor(0xFF644242);
            horizontalLinearLayout3.addView(textView);
        }
        verticalLinearLayout.addView(horizontalLinearLayout3);
        */
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

                // add name of event to view
                String name = events[j].getName();
                TextView textView = new TextView(EventActivity.this);
                textView.setTextSize(18f);
                textView.setBackgroundColor(0xFDED1464);
                textView.setTextColor(0xFFFFFFFF);
                textView.setText(name);
                textView.setGravity(Gravity.CENTER);
                columns.addView(textView);

                LinearLayout miniLayout = new LinearLayout(EventActivity.this);
                miniLayout.setOrientation(LinearLayout.HORIZONTAL);
                miniLayout.setDividerPadding(18);
                miniLayout.setPadding(25,8,8,8);
                // add moreInfo to view
                String moreInfo = events[j].getMoreInfo();
                Button button = new Button(EventActivity.this);
                button.setPadding(10,10,10,10);
                button.setText("More Info");
                button.setBackgroundColor(0xFF06BDCB);
                setOnClick(button, moreInfo);
                miniLayout.addView(button);

                LinearLayout ml = new LinearLayout(EventActivity.this);
                ml.setOrientation(LinearLayout.VERTICAL);
                ml.setPadding(25,10,10,10);

                // Add date of event to view
                String date = events[j].getDate();
                textView = new TextView(EventActivity.this);
                textView.setText(date);
                textView.setTextColor(0xFF644242);
                ml.addView(textView);

                // add address to view
                textView = new TextView(EventActivity.this);
                textView.setText(address);
                textView.setTextColor(0xFF644242);
                ml.addView(textView);

                // add start/end times to view
                String startTime = events[j].getStart();
                String endTime = events[j].getEnd();
                if(startTime != null && endTime != null){ // only add if there are specific times that the event takes place
                    textView = new TextView(EventActivity.this);
                    textView.setText(startTime + "-" + endTime);
                    textView.setTextColor(0xFF644242);
                    ml.addView(textView);
                }
                miniLayout.addView(ml);
                columns.addView(miniLayout);


                // Add button for map of this shelter to view
                Float latitude = events[j].getLatitude();
                Float longitude = events[j].getLongitude();
                button = new Button(EventActivity.this);
                button.setText("Map");
                button.setBackgroundColor(0xFF06BDCB);
                setOnClick(button, latitude, longitude, name, address);
                columns.addView(button);

                textView = new TextView(EventActivity.this);
                textView.setText("                                 ");
                columns.addView(textView);
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
