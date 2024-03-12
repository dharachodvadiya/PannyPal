package com.indie.apps.pannypal;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import com.google.gson.Gson;
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

public class ContactEntryActivity extends AppCompatActivity  implements View.OnClickListener {

    LinearLayout layoutContactName;

    ImageButton btnSave, btnBack;
    ImageButton btnReceive, btnSpent, btnSelectContact, btnAddPayment;

    TextView txtContactName,txtHeading;

    Spinner spPaymentType;
    EditText etAmount, etDesc;
    int currAmtType= -1;
    long currContactTypeId = -1;
    String currContactName;
    long currPaymentTypeId = -1;
    String currPaymentName;

    List<PaymentType> paymentTypes = new ArrayList<>();
    List<String> paymentTypeName = new ArrayList<>();

    ArrayAdapter<PaymentType> paymentAdapter;
    DbManager dbManager;

    ContactData currContactData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_entry);

        if(getIntent().hasExtra("selected_contact"))
        {
            String str = getIntent().getStringExtra("selected_contact");
            Gson gson = new Gson();
            currContactData = gson.fromJson(str, ContactData.class);

        }


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
        layoutContactName = findViewById(R.id.layoutContactName);
        txtHeading = findViewById(R.id.txtHeading);

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

        if(currContactData != null)
        {
            txtHeading.setText("Edit " + currContactData.getC_name() + "'s Entry");
            layoutContactName.setVisibility(View.GONE);

            selectAmountType(currContactData.getType());
            etAmount.setText(Globle.getValue(currContactData.getAmount()));

            if(!currContactData.getRemark().isEmpty())
            {
                etDesc.setText(currContactData.getRemark());
            }

            for (int i=0; i<paymentTypes.size(); i++ )
            {
                if(currContactData.getP_id() == paymentTypes.get(i).getId())
                {
                    spPaymentType.setSelection(i+1);
                    break;
                }
            }

        }else {
            txtHeading.setText("Add New Entry");
            layoutContactName.setVisibility(View.VISIBLE);
        }
    }

    void openContactSuggestionLayout()
    {
        DialogFragment dialogFragment = new DialogSearchContact(this, new IDilogCallback() {
            @Override
            public void onActionClick(String data) {
                Gson gson = new Gson();
                suggestContactData item = gson.fromJson(data,suggestContactData.class);
                currContactTypeId = item.getId();
                currContactName = item.getName();
                txtContactName.setText(item.getName());
            }

            @Override
            public void onCancelClick() {

            }
        });

        dialogFragment.show(getSupportFragmentManager(), "tag");
    }

    void openNewPaymentLayout()
    {
        DialogFragment dialogFragment = new DialogAddPayment(this, new IDilogCallback() {
            @Override
            public void onActionClick(String data) {
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

            @Override
            public void onCancelClick() {

            }
        });

        dialogFragment.show(getSupportFragmentManager(), "tag");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgbtnSave:
                if(saveData())
                {
                    if(currContactData == null)
                    {
                        Intent i = new Intent(ContactEntryActivity.this, HomeActivity.class);
                        startActivity(i);
                    }else {
                        Intent i = new Intent(ContactEntryActivity.this, ContactSelectedActivity.class);
                        Gson gson = new Gson();
                        i.putExtra("selected_item", gson.toJson(dbManager.get_ContactFromId(currContactData.getC_id())));
                        startActivity(i);
                    }

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
                //closeContactSuggestionLayout();
                break;

            case R.id.imgbtnNewContact:
                //hideKeyboard(this);
               // openNewContactLayout();
                break;

            case R.id.btnNewContactClose:
                /*closeNewContactLayout();
                hideKeyboard(this);
                openContactSuggestionLayout();*/
                break;

            case R.id.imgbtnSaveContact:
                /*hideKeyboard(this);
                Contacts contacts = saveNewContact();
                if(contacts != null)
                {
                    currContactTypeId = contacts.getId();
                    currContactName = contacts.getName();
                    txtContactName.setText(contacts.getName());
                    closeContactSuggestionLayout();
                    closeNewContactLayout();
                }*/
                break;

            case R.id.imgbtnAddPayment:
                hideKeyboard(this);
                openNewPaymentLayout();
                break;

            case R.id.btnNewPaymentClose:
               // closeNewPaymentLayout(false);
                break;

            case R.id.imgbtnSavePayment:
               /* if(saveNewPayment())
                {
                    closeNewPaymentLayout(true);
                }*/
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

    boolean saveData()
    {
        if (currContactTypeId == -1 && currContactData == null)
        {
            Toast.makeText(getApplicationContext(),"Please select Contact",Toast.LENGTH_LONG).show();
            return false;
        }else if(etAmount.getText().toString().trim().length() <=0)
        {
            Toast.makeText(getApplicationContext(),"Please enter Amount",Toast.LENGTH_LONG).show();
            return false;
        }else {

            if(currContactData == null)
            {
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
            }else {
                Double amount = Double.parseDouble(etAmount.getText().toString());
                String desc = etDesc.getText().toString().trim();
                ContactData contactData = new ContactData(
                        currContactData.getId(),
                        currContactData.getC_id(),
                        currPaymentTypeId,
                        currContactData.getC_name(),
                        currPaymentName,
                        currAmtType,
                        amount,
                        desc,
                        currContactData.getDateTime());

                dbManager.edit_ContactData(contactData,(currContactData.getType() == 1) ? currContactData.getAmount() : 0, (currContactData.getType() == -1) ? currContactData.getAmount() : 0 );

                Toast.makeText(getApplicationContext(),"Edit Contact Successfully",Toast.LENGTH_LONG).show();
            }


           return true;
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
        if(currContactData == null)
        {
            Intent i = new Intent(ContactEntryActivity.this, HomeActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }else {
            Intent i = new Intent(ContactEntryActivity.this, ContactSelectedActivity.class);
            Gson gson = new Gson();
            i.putExtra("selected_item", gson.toJson(dbManager.get_ContactFromId(currContactData.getC_id())));
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }

    }

}