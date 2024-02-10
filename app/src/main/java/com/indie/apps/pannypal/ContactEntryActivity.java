package com.indie.apps.pannypal;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.indie.apps.pannypal.Adapter.SearchContactFromNewEntryAdapter;
import com.indie.apps.pannypal.Database.DbHelper;
import com.indie.apps.pannypal.Database.DbManager;
import com.indie.apps.pannypal.Model.ContactData;
import com.indie.apps.pannypal.Model.suggestContactData;
import com.indie.apps.pannypal.Thread.AsyncTaskExecutorService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ContactEntryActivity extends AppCompatActivity  implements View.OnClickListener{

    ImageButton btnSave, btnBack;
    ImageButton btnReceive, btnSpent, btnSelectContact;


    TextView txtContactName;
    SearchContactFromNewEntryAdapter searchContactFromNewEntryAdapter;
    Spinner spPaymentType;
    EditText etAmount, etDesc;
    int currAmtType= -1;
    long currContactTypeId = -1;
    long currPaymentTypeId = -1;
    DbManager dbManager;

    // search layout data
    RelativeLayout layoutContactSearch;
    RecyclerView rvSuggestContact;
    SearchView svContact ;
    ImageButton btnContactClose, btnNewContact;
    List<suggestContactData> suggestContactAllData = new ArrayList<>();
    List<suggestContactData> suggestContactadapterData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_entry);
        init();
        selectAmountType(currAmtType);
    }

    void init()
    {
        dbManager= new DbManager(ContactEntryActivity.this);
        dbManager.open();

        getOnBackPressedDispatcher().addCallback(this,callback);

        btnSave =findViewById(R.id.imgbtnSave);
        btnBack =findViewById(R.id.imgbtnBack);
        btnReceive = findViewById(R.id.imgbtnReceive);
        btnSpent =findViewById(R.id.imgbtnSpent);
        txtContactName =findViewById(R.id.txtContactName);
        btnSelectContact =findViewById(R.id.imgbtnSelectContact);
        spPaymentType = findViewById(R.id.spPaymentType);
        etAmount = findViewById(R.id.etAmount);
        etDesc = findViewById(R.id.etDesc);

        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnReceive.setOnClickListener(this);
        btnSpent.setOnClickListener(this);
        btnSelectContact.setOnClickListener(this);



        String[] courses = { "cash", "Bank"};
        ArrayAdapter ad = new ArrayAdapter(this,R.layout.spinner_item,courses);
        ad.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spPaymentType.setAdapter(ad);
        spPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currPaymentTypeId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // search layout data

        layoutContactSearch = findViewById(R.id.layoutContactSearch);
        svContact = findViewById(R.id.svContact);
        rvSuggestContact = findViewById(R.id.rvContact);
        btnContactClose = findViewById(R.id.btnContactClose);
        btnNewContact = findViewById(R.id.imgbtnNewContact);

        btnContactClose.setOnClickListener(this);
        btnNewContact.setOnClickListener(this);

        layoutContactSearch.setVisibility(View.GONE);

        rvSuggestContact.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvSuggestContact.setLayoutManager(layoutManager);

        searchContactFromNewEntryAdapter = new SearchContactFromNewEntryAdapter(suggestContactadapterData, new SearchContactFromNewEntryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(suggestContactData item) {
                currContactTypeId = item.getId();
                txtContactName.setText(item.getName());
                closeContactSuggestionLayout();
            }
        });
        rvSuggestContact.setAdapter(searchContactFromNewEntryAdapter);

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
                    suggestContactadapterData.clear();

                    for (int i= suggestContactAllData.size()-1 ;i>=0;i--)
                    {
                        if(suggestContactAllData.get(i).getName().toLowerCase().contains(text.toLowerCase()))
                        {
                            suggestContactadapterData.add(suggestContactAllData.get(i));
                        }
                    }

                    searchContactFromNewEntryAdapter.notifyDataSetChanged();

                }else {
                    suggestContactadapterData.clear();
                    suggestContactadapterData.addAll(suggestContactAllData);
                    searchContactFromNewEntryAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

    }

    void openContactSuggestionLayout()
    {
        layoutContactSearch.setVisibility(View.VISIBLE);
        svContact.setQuery(null, true);
        new loadContactSuggestionData().execute();
    }

    void closeContactSuggestionLayout()
    {
        layoutContactSearch.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgbtnSave:
                saveData();
                break;
            case R.id.imgbtnBack:
                backAction();
                break;
            case R.id.imgbtnReceive:
                selectAmountType(1);
                break;
            case R.id.imgbtnSpent:
                selectAmountType(-1);
                break;

            case R.id.imgbtnSelectContact:
               openContactSuggestionLayout();
                break;

            case R.id.btnContactClose:
                closeContactSuggestionLayout();
                break;

            case R.id.imgbtnNewContact:
                break;
        }
    }

    void saveData()
    {
        if (currContactTypeId == -1)
        {
            Toast.makeText(getApplicationContext(),"Please select Contact",Toast.LENGTH_LONG).show();
        }else if(etAmount.getText().toString().trim().length() <=0)
        {
            Toast.makeText(getApplicationContext(),"Please enter Amount",Toast.LENGTH_LONG).show();
        }else {
            Double amount = Double.parseDouble(etAmount.getText().toString());
            String desc = etDesc.getText().toString().trim();
            ContactData contactData = new ContactData(
                    currContactTypeId,
                    currPaymentTypeId,
                    currAmtType,
                    amount,
                    desc,
                    Calendar.getInstance().getTimeInMillis());

            dbManager.add_ContactData(contactData);

            Toast.makeText(getApplicationContext(),"Add Contact Successfully",Toast.LENGTH_LONG).show();

            Intent i = new Intent(ContactEntryActivity.this, HomeActivity.class);
            startActivity(i);
        }

    }

    void selectAmountType(int type)
    {
        //1 = credit,  -1= debit
        currAmtType = type;
        if(currAmtType == 1)
        {
            btnReceive.setBackground(getResources().getDrawable(R.drawable.add_entry_select_credit, getApplication().getTheme()));
            btnSpent.setBackground(getResources().getDrawable(R.drawable.add_entry_unselect_debit, getApplication().getTheme()));
        }else {
            btnReceive.setBackground(getResources().getDrawable(R.drawable.add_entry_unselect_credit, getApplication().getTheme()));
            btnSpent.setBackground(getResources().getDrawable(R.drawable.add_entry_select_debit, getApplication().getTheme()));
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
        Intent i = new Intent(ContactEntryActivity.this, HomeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    public class loadContactSuggestionData extends AsyncTaskExecutorService< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            // before start background execution
        }

        @Override
        protected Void doInBackground(Void params) {
            // perform background task load app and return a result based on need
            suggestContactAllData.clear();
            suggestContactadapterData.clear();
            suggestContactAllData =dbManager.get_ContactsNameList();

            suggestContactadapterData.addAll(suggestContactAllData);

            return params;
        }

        @Override
        protected void onPostExecute(Void result) {
            searchContactFromNewEntryAdapter.notifyDataSetChanged();
        }

    }
}