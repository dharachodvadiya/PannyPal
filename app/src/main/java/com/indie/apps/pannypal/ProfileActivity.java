package com.indie.apps.pannypal;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton imgbtnBack;
    TextView txtName, txtTotal, txtCredit, txtDebit;

    LinearLayout layoutbg;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        txtName.setText(Globle.MyProfile.getName());
        txtTotal.setText(Globle.getFormattedValue(Globle.MyProfile.getTotalAmt()));
        txtCredit.setText(Globle.getFormattedValue(Globle.MyProfile.getCreditAmt()));
        txtDebit.setText(Globle.getFormattedValue(Globle.MyProfile.getDebitAmt()));

        if(Globle.MyProfile.getTotalAmt() >=0)
        {
            layoutbg.setBackgroundResource(R.drawable.profile_bg_credit);
            txtTotal.setTextColor(getResources().getColor(R.color.credit));
        }else {
            layoutbg.setBackgroundResource(R.drawable.profile_bg_debit);

            txtTotal.setTextColor(getResources().getColor(R.color.debit));
        }
    }

    void init()
    {
        layoutbg = findViewById(R.id.bg);

        txtName = findViewById(R.id.txtName);
        txtTotal = findViewById(R.id.txtTotalBalance);
        txtCredit = findViewById(R.id.txtTotalCredit);
        txtDebit = findViewById(R.id.txtTotalDebit);

        imgbtnBack = findViewById(R.id.imgbtnBack);
        imgbtnBack.setOnClickListener(this);

        getOnBackPressedDispatcher().addCallback(this,callback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgbtnBack:
                backAction();
                break;

        }
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            // Handle the back button event
            backAction();
        }
    };

    void backAction()
    {
        Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}