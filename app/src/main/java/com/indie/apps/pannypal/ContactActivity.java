package com.indie.apps.pannypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ContactActivity extends AppCompatActivity {

    ImageButton imgbtnContact, imgbtnHome, imgbtnCalculator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        imgbtnContact = findViewById(R.id.imgbtnContact);
        imgbtnHome = findViewById(R.id.imgbtnHome);
        imgbtnCalculator = findViewById(R.id.imgbtnCalculator);

        imgbtnHome.setSelected(false);
        imgbtnContact.setSelected(true);
        imgbtnCalculator.setSelected(false);


        imgbtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContactActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
        imgbtnCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContactActivity.this, CalculatorActivity.class);
                startActivity(i);
            }
        });
    }
}