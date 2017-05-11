package com.example.group1.puppyfinder;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    String shelter_id, age, gender, breed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event);
        mShowPets = FirebaseDatabase.getInstance().getReference().child("pet_id");

        //get intent extras
        shelter_id = getIntent().getExtras().getString("shelterName");
        age = getIntent().getExtras().getString("age");
        gender = getIntent().getExtras().getString("gender");
        breed = getIntent().getExtras().getString("breed");
        //incase nothing passes from these values
        if(shelter_id == null){
            shelter_id = "";
        }
        if(age == null){
            age = "";
        }
        if(gender == null){
            gender = "";
        }
        if(breed == null){
            breed = "";
        }
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

    public void addHeader(){
        // Title
        TextView textView = new TextView(this);
        textView.setText("Pet List");
        textView.setTextColor(0xFFFFFFFF);
        textView.setBackgroundColor(0xFF06BDCB);
        textView.setTextSize(24f);
        textView.setPadding(8,45,8,45);
        textView.setGravity(Gravity.CENTER);
        verticalLinearLayout.addView(textView);

        // add new horizontalLinearLayout
        LinearLayout horizontalLinearLayout = new LinearLayout(this);
        horizontalLinearLayout.setOrientation(LinearLayout.VERTICAL);
        horizontalLinearLayout.setGravity(Gravity.CENTER);

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

        for (int i = 0; i < petLength; i++) { // go through all shelters


            String petName = _PetList[i].getName();
            //How to parse for correct pet

            if(!(_PetList[i].getShelter_id().toLowerCase().contains(shelter_id.toLowerCase()))
                    || !(_PetList[i].getAge().toString().toLowerCase().contains(age.toLowerCase()))
                    || !(_PetList[i].getGender().toString().toLowerCase().contains(gender.toLowerCase()))
                    || !(_PetList[i].getBreed().toString().toLowerCase().contains(breed.toLowerCase()))
                    ){
                    continue;
             }


                // add new horizontalLinearLayout
            LinearLayout horizontalLinearLayout = new LinearLayout(PetList.this);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            // TODO: NEED IMAGE OF THE SHELTER

            LinearLayout columns = new LinearLayout(PetList.this);
            columns.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            columns.setLayoutParams(params);

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

            ImageView pic = new ImageView(PetList.this);
            pic.setPadding(10,10,10,10);
            pic.setBackgroundColor(0xFF06BDCB);
            pic.setImageResource(R.drawable.gritty);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(450, 300);
            pic.setLayoutParams(layoutParams);
            miniLayout.addView(pic);



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
