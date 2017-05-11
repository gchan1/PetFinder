package com.example.group1.puppyfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    //private TextView mTextMessage;
    public ImageButton buttonShelters, buttonEvents, buttonMap, buttonSearch;
    Intent intent;
    private LinearLayout puppyRows;
    private DatabaseReference mShowPets;
    DataSnapshot data;
    Integer petLength;
    boolean isOnScreen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mShowPets = FirebaseDatabase.getInstance().getReference().child("pet_id");
        setContentView(R.layout.activity_home_screen);
        puppyRows =  (LinearLayout) findViewById(R.id.puppyList);
        buttonShelters = (ImageButton) findViewById(R.id.buttonShelters);
        buttonEvents = (ImageButton) findViewById(R.id.buttonEvents);
        buttonMap = (ImageButton) findViewById(R.id.buttonMap) ;
        buttonSearch = (ImageButton) findViewById(R.id.imageButton5);
        buttonShelters.setOnClickListener(this);
        buttonEvents.setOnClickListener(this);

        puppyRows.setOrientation(LinearLayout.VERTICAL);
        //showPets();
    }

    @Override
    protected void onStart() {
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

    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonShelters:
                intent = new Intent(HomeScreen.this, ShelterLocationActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonEvents:
                intent = new Intent(HomeScreen.this, EventActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonMap:
                intent = new Intent(HomeScreen.this, SearchShelterActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButton5:
                intent = new Intent(HomeScreen.this, PetSearch.class);
                startActivity(intent);
                break;

        }
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
            if(!isOnScreen){
                showPets();
            }
        }
        @Override
        //This is only called if there is an error within our data retrieval
        public void onCancelled(DatabaseError databaseError) {
            Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());

        }
    };

    private void showPets(){

        PetInformation[] _PetList = showPets(data);

        Log.d("LengthTest", String.valueOf(petLength));

        for (int i = 0; i < petLength; i++) { // go through all shelters


            Integer Age = _PetList[i].getAge();
            if(Age < 5){ // Featured pet if >= 5
                continue;
            }

            String petName = _PetList[i].getName();
            Log.d("Firebasetest", petName);

            LinearLayout horizontalLinearLayout = new LinearLayout(this);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            // TODO: NEED IMAGE OF THE SHELTER

            LinearLayout columns = new LinearLayout(this);
            columns.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            columns.setLayoutParams(params);

            // add name of pet to view
            String name = petName;
            TextView textView = new TextView(this);
            textView.setTextSize(18f);
            textView.setBackgroundColor(0xFDED1464);
            textView.setTextColor(0xFFFFFFFF);
            textView.setText(name);
            textView.setGravity(Gravity.CENTER);
            columns.addView(textView);

            LinearLayout miniLayout = new LinearLayout(this);
            miniLayout.setOrientation(LinearLayout.HORIZONTAL);
            miniLayout.setDividerPadding(18);
            miniLayout.setPadding(25,8,8,8);


            LinearLayout ml = new LinearLayout(this);
            ml.setOrientation(LinearLayout.VERTICAL);
            ml.setPadding(25,10,10,10);

            // Add breed to view
            String breed = _PetList[i].getBreed();
            textView = new TextView(this);
            textView.setText(breed);
            textView.setTextColor(0xFF644242);
            ml.addView(textView);

            // add description to view
            String description = _PetList[i].getDescription();
            textView = new TextView(this);
            textView.setText(description);
            textView.setTextColor(0xFF644242);
            ml.addView(textView);

            // add gender to view
            String Gender = _PetList[i].getGender();
            textView = new TextView(this);
            textView.setText(Gender);
            textView.setTextColor(0xFF644242);
            ml.addView(textView);

            textView = new TextView(this);
            textView.setText("Age: " + String.valueOf(Age));
            textView.setTextColor(0xFF644242);
            ml.addView(textView);

            miniLayout.addView(ml);
            columns.addView(miniLayout);

            textView = new TextView(this);
            textView.setText("                                 ");

            columns.addView(textView);
            horizontalLinearLayout.addView(columns);
            puppyRows.addView(horizontalLinearLayout);
        }
        isOnScreen = true;

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
