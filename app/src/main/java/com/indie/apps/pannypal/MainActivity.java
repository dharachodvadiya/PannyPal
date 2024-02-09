package com.indie.apps.pannypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.indie.apps.pannypal.Database.DbHelper;
import com.indie.apps.pannypal.Database.DbManager;
import com.indie.apps.pannypal.Model.Contacts;
import com.indie.apps.pannypal.Model.UserProfile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnGuestLogin, btnGoogleLogin;
    DbManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager= new DbManager(MainActivity.this);
        dbManager.open();

        /*dbManager.add_Contacts(new Contacts("aaa", "",0,0.0,"",0.0,0.0,0.0,0));
        dbManager.add_Contacts(new Contacts("bbb", "",0,0.0,"",0.0,0.0,0.0,0));
        dbManager.add_Contacts(new Contacts("bb", "",0,0.0,"",0.0,0.0,0.0,0));
        dbManager.add_Contacts(new Contacts("ccc", "",0,0.0,"",0.0,0.0,0.0,0));
        */init();


    }

    void init()
    {
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


                Globle.MyProfile = null;

                if(dbManager.isTableExists(DbHelper.TBL_USERPROFILE))
                {
                    Globle.MyProfile = dbManager.get_UserProfile();
                }

                if(Globle.MyProfile == null)
                {
                    Globle.MyProfile = new UserProfile("guest", null, null,0.0,0.0,0.0);
                    dbManager.add_UserProfile(Globle.MyProfile);
                }

                dbManager.close();
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(i);
                break;
            case R.id.btn_login_google:
                break;

        }
    }
}