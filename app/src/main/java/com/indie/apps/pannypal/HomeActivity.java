package com.indie.apps.pannypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener
{

    RelativeLayout layoutTotalBg, layoutTotalBgLine;

    TextView txtTotalBalance;
    ImageButton imgbtnContact, imgbtnHome, imgbtnCalculator;
    ImageButton imgbtnProfile;

    Button btnNewEntry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        txtTotalBalance.setText(Globle.getFormattedValue(Globle.MyProfile.getTotalAmt()));
        if(Globle.MyProfile.getTotalAmt() >=0)
        {
            layoutTotalBg.setBackgroundResource(R.drawable.home_totalbg_gradient_credit);
            layoutTotalBgLine.setBackgroundResource(R.drawable.home_totalbg_credit);
        }else {
            layoutTotalBg.setBackgroundResource(R.drawable.home_totalbg_gradient_debit);
            layoutTotalBgLine.setBackgroundResource(R.drawable.home_totalbg_debit);
        }
    }

    void init()
    {
        txtTotalBalance = findViewById(R.id.txtTotalBalance);
        layoutTotalBg = findViewById(R.id.totalBg);
        layoutTotalBgLine = findViewById(R.id.totalBgLine);

        imgbtnContact = findViewById(R.id.imgbtnContact);
        imgbtnHome = findViewById(R.id.imgbtnHome);
        imgbtnCalculator = findViewById(R.id.imgbtnCalculator);

        imgbtnHome.setSelected(true);
        imgbtnContact.setSelected(false);
        imgbtnCalculator.setSelected(false);

        imgbtnProfile = findViewById(R.id.imgbtnProfile);
        btnNewEntry = findViewById(R.id.btnNewEntry);

        imgbtnContact.setOnClickListener(this);
        imgbtnHome.setOnClickListener(this);
        imgbtnCalculator.setOnClickListener(this);
        imgbtnProfile.setOnClickListener(this);
        btnNewEntry.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgbtnContact:
                Intent i = new Intent(HomeActivity.this, ContactActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                break;
            case R.id.imgbtnHome:
                break;
            case R.id.imgbtnCalculator:
                Intent i1 = new Intent(HomeActivity.this, CalculatorActivity.class);
                startActivity(i1);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.imgbtnProfile:
                Intent i2 = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(i2);
                break;
            case R.id.btnNewEntry:
                Intent i3 = new Intent(HomeActivity.this, ContactEntryActivity.class);
                startActivity(i3);
                break;
        }
    }
}