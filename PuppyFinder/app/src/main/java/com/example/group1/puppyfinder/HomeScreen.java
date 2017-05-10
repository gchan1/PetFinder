package com.example.group1.puppyfinder;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    //private TextView mTextMessage;
    public ImageButton buttonShelters, buttonEvents, buttonMaps;
    public View topBar;
    public TableRow puppyRows;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_screen);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        topBar = (View) findViewById(R.id.topBar);
        puppyRows = (TableRow) findViewById(R.id.cardList);
        buttonShelters = (ImageButton) findViewById(R.id.buttonShelters);
        buttonEvents = (ImageButton) findViewById(R.id.buttonEvents);
        buttonMaps = (ImageButton) findViewById(R.id.buttonMap) ;
        buttonMaps.bringToFront();
        buttonEvents.bringToFront();
        buttonShelters.bringToFront();
        buttonShelters.setOnClickListener(this);
        buttonEvents.setOnClickListener(this);
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
        }
    }
}
