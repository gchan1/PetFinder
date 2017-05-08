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

import org.w3c.dom.Text;

public class EventActivity extends AppCompatActivity {
    private DatabaseReference mShowEvents;
    private LinearLayout verticalLinearLayout;
    Integer shelterLength, eventLength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mShowEvents = FirebaseDatabase.getInstance().getReference().child("shelter_id");

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

    //for mShowEvents
    ValueEventListener shelterEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ShelterInformation[] _shelterList = showShelter(dataSnapshot);

            for (int i = 0; i < shelterLength; i++) { // go through all shelters
                EventInformation[] events = _shelterList[i].getListOfEvents();
                for (int j = 0; j < _shelterList[i].getNumEvents(); j++) { // go through all events
                    // add new horizontalLinearLayout
                    LinearLayout horizontalLinearLayout = new LinearLayout(EventActivity.this);
                    horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                    // TODO: NEED IMAGE OF THE SHELTER


                    LinearLayout miniLayout = new LinearLayout(EventActivity.this);
                    miniLayout.setOrientation(LinearLayout.VERTICAL);
                    // add name of event to view
                    String name = events[j].getName();
                    TextView textView = new TextView(EventActivity.this);
                    textView.setText(name);
                    miniLayout.addView(textView);

                    // add moreInfo to view
                    String moreInfo = events[j].getMoreInfo();
                    Button button = new Button(EventActivity.this);
                    button.setText("More Info");
                    setOnClick(button, moreInfo);
                    miniLayout.addView(button);

                    horizontalLinearLayout.addView(miniLayout);

                    

                    miniLayout = new LinearLayout(EventActivity.this);
                    miniLayout.setOrientation(LinearLayout.VERTICAL);

                    // Add date of event to view
                    String date = events[j].getDate();
                    textView = new TextView(EventActivity.this);
                    textView.setText(date);
                    miniLayout.addView(textView);

                    // add address to view
                    String address = events[j].getAddress();
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

                    horizontalLinearLayout.addView(miniLayout);



                    // Add button for map of this shelter to view
                    Float latitude = events[j].getLatitude();
                    Float longitude = events[j].getLongitude();
                    button = new Button(EventActivity.this);
                    button.setText("Map");
                    setOnClick(button, latitude, longitude);
                    horizontalLinearLayout.addView(button);


                    verticalLinearLayout.addView(horizontalLinearLayout);
                } // end inner for
            } // end outer for



            /*


                // add name of shelter to view
                String name = _shelterList[i].getName();
                TextView textView = new TextView(ShelterLocationActivity.this);
                textView.setText(name);
                horizontalLinearLayout.addView(textView);


                // add contact info to view
                String contactInfo = _shelterList[i].getContact();
                textView = new TextView(ShelterLocationActivity.this);
                textView.setText(contactInfo);
                horizontalLinearLayout.addView(textView);

                verticalLinearLayout.addView(horizontalLinearLayout);
            } // end for
 */       } // end onDataChange

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
            Toast.makeText(EventActivity.this, "Failed to load post.",
                    Toast.LENGTH_SHORT).show();
        }
    };

    // You can use lat/lon in any onClick now. lat/lon can be different for multiple buttons
    private void setOnClick(final Button button, final Float lat, final Float lon){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: use Lat/Lon for Map
                // Toast.makeText(ShelterLocationActivity.this, lat.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    } // end setOnClick

    private void setOnClick(final Button button, final String moreInfo){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Take user to the moreInfo site
            }
        });
    } // end setOnClick

}
