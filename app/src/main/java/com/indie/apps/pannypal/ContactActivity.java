package com.indie.apps.pannypal;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.indie.apps.pannypal.Adapter.ContactAdapter;
import com.indie.apps.pannypal.Adapter.SearchContactFromNewEntryAdapter;
import com.indie.apps.pannypal.Database.DbManager;
import com.indie.apps.pannypal.Model.Contacts;
import com.indie.apps.pannypal.Model.suggestContactData;
import com.indie.apps.pannypal.Thread.AsyncTaskExecutorService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    // contact list layout data
    RelativeLayout layoutSearch, layoutMultiSelect;
    ImageButton imgbtnContact, imgbtnHome, imgbtnCalculator;
    RecyclerView recycleviewData;
    SearchView svContact ;
    ImageButton  btnNewContact,btnBack, imgBtnEdit, imgBtnMultiDelete;
    DbManager dbManager;
    ContactAdapter contactAdapter;
    List<Contacts> suggestContactAllData = new ArrayList<>();
    List<Contacts> suggestContactadapterData = new ArrayList<>();

    List<Contacts> selectContactList = new ArrayList<>();

    TextView txtNoDataFound;

    //new contact
    RelativeLayout layoutAddContact;
    ImageButton btnNewContactClose,btnSaveNewContact;
    RelativeLayout layoutLimit,layoutLimitAnim;
    EditText etContactName, etPhno , etLimitAmt;
    CountryCodePicker codePicker;
    Switch switchLimit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        init();

    }

    void init()
    {
        dbManager= new DbManager(ContactActivity.this);
        dbManager.open();

        getOnBackPressedDispatcher().addCallback(this,callback);

        // contact list layout data

        imgbtnContact = findViewById(R.id.imgbtnContact);
        imgbtnHome = findViewById(R.id.imgbtnHome);
        imgbtnCalculator = findViewById(R.id.imgbtnCalculator);

        layoutMultiSelect = findViewById(R.id.layoutMultiselect);
        layoutSearch = findViewById(R.id.layoutSearch);

        recycleviewData = findViewById(R.id.recycleviewData);
        svContact = findViewById(R.id.svContact);
        btnNewContact = findViewById(R.id.imgbtnNewContact);
        btnBack = findViewById(R.id.btnBack);
        imgBtnEdit = findViewById(R.id.imgBtnEdit);
        imgBtnMultiDelete = findViewById(R.id.imgBtnMultiDelete);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);

        imgbtnHome.setSelected(false);
        imgbtnContact.setSelected(true);
        imgbtnCalculator.setSelected(false);

        imgbtnHome.setOnClickListener(this);
        imgbtnContact.setOnClickListener(this);
        imgbtnCalculator.setOnClickListener(this);
        btnNewContact.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        imgBtnEdit.setOnClickListener(this);
        imgBtnMultiDelete.setOnClickListener(this);

        recycleviewData.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycleviewData.setLayoutManager(layoutManager);

        contactAdapter = new ContactAdapter(this,this,suggestContactadapterData, txtNoDataFound,new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contacts item) {
                Intent i = new Intent(ContactActivity.this, ContactSelectedActivity.class);
                startActivity(i);
            }

            @Override
            public void OnItemLongClickAdd(Contacts item) {
                selectContactList.add(item);
                openMultiselectLayout();
            }

            @Override
            public void OnItemLongClickRemove(Contacts item) {
                selectContactList.remove(item);

                if(selectContactList.size() == 0)
                {
                    contactAdapter.setSelected(false);
                    openSearchLayout();
                }else {
                    openMultiselectLayout();
                }
            }

        });
        recycleviewData.setAdapter(contactAdapter);

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

                    contactAdapter.dataChange();

                }else {
                    suggestContactadapterData.clear();
                    suggestContactadapterData.addAll(suggestContactAllData);
                    contactAdapter.dataChange();
                }
                return false;
            }
        });

        //new contact

        layoutAddContact = findViewById(R.id.layoutAddContact);
        btnNewContactClose = findViewById(R.id.btnNewContactClose);
        btnSaveNewContact = findViewById(R.id.imgbtnSaveContact);

        etContactName = findViewById(R.id.etContactName);
        etPhno = findViewById(R.id.etContactNumber);
        switchLimit = findViewById(R.id.switchLimit);
        etLimitAmt = findViewById(R.id.etLimitAmt);
        layoutLimit = findViewById(R.id.layoutLimit);
        layoutLimitAnim = findViewById(R.id.layoutLimitAnim);
        codePicker=findViewById(R.id.country_code);

        btnNewContactClose.setOnClickListener(this);
        btnSaveNewContact.setOnClickListener(this);
        switchLimit.setOnCheckedChangeListener(this);

        openContactListLayout(false,null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgbtnContact:
                break;
            case R.id.imgbtnHome:
                Intent i = new Intent(ContactActivity.this, HomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.imgbtnCalculator:
                Intent i1 = new Intent(ContactActivity.this, CalculatorActivity.class);
                startActivity(i1);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.imgbtnNewContact:
                hideKeyboard(this);
                svContact.setQuery(null,true);
                openNewContactLayout();
                break;

            case R.id.btnNewContactClose:
                closeNewContactLayout();
                hideKeyboard(this);
                break;

            case R.id.imgbtnSaveContact:
                hideKeyboard(this);
                Contacts contacts = saveNewContact();
                if(contacts != null)
                {
                    closeNewContactLayout();
                    openContactListLayout(true,contacts);
                }
                break;
            case R.id.btnBack:
                hideKeyboard(this);
                backAction();
                break;

            case R.id.imgBtnEdit:
                break;

            case R.id.imgBtnMultiDelete:
                int count = selectContactList.size();

                if(dbManager.delete_ContactFromIds(selectContactList) >0)
                {
                    for (int j = 0; j< count ;j++)
                    {
                        suggestContactadapterData.remove(selectContactList.get(j));
                    }
                    selectContactList.clear();
                    contactAdapter.setSelected(false);
                    openSearchLayout();
                   // contactAdapter.notifyDataSetChanged();

                }
                break;
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

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
            suggestContactAllData =dbManager.get_Contacts();

            suggestContactadapterData.addAll(suggestContactAllData);

            return params;
        }

        @Override
        protected void onPostExecute(Void result) {
            contactAdapter.dataChange();
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
        if(!svContact.getQuery().toString().isEmpty())
        {
            svContact.setQuery(null,true);
        }else if(layoutMultiSelect.getVisibility() == View.VISIBLE)
        {
            selectContactList.clear();
            contactAdapter.setSelected(false);
            openSearchLayout();
        }else  if(layoutAddContact.getVisibility() == View.VISIBLE)
        {
            closeNewContactLayout();
        }
    }

    void openContactListLayout(boolean isComeFromNewData, Contacts contacts)
    {
        openSearchLayout();
        layoutAddContact.setVisibility(View.GONE);
        svContact.setQuery(null, true);

        if(isComeFromNewData)
        {
            suggestContactAllData.add(0,contacts);
            suggestContactadapterData.add(0,contacts);

            contactAdapter.itemInsertDataChange(0);
        }else {
            new loadContactSuggestionData().execute();
        }

    }

    void openNewContactLayout()
    {
        layoutAddContact.setVisibility(View.VISIBLE);

        etContactName.setText("");
        etPhno.setText("");
        switchLimit.setChecked(false);
    }

    void closeNewContactLayout()
    {
        layoutAddContact.setVisibility(View.GONE);
    }

    Contacts saveNewContact()
    {
        if(etContactName.getText().toString().trim().length() <=0)
        {
            Toast.makeText(getApplicationContext(),"Please enter Name",Toast.LENGTH_LONG).show();
            return  null;
        }else if(!etPhno.getText().toString().isEmpty() && !Globle.isValidPhoneNumber(codePicker.getSelectedCountryNameCode(),etPhno.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Please enter Valid Contact Number",Toast.LENGTH_LONG).show();
            return null;
        }else if(switchLimit.isChecked() && etLimitAmt.getText().toString().trim().length() <=0)
        {
            Toast.makeText(getApplicationContext(),"Please enter Limit Amount",Toast.LENGTH_LONG).show();
            return  null;
        }
        else
        {
            String name = etContactName.getText().toString().trim();
            if(dbManager.get_ContactsFromName(name) == -1)
            {
                Double limitAmount = 0.0;
                int isLimit = switchLimit.isChecked()? 1 :0;
                if(isLimit == 1)
                {
                    limitAmount = Double.parseDouble(etLimitAmt.getText().toString());
                }
                String phno="";
                if(!etPhno.getText().toString().isEmpty() )
                {
                    phno = codePicker.getSelectedCountryCode() + " " +etPhno.getText().toString().trim();
                }
                Contacts contactData = new Contacts(
                        name,
                        phno,
                        isLimit,
                        limitAmount,
                        null,
                        0.0,
                        0.0,
                        Calendar.getInstance().getTimeInMillis());

                contactData.setId(dbManager.add_Contacts(contactData));

                Toast.makeText(getApplicationContext(),"Add new Contact Successfully",Toast.LENGTH_LONG).show();
                return  contactData;
            }else {
                Toast.makeText(getApplicationContext(),"Contact Name Already Exist",Toast.LENGTH_LONG).show();
                return  null;
            }

        }
    }

    void  openSearchLayout()
    {
        layoutMultiSelect.setVisibility(View.GONE);
        layoutSearch.setVisibility(View.VISIBLE);
    }

    void openMultiselectLayout()
    {
        layoutMultiSelect.setVisibility(View.VISIBLE);
        layoutSearch.setVisibility(View.GONE);

        if(selectContactList.size() == 1)
        {
            //show edit button
            imgBtnEdit.setVisibility(View.VISIBLE);
        }else {
            //hide edit button
            imgBtnEdit.setVisibility(View.GONE);
        }
    }

    public boolean isLongClickAvailable()
    {
        return svContact.getQuery().toString().isEmpty();
    }
}