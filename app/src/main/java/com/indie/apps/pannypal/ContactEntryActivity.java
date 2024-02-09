package com.indie.apps.pannypal;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.indie.apps.pannypal.Database.DbHelper;
import com.indie.apps.pannypal.Database.DbManager;
import com.indie.apps.pannypal.Model.ContactData;

import java.util.Calendar;

public class ContactEntryActivity extends AppCompatActivity  implements View.OnClickListener{

    Button btnSave, btnBack, btnReceive, btnSpent;

    SearchView svContact ;
    Spinner spPaymentType;
    EditText etAmount, etDesc;
    int currAmtType= -1;
    int currContactTypeId = -1;
    int currPaymentTypeId = -1;

    SimpleCursorAdapter cursorAdapter;
    DbManager dbManager;
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

        btnSave = (Button) findViewById(R.id.btnSave);
        btnBack = (Button) findViewById(R.id.btnCancel);
        btnReceive = (Button) findViewById(R.id.btnReceive);
        btnSpent = (Button) findViewById(R.id.btnSpent);
        svContact = findViewById(R.id.svContact);
        spPaymentType = findViewById(R.id.spPaymentType);
        etAmount = findViewById(R.id.etAmount);
        etDesc = findViewById(R.id.etDesc);

        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnReceive.setOnClickListener(this);
        btnSpent.setOnClickListener(this);

        getOnBackPressedDispatcher().addCallback(this,callback);


        final String[] from = new String[] {DbHelper.C_NAME};
        final int[] to = new int[] {R.id.txtName};

        cursorAdapter = new SimpleCursorAdapter(ContactEntryActivity.this,R.layout.item_suggestion_contact_entry,dbManager.get_Contacts_suggestion(""),from, to,0);
        svContact.setSuggestionsAdapter(cursorAdapter);
        AutoCompleteTextView textview = (AutoCompleteTextView) svContact.findViewById(getResources().getIdentifier("android:id/search_src_text", null, null));
        ImageView close_btn = (ImageView) svContact.findViewById(getResources().getIdentifier("android:id/search_close_btn", null, null));
        textview.setThreshold(0);
        svContact.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                Log.d("aaa" , "onSuggestionSelect");


                return true;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                Log.d("aaa" , "onSuggestionClick");

                CursorAdapter ca = svContact.getSuggestionsAdapter();
                Cursor cursor = cursorAdapter.getCursor();
                cursor.moveToPosition(i);
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                if(id != -1)
                {
                    currContactTypeId = id;
                    String word = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.C_NAME));
                    svContact.setQuery(word,true);

                    svContact.clearFocus();
                    svContact.setFocusable(false);
                    close_btn.setVisibility(View.GONE);
                    return true;
                }else {

                }
                return false;

            }
        });

        svContact.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("aaa" , "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                Cursor cursor = dbManager.get_Contacts_suggestion(s);
                if(cursor == null)
                {
                    MatrixCursor extras = new MatrixCursor(new String[] { "_id", DbHelper.C_NAME });
                    extras.addRow(new String[] { "-1", "Not found" });
                    cursor = extras;
                }
                Log.d("aaa" , "onQueryTextChange" + s + "Count " + cursor.getCount());
                cursorAdapter.changeCursor(cursor);
                return false;
            }
        });

        String[] courses = { "cash", "Bank"};
        ArrayAdapter ad = new ArrayAdapter(this,android.R.layout.simple_spinner_item,courses);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnSave:
                saveData();
                break;
            case R.id.btnCancel:
                backAction();
                break;
            case R.id.btnReceive:
                selectAmountType(1);
                break;
            case R.id.btnSpent:
                selectAmountType(-1);
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
            btnReceive.setBackgroundColor(getResources().getColor(R.color.credit, getApplication().getTheme()));
            btnSpent.setBackgroundColor(getResources().getColor(R.color.unselectButton,getApplication().getTheme()));
        }else {
            btnSpent.setBackgroundColor(getResources().getColor(R.color.debit, getApplication().getTheme()));
            btnReceive.setBackgroundColor(getResources().getColor(R.color.unselectButton,getApplication().getTheme()));
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
}