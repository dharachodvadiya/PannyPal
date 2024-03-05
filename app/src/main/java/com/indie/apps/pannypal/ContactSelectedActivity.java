package com.indie.apps.pannypal;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.indie.apps.pannypal.Adapter.ContactAdapter;
import com.indie.apps.pannypal.Adapter.ContactDataAdapter;
import com.indie.apps.pannypal.Database.DbManager;
import com.indie.apps.pannypal.Model.ContactData;
import com.indie.apps.pannypal.Model.Contacts;
import com.indie.apps.pannypal.Thread.AsyncTaskExecutorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactSelectedActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    Contacts currContact;

    ImageButton btnBack;
    TextView txtName, txtPhno, txtLimitAmt, txtNoDataFound;
    RecyclerView recyclerView;
    ContactDataAdapter contactDataAdapter;
    List<ContactData> suggestContactadapterData = new ArrayList<>();

    HashMap<Integer,ContactData> selectContactList = new HashMap<>();
    DbManager dbManager;

    RelativeLayout layoutTotal, layoutMultiselect;
    TextView txtCrTotal,txtdeTotal,txtTotal;
    ImageButton imgBtnMultiDeletelete;

    ContactData currSelectedData;
    int currSelectedDataPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_selected);

        String str = getIntent().getStringExtra("selected_item");
        Gson gson = new Gson();
        currContact = gson.fromJson(str, Contacts.class);

        init();


    }

    void init()
    {
        dbManager= new DbManager(ContactSelectedActivity.this);
        dbManager.open();

        getOnBackPressedDispatcher().addCallback(this,callback);

        btnBack = findViewById(R.id.btnBack);
        txtName = findViewById(R.id.txtName);
        txtPhno = findViewById(R.id.txtPhno);
        txtLimitAmt = findViewById(R.id.txtLimitAmt);
        recyclerView = findViewById(R.id.recyclerView);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);

        layoutTotal = findViewById(R.id.layoutTotal);
        layoutMultiselect = findViewById(R.id.layoutMultiselect);
        txtCrTotal = findViewById(R.id.txtCrTotal);
        txtdeTotal = findViewById(R.id.txtdeTotal);
        txtTotal = findViewById(R.id.txtTotal);
        imgBtnMultiDeletelete = findViewById(R.id.imgBtnMultiDeletelete);

        btnBack.setOnClickListener(this);
        imgBtnMultiDeletelete.setOnClickListener(this);

        txtName.setText(currContact.getName());

        if(currContact.getPhno().isEmpty())
        {
            txtPhno.setText("Phone Number Not added");
        }else {
            txtPhno.setText("+" + currContact.getPhno());
        }

        if(currContact.getIsLimit() == 0)
        {
            txtLimitAmt.setText("Not set");
        }else {
            txtLimitAmt.setText(Globle.getFormattedValue(currContact.getLimitAmt()) +"");
        }

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        contactDataAdapter = new ContactDataAdapter(this,suggestContactadapterData, txtNoDataFound,new ContactDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ContactData item, View v, int pos) {
              /*  Intent i = new Intent(ContactActivity.this, ContactSelectedActivity.class);
                i.putExtra("selected_item", item);
                startActivity(i);*/

                openMenu(item,v, pos );
            }

            @Override
            public void OnItemLongClickAdd(ContactData item, int pos) {
                selectContactList.put(pos,item);
                openMultiselectLayout();
            }

            @Override
            public void OnItemLongClickRemove(ContactData item, int pos) {
                selectContactList.remove(pos);

                if(selectContactList.size() == 0)
                {
                    contactDataAdapter.setSelected(false);
                    openTotalLayout();
                }else {
                    openMultiselectLayout();
                }
            }

        });
        recyclerView.setAdapter(contactDataAdapter);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(contactDataAdapter.getItemCount() - 1);
            }
        });

        openContactListLayout(false,null);

    }

    void  setTotal()
    {
        txtCrTotal.setText("" + Globle.getFormattedValue(currContact.getCreditAmt()));
        txtdeTotal.setText("" + Globle.getFormattedValue(currContact.getDebitAmt()));
        txtTotal.setText("" + Globle.getFormattedValue(currContact.getCreditAmt()-currContact.getDebitAmt()));
    }
    void openContactListLayout(boolean isComeFromNewData, Contacts contacts)
    {
        openTotalLayout();
        if(isComeFromNewData)
        {
           /* if(isEdit)
            {
                suggestContactAllData.set(editItemPos,contacts);
                suggestContactadapterData.set(editItemPos,contacts);
                contactAdapter.setSelected(false);
                //contactAdapter.itemEditDataChange(editItemPos);

            }else {
                suggestContactAllData.add(0,contacts);
                suggestContactadapterData.add(0,contacts);

                contactAdapter.itemInsertDataChange(0);
            }*/

        }else {
            new loadContactSuggestionData().execute();
        }

    }

    void  openTotalLayout()
    {
        layoutMultiselect.setVisibility(View.GONE);
        layoutTotal.setVisibility(View.VISIBLE);

        setTotal();
    }


    void openMultiselectLayout()
    {
        layoutMultiselect.setVisibility(View.VISIBLE);
        layoutTotal.setVisibility(View.GONE);

       /* if(selectContactList.size() == 1)
        {
            //show edit button
            imgBtnEdit.setVisibility(View.VISIBLE);
        }else {
            //hide edit button
            imgBtnEdit.setVisibility(View.GONE);
        }*/
    }

    void  openMenu(ContactData contactData, View view, int pos)
    {
        currSelectedData = contactData;
        currSelectedDataPos = pos;
        PopupMenu menu = new PopupMenu (ContactSelectedActivity.this, view);
        menu.inflate (R.menu.data_menu_layout);
        menu.show();

        menu.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.item_editEntry:
                break;
            case R.id.item_deleteEntry:

                if(dbManager.delete_ContactDataFromId(currSelectedData, currContact.getId()) >0)
                {

                    suggestContactadapterData.remove(currSelectedData);
                    currContact = dbManager.get_ContactFromId(currContact.getId());
                    contactDataAdapter.notifyItemRemoved(currSelectedDataPos);
                    openTotalLayout();

                    currSelectedData = null;
                    currSelectedDataPos = -1;
                }

                break;
        }
        return false;
    }


    public class loadContactSuggestionData extends AsyncTaskExecutorService< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            // before start background execution
        }

        @Override
        protected Void doInBackground(Void params) {
            // perform background task load app and return a result based on need
            suggestContactadapterData.clear();
            suggestContactadapterData.addAll(dbManager.get_ContactDataFromContactId(currContact.getId()));

            return params;
        }

        @Override
        protected void onPostExecute(Void result) {
            contactDataAdapter.dataChange();
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
        if(layoutMultiselect.getVisibility() == View.VISIBLE)
        {
            selectContactList.clear();
            contactDataAdapter.setSelected(false);
            openTotalLayout();
        }else {
            Intent i = new Intent(ContactSelectedActivity.this, ContactActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnBack:
                backAction();
                break;
            case R.id.imgBtnMultiDeletelete:

                if(dbManager.delete_ContactDataFromIds(selectContactList, currContact.getId()) >0)
                {
                    for (Map.Entry<Integer, ContactData> e : selectContactList.entrySet())
                    {
                        suggestContactadapterData.remove(e.getValue());
                    }
                    selectContactList.clear();
                    contactDataAdapter.setSelected(false);

                    currContact = dbManager.get_ContactFromId(currContact.getId());
                    openTotalLayout();
                    // contactAdapter.notifyDataSetChanged();

                }
                break;
        }
    }
}