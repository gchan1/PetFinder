package com.example.group1.puppyfinder;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ShelterMarkerActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int SHELTER_ACTIVITY = 1;
    private static final int EVENT_ACTIVITY = 2;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_marker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("lastActivity")) {
                if(bundle.getInt("lastActivity") == SHELTER_ACTIVITY){
                    // Adding in a marker with shelter info
                    Float lat = bundle.getFloat("lat");
                    Float lon = bundle.getFloat("lon");
                    String name = bundle.getString("name");
                    String address = bundle.getString("address");
                    Long number = bundle.getLong("number");
                    LatLng position = new LatLng(lat, lon);
                    mMap.addMarker(new MarkerOptions().position(position).title(name).snippet(address + "\n" + number.toString()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
                else if(bundle.getInt("lastActivity") == EVENT_ACTIVITY){
                    Toast.makeText(this, "in Event!", Toast.LENGTH_SHORT).show();
                    // Adding in a marker with shelter info
                    Float lat = bundle.getFloat("lat");
                    Float lon = bundle.getFloat("lon");
                    String name = bundle.getString("name");
                    String address = bundle.getString("address");
                    LatLng position = new LatLng(lat, lon);
                    mMap.addMarker(new MarkerOptions().position(position).title(name).snippet(address));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    Toast.makeText(this, "Zooming", Toast.LENGTH_SHORT).show();
                }

            }
        }

        else {
            String result = getIntent().getStringExtra("response");
            try {
                //List<LatLng> latLngList = new ArrayList<>();
                JSONArray jsonArr = new JSONArray(result);
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject c = jsonArr.getJSONObject(i);
                    double shel_lat = c.getDouble("lat");
                    double shel_lon = c.getDouble("lon");
                    String name = c.getString("name");
                    LatLng position = new LatLng(shel_lat, shel_lon);
                    mMap.addMarker(new MarkerOptions().position(position).title(name));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
