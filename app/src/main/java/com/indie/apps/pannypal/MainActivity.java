package com.indie.apps.pannypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnGuestLogin, btnGoogleLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGuestLogin = findViewById(R.id.btn_login_guest);
        btnGoogleLogin = findViewById(R.id.btn_login_google);

        btnGuestLogin.setOnClickListener(this);
        btnGoogleLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_login_guest:
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(i);
                break;
            case R.id.btn_login_google:
                break;

        }
    }
}