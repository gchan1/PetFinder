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

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private DatabaseReference mShowPets;
    private DatabaseReference mShowShelters;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("pet_id").child("a").child("Breed");
        mShowPets = FirebaseDatabase.getInstance().getReference().child("pet_id");
        mShowShelters = FirebaseDatabase.getInstance().getReference().child("shelter_id");


    }

    @Override
    public void onStart(){
        super.onStart();
        //for mShowPets
        ValueEventListener showDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //shows how to implement classes
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        //for mDatabase
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                showBreed(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        ValueEventListener shelterListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showShelter(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);
        mShowPets.addValueEventListener(showDataListener);
        mShowShelters.addValueEventListener(shelterListener);

    }
    private void showShelter(DataSnapshot dataSnapshot){
        for(DataSnapshot ds: dataSnapshot.getChildren()){
            ShelterInformation sInfo = new ShelterInformation();
            sInfo.setPet_ids(ds.getValue(ShelterInformation.class).getPet_ids());

            //now iterate through hashmap to get pet id's
        }

    }

    private void showBreed(DataSnapshot dataSnapshot){
        String value = dataSnapshot.getValue(String.class);
        Log.d("showBreed", "Value is "+ value);

    }

    private void showData(DataSnapshot dataSnapshot){
        //iterate through all the Pets
        for(DataSnapshot ds: dataSnapshot.getChildren()){
            PetInformation pInfo = new PetInformation();
            pInfo.setName(ds.getValue(PetInformation.class).getName());
            pInfo.setAge(ds.getValue(PetInformation.class).getAge());
            pInfo.setBreed(ds.getValue(PetInformation.class).getBreed());
            pInfo.setDescription(ds.getValue(PetInformation.class).getDescription());
            pInfo.setGender(ds.getValue(PetInformation.class).getGender());
            pInfo.setShelter_id(ds.getValue(PetInformation.class).getShelter_id());

            Log.d("showData","name: " + pInfo.getName());
            Log.d("showData","age: " + pInfo.getAge());

        }

    }

}
