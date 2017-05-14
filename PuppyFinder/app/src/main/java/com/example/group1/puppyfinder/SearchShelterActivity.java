package com.example.group1.puppyfinder;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchShelterActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {

    Spinner spinner;
    LocationManager locationmanager;
    Float radius, currLat, currLon, shelLat, shelLon;
    private DatabaseReference mShowShelters;
    boolean mark = true;
    Integer shelterLength;
    public Button buttonFind;
    Location mCurrentLocation;
    List<ShelterInformation> shelterMark = new ArrayList<ShelterInformation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shelter);
        spinner = (Spinner) findViewById(R.id.spinner);
        buttonFind = (Button) findViewById(R.id.button2);
        buttonFind.setOnClickListener(this);

        Integer[] items = new Integer[]{10,25,50,100,150};
        //String[] items = new String[]{"Select distance in miles", "10", "25", "50", "100", "150"};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mCurrentLocation = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mShowShelters = FirebaseDatabase.getInstance().getReference().child("shelter_id");
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshShelterMark();

        //for mShowShelters

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sSelected = parent.getItemAtPosition(position).toString();
                radius = Float.parseFloat(sSelected);

                refreshShelterMark();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void refreshShelterMark() {
        mShowShelters.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shelterMark = new ArrayList<ShelterInformation>();
                Integer count=0;
                Iterable<DataSnapshot> locations = dataSnapshot.getChildren();
                for(DataSnapshot ds: locations){
                    ShelterInformation sInfo = ds.getValue(ShelterInformation.class);
                    shelLat = sInfo.getLatitude();
                    shelLon = sInfo.getLongitude();
                    sInfo.getName();
                    sInfo.getAddress();
                    if(checkShelterWithinRange(shelLat, shelLon)) {
                        shelterMark.add(sInfo);
                        count += 1;
                    }
                }
                shelterLength = count;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button2:
                Intent intent = new Intent(SearchShelterActivity.this, ShelterMarkerActivity.class);
                intent.putExtra("User_lat",  mCurrentLocation.getLatitude());
                intent.putExtra("User_lon",  mCurrentLocation.getLongitude());
                intent.putParcelableArrayListExtra("Shelters", (ArrayList<? extends Parcelable>) shelterMark);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static float calculateDistance(float lat1, float lon1, float lat2, float lon2) {
        float dLat = (float) Math.toRadians(lat2 - lat1);
        float dLon = (float)Math.toRadians(lon2 - lon1);
        float a = (float)(Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2));
        float c = (float)(2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        float d = 6371 * c;
        return d;
    }

    private boolean checkShelterWithinRange(float shelLat, float shelLon) {
        currLat = (float)mCurrentLocation.getLatitude();
        currLon = (float)mCurrentLocation.getLongitude();
        float dist = calculateDistance(currLat, currLon, shelLat, shelLon);
        //Log.d("radius", String.valueOf(radius));
        if (dist <= radius) {
            mark = true;
        }
        else {
            mark = false;
        }
        return mark;
    }
}
