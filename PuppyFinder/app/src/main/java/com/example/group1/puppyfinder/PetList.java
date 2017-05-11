package com.example.group1.puppyfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PetList extends AppCompatActivity {
    private LinearLayout verticalLinearLayout, currentView, bigView;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event);

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
        //Step-4: Add ValueEventListener to our database reference
    } // end onStart

}
