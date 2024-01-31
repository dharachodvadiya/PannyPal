package com.indie.apps.pannypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CalculatorActivity extends AppCompatActivity {

    ImageButton imgbtnContact, imgbtnHome, imgbtnCalculator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        imgbtnContact = findViewById(R.id.imgbtnContact);
        imgbtnHome = findViewById(R.id.imgbtnHome);
        imgbtnCalculator = findViewById(R.id.imgbtnCalculator);

        imgbtnHome.setSelected(false);
        imgbtnContact.setSelected(false);
        imgbtnCalculator.setSelected(true);


        imgbtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CalculatorActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
        imgbtnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CalculatorActivity.this, ContactActivity.class);
                startActivity(i);
            }
        });
    }
}