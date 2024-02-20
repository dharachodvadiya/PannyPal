package com.indie.apps.pannypal;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.indie.apps.pannypal.Adapter.SearchContactFromNewEntryAdapter;
import com.indie.apps.pannypal.Database.DbHelper;
import com.indie.apps.pannypal.Database.DbManager;
import com.indie.apps.pannypal.Model.ContactData;
import com.indie.apps.pannypal.Model.Contacts;
import com.indie.apps.pannypal.Model.PaymentType;
import com.indie.apps.pannypal.Model.suggestContactData;
import com.indie.apps.pannypal.Thread.AsyncTaskExecutorService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ContactEntryActivity extends AppCompatActivity  implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageButton btnSave, btnBack;
    ImageButton btnReceive, btnSpent, btnSelectContact, btnAddPayment;


    TextView txtContactName;
    SearchContactFromNewEntryAdapter searchContactFromNewEntryAdapter;
    Spinner spPaymentType;
    EditText etAmount, etDesc;
    int currAmtType= -1;
    long currContactTypeId = -1;
    String currContactName;
    long currPaymentTypeId = -1;
    String currPaymentName;
    DbManager dbManager;

    // search layout data
    RelativeLayout layoutContactSearch;
    RecyclerView rvSuggestContact;
    SearchView svContact ;
    ImageButton btnContactClose, btnNewContact;
    List<suggestContactData> suggestContactAllData = new ArrayList<>();
    List<suggestContactData> suggestContactadapterData = new ArrayList<>();

    List<PaymentType> paymentTypes = new ArrayList<>();
    List<String> paymentTypeName = new ArrayList<>();

    ArrayAdapter<PaymentType> paymentAdapter;

    // new Contact
    RelativeLayout layoutAddContact;
    ImageButton btnNewContactClose,btnSaveNewContact;
    RelativeLayout layoutLimit,layoutLimitAnim;
    EditText etContactName, etPhno , etLimitAmt;
    CountryCodePicker codePicker;
    Switch switchLimit;

    // new Payment
    RelativeLayout layoutAddPayment;
    ImageButton btnNewPaymentClose,btnSaveNewPayment;
    EditText etPaymentName;

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
        btnAddPayment =findViewById(R.id.imgbtnAddPayment);
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
        btnAddPayment.setOnClickListener(this);

        paymentTypeName.clear();
        paymentTypeName.add("None");
        paymentTypes = dbManager.get_PaymentType();
        int count = paymentTypes.size();
        for (int i= 0; i< count; i++)
        {
            paymentTypeName.add(paymentTypes.get(i).getType());
        }

        paymentAdapter = new ArrayAdapter(this,R.layout.spinner_item,paymentTypeName);
        paymentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spPaymentType.setAdapter(paymentAdapter);
        spPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==0)
                {
                    currPaymentTypeId = -1;
                    currPaymentName = "";
                }else {
                    currPaymentTypeId = paymentTypes.get(i-1).getId();
                    currPaymentName = paymentTypes.get(i-1).getType();
                }

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
                currContactName = item.getName();
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


        //new payment
        layoutAddPayment = findViewById(R.id.layoutAddPayment);
        btnNewPaymentClose = findViewById(R.id.btnNewPaymentClose);
        btnSaveNewPayment = findViewById(R.id.imgbtnSavePayment);
        etPaymentName = findViewById(R.id.etContactType);

        btnNewPaymentClose.setOnClickListener(this);
        btnSaveNewPayment.setOnClickListener(this);

    }

    void openContactSuggestionLayout()
    {
        layoutContactSearch.setVisibility(View.VISIBLE);
        layoutAddContact.setVisibility(View.GONE);
        layoutAddPayment.setVisibility(View.GONE);
        svContact.setQuery(null, true);
        new loadContactSuggestionData().execute();
    }

    void closeContactSuggestionLayout()
    {
        layoutContactSearch.setVisibility(View.GONE);
    }

    void openNewContactLayout()
    {
        layoutContactSearch.setVisibility(View.GONE);
        layoutAddContact.setVisibility(View.VISIBLE);
        layoutAddPayment.setVisibility(View.GONE);

        etContactName.setText("");
        etPhno.setText("");
        switchLimit.setChecked(false);
    }

    void closeNewContactLayout()
    {
        layoutAddContact.setVisibility(View.GONE);
    }

    void openNewPaymentLayout()
    {
        layoutContactSearch.setVisibility(View.GONE);
        layoutAddContact.setVisibility(View.GONE);
        layoutAddPayment.setVisibility(View.VISIBLE);

        etPaymentName.setText("");
    }

    void closeNewPaymentLayout(boolean isNewData)
    {
        layoutAddPayment.setVisibility(View.GONE);
        if(isNewData)
        {
            paymentTypeName.clear();
            paymentTypeName.add("None");
            paymentTypes = dbManager.get_PaymentType();
            int count = paymentTypes.size();
            for (int i= 0; i< count; i++)
            {
                paymentTypeName.add(paymentTypes.get(i).getType());
            }
            paymentAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgbtnSave:
                if(saveData())
                {
                    Intent i = new Intent(ContactEntryActivity.this, HomeActivity.class);
                    startActivity(i);
                }
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
                hideKeyboard(this);
               openContactSuggestionLayout();
                break;

            case R.id.btnContactClose:
                closeContactSuggestionLayout();
                break;

            case R.id.imgbtnNewContact:
                hideKeyboard(this);
                openNewContactLayout();
                break;

            case R.id.btnNewContactClose:
                closeNewContactLayout();
                hideKeyboard(this);
                openContactSuggestionLayout();
                break;

            case R.id.imgbtnSaveContact:
                Contacts contacts = saveNewContact();
                if(contacts != null)
                {
                    currContactTypeId = contacts.getId();
                    currContactName = contacts.getName();
                    txtContactName.setText(contacts.getName());
                    closeContactSuggestionLayout();
                    closeNewContactLayout();
                }
                break;

            case R.id.imgbtnAddPayment:
                hideKeyboard(this);
                openNewPaymentLayout();
                break;

            case R.id.btnNewPaymentClose:
                closeNewPaymentLayout(false);
                break;

            case R.id.imgbtnSavePayment:
                if(saveNewPayment())
                {
                    closeNewPaymentLayout(true);
                }
                break;

        }
    }

    // Close keyboard. Wont work if an EditText has focus.
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

        if(b)
        {

            //layoutLimit.setBackground(null);
            //layoutLimitAnim.setBackground(getResources().getDrawable(R.drawable.dilog_field_bg_9,getApplication().getTheme()));

            etLimitAmt.setVisibility(View.VISIBLE);
            ScaleAnimation scaleAnimation = new ScaleAnimation(1,1,0,1,1,0);
            scaleAnimation.setDuration(300);
            scaleAnimation.setFillAfter(true);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //layoutLimit.setBackground(getResources().getDrawable(R.drawable.dilog_field_bg_9,getApplication().getTheme()));
                    //layoutLimitAnim.setBackground(null);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            etLimitAmt.startAnimation(scaleAnimation);

            ScaleAnimation scaleAnimation1 = new ScaleAnimation(1,1,0.5f,1,1,1);
            scaleAnimation1.setDuration(300);
            scaleAnimation1.setFillAfter(true);

            layoutLimitAnim.startAnimation(scaleAnimation1);

        }else {
            //layoutLimit.setBackground(null);
           // layoutLimitAnim.setBackground(getResources().getDrawable(R.drawable.dilog_field_bg_9,getApplication().getTheme()));

            ScaleAnimation scaleAnimation = new ScaleAnimation(1,1,1,0,1,0);
            scaleAnimation.setDuration(300);
            scaleAnimation.setFillAfter(true);

            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    etLimitAmt.setVisibility(View.GONE);
                    //layoutLimit.setBackground(getResources().getDrawable(R.drawable.dilog_field_bg_9,getApplication().getTheme()));
                   // layoutLimitAnim.setBackground(null);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            etLimitAmt.startAnimation(scaleAnimation);



            ScaleAnimation scaleAnimation1 = new ScaleAnimation(1,1,1,0.5f,1,1);
            scaleAnimation1.setDuration(300);
            scaleAnimation1.setFillAfter(true);

            layoutLimitAnim.startAnimation(scaleAnimation1);
        }

    }

    boolean saveData()
    {
        if (currContactTypeId == -1)
        {
            Toast.makeText(getApplicationContext(),"Please select Contact",Toast.LENGTH_LONG).show();
            return false;
        }else if(etAmount.getText().toString().trim().length() <=0)
        {
            Toast.makeText(getApplicationContext(),"Please enter Amount",Toast.LENGTH_LONG).show();
            return false;
        }else {

            Double amount = Double.parseDouble(etAmount.getText().toString());
            String desc = etDesc.getText().toString().trim();
            ContactData contactData = new ContactData(
                    currContactTypeId,
                    currPaymentTypeId,
                    currContactName,
                    currPaymentName,
                    currAmtType,
                    amount,
                    desc,
                    Calendar.getInstance().getTimeInMillis());

            dbManager.add_ContactData(contactData);

            Toast.makeText(getApplicationContext(),"Add Contact Successfully",Toast.LENGTH_LONG).show();

           return true;
        }

    }

    Contacts saveNewContact()
    {
        if(etContactName.getText().toString().trim().length() <=0)
        {
            Toast.makeText(getApplicationContext(),"Please enter Name",Toast.LENGTH_LONG).show();
            return  null;
        }else if(!Globle.isValidPhoneNumber(codePicker.getSelectedCountryNameCode(),etPhno.getText().toString()))
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

                String phno = codePicker.getSelectedCountryCode() + " " +etPhno.getText().toString().trim();
                Contacts contactData = new Contacts(
                        name,
                        phno,
                        isLimit,
                        limitAmount,
                        null,
                        0.0,
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

    boolean  saveNewPayment()
    {
        if(etPaymentName.getText().toString().trim().length() <=0)
        {
            Toast.makeText(getApplicationContext(),"Please enter Payment Type",Toast.LENGTH_LONG).show();
            return  false;
        }else if(dbManager.get_PaymentFromType(etPaymentName.getText().toString().trim()) != -1)
        {
            Toast.makeText(getApplicationContext(),"Payment type  already exist",Toast.LENGTH_LONG).show();
            return  false;
        }else {
            String name = etPaymentName.getText().toString().trim();
            PaymentType data = new PaymentType(
                    name);

            dbManager.add_PaymentType(data);

            Toast.makeText(getApplicationContext(),"Add new Payment Successfully",Toast.LENGTH_LONG).show();
            return  true;
        }
    }

    void selectAmountType(int type)
    {
        currAmtType = type;
        //1 = credit,  -1= debit
        if(currAmtType == 1)
        {
            btnReceive.setImageResource(R.drawable.add_entry_select_credit);
            btnSpent.setImageResource(R.drawable.add_entry_unselect_debit);
        }else {
            btnReceive.setImageResource(R.drawable.add_entry_unselect_credit);
            btnSpent.setImageResource(R.drawable.add_entry_select_debit);
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
        if(layoutContactSearch.getVisibility() == View.VISIBLE)
        {
            closeContactSuggestionLayout();
        }else if(layoutAddContact.getVisibility() == View.VISIBLE)
        {
            closeNewContactLayout();
        }else if(layoutAddPayment.getVisibility() == View.VISIBLE)
        {
            closeNewPaymentLayout(false);
        }else {
            Intent i = new Intent(ContactEntryActivity.this, HomeActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }
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

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);
        // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
}