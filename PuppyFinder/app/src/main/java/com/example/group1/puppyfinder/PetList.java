package com.example.group1.puppyfinder;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PetList extends AppCompatActivity {
    private LinearLayout verticalLinearLayout, currentView, bigView;
    private DatabaseReference mShowPets;
    ScrollView scrollView;
    DataSnapshot data;
    Integer petLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event);
        mShowPets = FirebaseDatabase.getInstance().getReference().child("pet_id");

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
        textView.setText("Pet List");
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

        /*

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
        mShowPets.addValueEventListener(showPetsListener);

    }

    ValueEventListener showPetsListener = new ValueEventListener() {
        @Override
            //This is called once our app boots up since this is placed in onStart
            //This will be called again if our database reference changes (i.e. someone writes new data)
            public void onDataChange(DataSnapshot dataSnapshot) {

                //This cleans the dataSnapshot of the Firebase Reference
                //into the Strings, Floats, and Integers that we stored

                //Step-6: Use data
                data = dataSnapshot;
                showPets();


            }
            @Override
            //This is only called if there is an error within our data retrieval
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(PetList.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };

    private void showPets(){

        PetInformation[] _PetList = showPets(data);

        bigView.removeView(currentView);
        currentView = new LinearLayout(this);
        currentView.setOrientation(LinearLayout.VERTICAL);
        Log.d("LengthTest", String.valueOf(petLength));

        for (int i = 0; i < petLength; i++) { // go through all shelters


            String petName = _PetList[i].getName();
            Log.d("Firebasetest", petName);
                //How to parse for correct pet
                /*
                if(!(shelterName.toLowerCase().contains(nameSearch.toLowerCase())) || !(address.toLowerCase().contains(addressSearch.toLowerCase()))){
                    continue;
                }
                */

                // add new horizontalLinearLayout
            LinearLayout horizontalLinearLayout = new LinearLayout(PetList.this);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            // TODO: NEED IMAGE OF THE SHELTER

            LinearLayout columns = new LinearLayout(PetList.this);
            columns.setOrientation(LinearLayout.VERTICAL);

            // add name of event to view
            String name = petName;
            TextView textView = new TextView(PetList.this);
            textView.setTextSize(18f);
            textView.setBackgroundColor(0xFDED1464);
            textView.setTextColor(0xFFFFFFFF);
            textView.setText(name);
            textView.setGravity(Gravity.CENTER);
            columns.addView(textView);

            LinearLayout miniLayout = new LinearLayout(PetList.this);
            miniLayout.setOrientation(LinearLayout.HORIZONTAL);
            miniLayout.setDividerPadding(18);
            miniLayout.setPadding(25,8,8,8);

            /*
            // add moreInfo to view
            String moreInfo = _PetList[i].getDescription();
                    Button button = new Button(EventActivity.this);
                    button.setPadding(10,10,10,10);
                    button.setText("More Info");
                    button.setBackgroundColor(0xFF06BDCB);
                    setOnClick(button, moreInfo);
                    miniLayout.addView(button);
                    */

                    LinearLayout ml = new LinearLayout(PetList.this);
                    ml.setOrientation(LinearLayout.VERTICAL);
                    ml.setPadding(25,10,10,10);

                    // Add date of event to view
                    String breed = _PetList[i].getBreed();
                    textView = new TextView(PetList.this);
                    textView.setText(breed);
                    textView.setTextColor(0xFF644242);
                    ml.addView(textView);

                    // add address to view
                    String description = _PetList[i].getDescription();
                    textView = new TextView(PetList.this);
                    textView.setText(description);
                    textView.setTextColor(0xFF644242);
                    ml.addView(textView);

                    // add start/end times to view
                    String Gender = _PetList[i].getGender();
                    textView = new TextView(PetList.this);
                    textView.setText(Gender);
                    textView.setTextColor(0xFF644242);
                    ml.addView(textView);

                    Integer Age = _PetList[i].getAge();
                    textView = new TextView(PetList.this);
                    textView.setText("Age: " + String.valueOf(Age));
                    textView.setTextColor(0xFF644242);
                    ml.addView(textView);

                    miniLayout.addView(ml);
                    columns.addView(miniLayout);
/*

                    // Add button for map of this shelter to view
                    Float latitude = events[j].getLatitude();
                    Float longitude = events[j].getLongitude();
                    button = new Button(EventActivity.this);
                    button.setText("Map");
                    button.setBackgroundColor(0xFF06BDCB);
                    setOnClick(button, latitude, longitude, name, address);
                    columns.addView(button);
*/
                    textView = new TextView(PetList.this);
                    textView.setText("                                 ");

                    columns.addView(textView);

                    horizontalLinearLayout.addView(columns);

                    currentView.addView(horizontalLinearLayout);

            } // end outer for
            bigView.addView(currentView);

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
            count += 1;
        }
        petLength = count;
        return petList;

    }



}
