package com.indie.apps.pannypal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.indie.apps.pannypal.Adapter.HomeDataAdapter;
import com.indie.apps.pannypal.Database.DbManager;
import com.indie.apps.pannypal.Model.ContactData;
import com.indie.apps.pannypal.Thread.AsyncTaskExecutorService;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener
{

    // contact list
    RelativeLayout layoutTotalBg, layoutTotalBgLine,layoutContact;
    TextView txtTotalBalance;
    ImageButton imgbtnContact, imgbtnHome, imgbtnCalculator, imgbtnSearch;
    ImageButton imgbtnProfile;
    Button btnNewEntry;
    HomeDataAdapter homeDataAdapter;
    List<ContactData> contactDataList;
    RecyclerView recycleviewData;
    DbManager dbManager;

    //contact search
    ImageButton btnBack;
    SearchView svContact;
    RecyclerView recycleviewDataSearch;
    RelativeLayout layoutContactSearch;

    HomeDataAdapter homeDataSearchAdapter;
    List<ContactData> contactDataSearchList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbManager= new DbManager(HomeActivity.this);
        dbManager.open();

        init();

        double total = Globle.MyProfile.getCreditAmt()-Globle.MyProfile.getDebitAmt();
        txtTotalBalance.setText(Globle.getFormattedValue(total));
        if(total >=0)
        {
            layoutTotalBg.setBackgroundResource(R.drawable.home_totalbg_gradient_credit);
            layoutTotalBgLine.setBackgroundResource(R.drawable.home_totalbg_credit);
        }else {
            layoutTotalBg.setBackgroundResource(R.drawable.home_totalbg_gradient_debit);
            layoutTotalBgLine.setBackgroundResource(R.drawable.home_totalbg_debit);
        }

        recycleviewData.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycleviewData.setLayoutManager(layoutManager);

        contactDataList = dbManager.get_ContactData_List_DESC();
        homeDataAdapter = new HomeDataAdapter(getApplicationContext(),contactDataList,false, new HomeDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ContactData item) {

            }
        });
        recycleviewData.setAdapter(homeDataAdapter);

        visibleContactLayout();


        recycleviewDataSearch.setHasFixedSize(true);
        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycleviewDataSearch.setLayoutManager(layoutManager1);

        contactDataSearchList.addAll(contactDataList);
        homeDataSearchAdapter = new HomeDataAdapter(getApplicationContext(),contactDataSearchList,true, new HomeDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ContactData item) {

            }
        });
        recycleviewDataSearch.setAdapter(homeDataSearchAdapter);

        svContact.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                String text = s.toString();

                if(!text.isEmpty())
                {
                    contactDataSearchList.clear();

                    for (int i= contactDataList.size()-1 ;i>=0;i--)
                    {
                        if(contactDataList.get(i).getC_name().toLowerCase().contains(text.toLowerCase()))
                        {
                            contactDataSearchList.add(contactDataList.get(i));
                        }
                    }

                    homeDataSearchAdapter.notifyDataSetChanged();

                }else {
                    contactDataSearchList.clear();
                    contactDataSearchList.addAll(contactDataList);
                    homeDataSearchAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    void init()
    {
        // contact list

        txtTotalBalance = findViewById(R.id.txtTotalBalance);
        layoutTotalBg = findViewById(R.id.totalBg);
        layoutTotalBgLine = findViewById(R.id.totalBgLine);
        layoutContact = findViewById(R.id.layoutContact);

        imgbtnSearch = findViewById(R.id.imgbtnSearch);

        imgbtnContact = findViewById(R.id.imgbtnContact);
        imgbtnHome = findViewById(R.id.imgbtnHome);
        imgbtnCalculator = findViewById(R.id.imgbtnCalculator);

        recycleviewData = findViewById(R.id.recycleviewData);

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
        imgbtnSearch.setOnClickListener(this);

        //contact search

        btnBack = findViewById(R.id.btnBack);
        svContact = findViewById(R.id.svContact);
        recycleviewDataSearch = findViewById(R.id.recycleviewDataSearch);
        layoutContactSearch = findViewById(R.id.layoutContactSearch);

        btnBack.setOnClickListener(this);

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
            case R.id.btnBack:
                visibleContactLayout();
                break;
            case  R.id.imgbtnSearch:
                visibleContactSearchLayout();
                break;
        }
    }

    void  visibleContactLayout()
    {
        layoutContact.setVisibility(View.VISIBLE);
        layoutContactSearch.setVisibility(View.GONE);
    }

    void  visibleContactSearchLayout()
    {
        layoutContact.setVisibility(View.GONE);
        layoutContactSearch.setVisibility(View.VISIBLE);
        svContact.setQuery(null, true);
        new loadContactSuggestionData().execute();
    }

    public class loadContactSuggestionData extends AsyncTaskExecutorService< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            // before start background execution
        }

        @Override
        protected Void doInBackground(Void params) {
            // perform background task load app and return a result based on need
            contactDataSearchList.clear();

            contactDataSearchList.addAll(contactDataList);

            return params;
        }

        @Override
        protected void onPostExecute(Void result) {
            homeDataSearchAdapter.notifyDataSetChanged();
        }

    }
}