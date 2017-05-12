package com.example.group1.puppyfinder;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SearchShelterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, LocationListener, View.OnClickListener {

    Spinner spinner;
    LocationManager locationmanager;
    //boolean gps_enabled, network_enabled;
    Float radius, currLat, currLon, shelLat, shelLon;
    private DatabaseReference mShowShelters;
    public Button buttonFind;
    Location mCurrentLocation;
    ShelterInformation[] shelterMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shelter);
        mShowShelters = FirebaseDatabase.getInstance().getReference().child("shelter_id");
        spinner = (Spinner) findViewById(R.id.spinner);
        buttonFind = (Button) findViewById(R.id.button2);
        buttonFind.setOnClickListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_item,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mCurrentLocation = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


    }

    @Override
    public void onStart() {
        super.onStart();

        //for mShowShelters
        ValueEventListener shelterListener = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (ActivityCompat.checkSelfPermission(SearchShelterActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SearchShelterActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SearchShelterActivity.this, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    return;
                }
                int count=0;
                shelterMark = new ShelterInformation[(int) dataSnapshot.getChildrenCount()];
                for(DataSnapshot ds: dataSnapshot.getChildren()){


                    ShelterInformation sInfo = new ShelterInformation();
                    shelLat = (float)sInfo.setLatitude(ds.getValue(ShelterInformation.class).getLatitude());
                    shelLon = (float)sInfo.setLongitude(ds.getValue(ShelterInformation.class).getLongitude());
                    sInfo.setName(ds.getValue(ShelterInformation.class).getName());
                    sInfo.setAddress(ds.getValue(ShelterInformation.class).getAddress());
                    sInfo.setNumber(ds.getValue(ShelterInformation.class).getNumber());
                    if(checkShelterWithinRange(shelLat, shelLon)) {
                        shelterMark[count] = sInfo;
                        count += 1;
                    }
                }
                //transferToMaps(shelterMark);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mShowShelters.addValueEventListener(shelterListener);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button2:
                transferToMaps(shelterMark);
                break;
        }
    }
    public void transferToMaps(ShelterInformation[] shelterMark) {
        Intent intent = new Intent(SearchShelterActivity.this, ShelterMarkerActivity.class);
        intent.putExtra("User_lat",  mCurrentLocation.getLatitude());
        intent.putExtra("User_lon",  mCurrentLocation.getLongitude());
        intent.putExtra("Shelters",shelterMark);
        //Bundle args = new Bundle();
        //args.putSerializable("Shelters",shelterMark);
        //intent.putExtra("BUNDLE",args);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sSelected = parent.getItemAtPosition(position).toString();
        //Toast.makeText(this, sSelected, Toast.LENGTH_SHORT).show();
        radius = Float.parseFloat(sSelected);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        if (dist <= 150)
            return true;
        else
            return false;
    }
}
