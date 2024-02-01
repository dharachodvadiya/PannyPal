package com.indie.apps.pannypal;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton imgbtnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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