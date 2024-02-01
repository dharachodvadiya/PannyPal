package com.indie.apps.pannypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener{

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


        imgbtnHome.setOnClickListener(this);
        imgbtnContact.setOnClickListener(this);
        imgbtnCalculator.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgbtnContact:
                Intent i1 = new Intent(CalculatorActivity.this, ContactActivity.class);
                startActivity(i1);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                break;
            case R.id.imgbtnHome:
                Intent i = new Intent(CalculatorActivity.this, HomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                break;
            case R.id.imgbtnCalculator:
                break;

        }
    }
}