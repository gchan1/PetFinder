package com.example.group1.puppyfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PetSearch extends AppCompatActivity {
    private LinearLayout verticalLinearLayout, currentView, bigView;
    ScrollView scrollView;
    EditText addressEditText, nameEditText, breedEditText;



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

        bigView.addView(verticalLinearLayout);

        // Used later
        currentView = new LinearLayout(this);
        currentView.setOrientation(LinearLayout.VERTICAL);

        bigView.addView(currentView);

        addHeader(); // Add the header and header rows to the activity


    }

    public void addHeader(){

        //verticalLinearLayout.setPadding(10,10,10,10);
        // Title
        TextView textView = new TextView(this);
        textView.setText("Find Your Next Pet");
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


        LinearLayout tview = new LinearLayout(this);
        tview.setBackgroundColor(0xFF644242);
        tview.setOrientation(LinearLayout.HORIZONTAL);
        textView = new TextView(this);
        textView.setText("\t Age \t");
        textView.setTextColor(0xFF06BDCB);
        //textView.setBackgroundColor(0xFFFFFFFF);
        textView.setTextSize(18f);
        tview.addView(textView);
        addressEditText = new EditText(this);
        addressEditText.setTextColor(0xFD000000);
        addressEditText.setHint("\t Put Age Here \t");
        addressEditText.setHintTextColor(0xFFFFFFFF);
        tview.addView(addressEditText);

        horizontalLinearLayout.addView(tview);

        tview = new LinearLayout(this);
        tview.setOrientation(LinearLayout.HORIZONTAL);
        tview.setBackgroundColor(0xFF644242);
        textView = new TextView(this);
        textView.setText("\t Gender\t \t");
        textView.setTextColor(0xFF06BDCB);
        //textView.setBackgroundColor(0xFFFFFFFF);
        textView.setTextSize(18f);
        tview.addView(textView);
        nameEditText = new EditText(this);
        nameEditText.setTextColor(0xFFFFFFFF);
        nameEditText.setHint("\t m or f \t");
        nameEditText.setHintTextColor(0xFFFFFFFF);

        tview.addView(nameEditText);

        horizontalLinearLayout.addView(tview);

        tview = new LinearLayout(this);
        tview.setOrientation(LinearLayout.HORIZONTAL);
        tview.setBackgroundColor(0xFF644242);
        textView = new TextView(this);
        textView.setText("\t Breed\t \t");
        textView.setTextColor(0xFF06BDCB);
        //textView.setBackgroundColor(0xFFFFFFFF);
        textView.setTextSize(18f);
        tview.addView(textView);
        breedEditText = new EditText(this);
        breedEditText.setTextColor(0xFFFFFFFF);
        breedEditText.setHint("\t Which Breed? \t");
        breedEditText.setHintTextColor(0xFFFFFFFF);

        tview.addView(breedEditText);

        horizontalLinearLayout.addView(tview);


        verticalLinearLayout.addView(horizontalLinearLayout);

        // Adding Button and editText searches
        LinearLayout horizontalLinearLayout2 = new LinearLayout(this);
        horizontalLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLinearLayout2.setGravity(Gravity.LEFT);

        Button button = new Button(this);
        button.setText("Go!");
        setOnClick(button);
        //button.setOnClickListener(this);

        horizontalLinearLayout2.addView(button);
        verticalLinearLayout.addView(horizontalLinearLayout2);

    }

    private void setOnClick(final Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("breedtest", breed);
                Intent intent = new Intent(getBaseContext(), PetList.class);

                intent.putExtra("age", addressEditText.getText().toString());
                intent.putExtra("gender", nameEditText.getText().toString());
                intent.putExtra("breed", breedEditText.getText().toString());
                startActivity(intent);

            }
        });
    } // end setOnClick
}
