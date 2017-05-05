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
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchShelterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, LocationListener {

    Spinner spinner;
    LocationManager locationmanager;
    boolean gps_enabled, network_enabled;
    private double radius, curlat, curlon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shelter);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_item,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sSelected = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, sSelected, Toast.LENGTH_SHORT).show();
        radius = Double.parseDouble(sSelected);
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

    public class GetLatLon extends AsyncTask<Double, Integer, String> {
        @Override
        protected String doInBackground(Double... params) {
            // Creating service handler class instance
            URL url;
            String response = "";
            String requestURL = "http://ec2-52-42-201-174.us-west-2.compute.amazonaws.com/fetch_post.php?";
            //String requestURL = "http://10.0.0.64/phpmyadmin/PuppyFinder/fetch_post.php?";
            try {
                StringBuilder str = new StringBuilder();
                str.append("lat=" + params[0] + "&").append("long=" + params[1] + "&").append("radius=" + params[2]);

                requestURL = requestURL+str.toString();
                url = new URL(requestURL);
                HttpURLConnection myconnection = (HttpURLConnection) url.openConnection();
                myconnection.setReadTimeout(15000);
                myconnection.setConnectTimeout(15000);
                myconnection.setRequestMethod("GET");

                if (200 == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    line = br.readLine();
                    while (line != null) {
                        response += line;
                        line = br.readLine();
                        Log.d("check:", line);
                    }
                    br.close();
                }
                myconnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String jsonStr = response.toString();
            return jsonStr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);
            Intent intent = new Intent(SearchShelterActivity.this, ShelterMarkerActivity.class);
            intent.putExtra("response",jsonStr);
            SearchShelterActivity.this.startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onLocationChanged(Location location) {
        curlat = location.getLatitude();
        curlon = location.getLongitude();
        new GetLatLon().execute(curlat, curlon, radius);
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
}
