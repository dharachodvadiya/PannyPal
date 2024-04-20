package com.indie.apps.pannypal.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.indie.apps.pannypal.Adapter.HomeDataAdapter;
import com.indie.apps.pannypal.ContactEntryActivity;
import com.indie.apps.pannypal.Database.DbManager;
import com.indie.apps.pannypal.Globle;
import com.indie.apps.pannypal.Model.ContactData;
import com.indie.apps.pannypal.ProfileActivity;
import com.indie.apps.pannypal.R;
import com.indie.apps.pannypal.Thread.AsyncTaskExecutorService;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{

    // contact list
    RelativeLayout layoutTotalBg, layoutTotalBgLine,layoutContact;
    TextView txtTotalBalance;
    ImageButton imgbtnSearch;
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
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager= new DbManager(getContext());
        dbManager.open();

        init(view);

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
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycleviewData.setLayoutManager(layoutManager);

        contactDataList = dbManager.get_ContactData_List_DESC();
        homeDataAdapter = new HomeDataAdapter(getContext(),contactDataList,false, new HomeDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ContactData item) {

            }
        });
        recycleviewData.setAdapter(homeDataAdapter);

        visibleContactLayout();


        recycleviewDataSearch.setHasFixedSize(true);
        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycleviewDataSearch.setLayoutManager(layoutManager1);

        contactDataSearchList.addAll(contactDataList);
        homeDataSearchAdapter = new HomeDataAdapter(getContext(),contactDataSearchList,true, new HomeDataAdapter.OnItemClickListener() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    void init(View view)
    {
        // contact list

        txtTotalBalance = view.findViewById(R.id.txtTotalBalance);
        layoutTotalBg = view.findViewById(R.id.totalBg);
        layoutTotalBgLine = view.findViewById(R.id.totalBgLine);
        layoutContact = view.findViewById(R.id.layoutContact);

        imgbtnSearch = view.findViewById(R.id.imgbtnSearch);

        recycleviewData = view.findViewById(R.id.recycleviewData);

        imgbtnProfile = view.findViewById(R.id.imgbtnProfile);
        btnNewEntry = view.findViewById(R.id.btnNewEntry);

        imgbtnProfile.setOnClickListener(this);
        btnNewEntry.setOnClickListener(this);
        imgbtnSearch.setOnClickListener(this);

        //contact search

        btnBack = view.findViewById(R.id.btnBack);
        svContact = view.findViewById(R.id.svContact);
        recycleviewDataSearch = view.findViewById(R.id.recycleviewDataSearch);
        layoutContactSearch = view.findViewById(R.id.layoutContactSearch);

        btnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.imgbtnProfile:
                Intent i2 = new Intent(getContext(), ProfileActivity.class);
                startActivity(i2);
                break;
            case R.id.btnNewEntry:
                Intent i3 = new Intent(getContext(), ContactEntryActivity.class);
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