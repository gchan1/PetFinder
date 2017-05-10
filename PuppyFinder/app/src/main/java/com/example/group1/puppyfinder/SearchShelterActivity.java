package com.example.group1.puppyfinder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchShelterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, LocationListener {

    Spinner spinner;
    LocationManager locationmanager;
    boolean gps_enabled, network_enabled;
    Float radius, currLat, currLon, shelLat, shelLon, rad;
    //EditText etRadius;
    //String search_rad;
    private DatabaseReference mShowShelters;
    Location mCurrentLocation;
    List<ShelterInformation> shelterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shelter);
        //etRadius = (EditText) findViewById(R.id.editText);
        //search_rad = etRadius.getText().toString();
        //double rad = Double.parseDouble(search_rad);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_item,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mShowShelters = FirebaseDatabase.getInstance().getReference().child("shelter_id");
    }

    @Override
    public void onStart() {
        super.onStart();
        //for mShowShelters
        mShowShelters.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shelterList.clear();
                Integer count=0;
                ShelterInformation[] shelterMark = new ShelterInformation[(int) dataSnapshot.getChildrenCount()];
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    //ShelterInformation sInfo = ds.getValue(ShelterInformation.class);
                    ShelterInformation sInfo = new ShelterInformation();
                    shelLat = (float)sInfo.setLatitude(ds.getValue(ShelterInformation.class).getLatitude());
                    shelLon = (float)sInfo.setLongitude(ds.getValue(ShelterInformation.class).getLongitude());
                    if(checkShelterWithinRange(shelLat, shelLon)) {
                        shelterMark[count] = sInfo;
                        //transferToMaps(sInfo);
                        count += 1;
                    }
                }
                transferToMaps(shelterMark);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void transferToMaps(ShelterInformation shelterMark[]) {
        Intent intent = new Intent(SearchShelterActivity.this, ShelterMarkerActivity.class);
        intent.putExtra("User_lat", mCurrentLocation.getLatitude());
        intent.putExtra("User_lon", mCurrentLocation.getLongitude());
        //intent.putExtra("Shelter_name", markShelter.getName().toString());
        //intent.putExtra("Longitude",shelterList.getLongitude());
        //intent.putExtra("Shelters",shelterMark);

        Bundle args = new Bundle();
        args.putSerializable("Shelters",shelterMark);
        intent.putExtra("BUNDLE",args);

        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sSelected = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, sSelected, Toast.LENGTH_SHORT).show();
        radius = Float.parseFloat(sSelected);
        gps_enabled = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        if (network_enabled) {
            locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
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
        if (dist <= radius)
            return true;
        else
            return false;
    }
}
