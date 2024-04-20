package com.indie.apps.pannypal.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.indie.apps.pannypal.Adapter.ContactAdapter;
import com.indie.apps.pannypal.ContactSelectedActivity;
import com.indie.apps.pannypal.Database.DbManager;
import com.indie.apps.pannypal.DialogAddContact;
import com.indie.apps.pannypal.Globle;
import com.indie.apps.pannypal.HomeActivity2;
import com.indie.apps.pannypal.IDilogCallback;
import com.indie.apps.pannypal.Model.Contacts;
import com.indie.apps.pannypal.R;
import com.indie.apps.pannypal.Thread.AsyncTaskExecutorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactFragment extends Fragment implements View.OnClickListener, HomeActivity2.OnBackPressedListener {

    // contact list layout data
    RelativeLayout layoutSearch, layoutMultiSelect;
    RecyclerView recycleviewData;
    SearchView svContact ;
    ImageButton  btnNewContact,btnBack, imgBtnEdit, imgBtnMultiDelete;
    DbManager dbManager;
    ContactAdapter contactAdapter;
    List<Contacts> suggestContactAllData = new ArrayList<>();
    List<Contacts> suggestContactadapterData = new ArrayList<>();

    HashMap<Integer,Contacts> selectContactList = new HashMap<>();

    TextView txtNoDataFound;

    boolean isEdit = false;
    int editItemPos;
    Contacts editItem;
    
    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(view);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    void init(View view)
    {
        dbManager= new DbManager(getContext());
        dbManager.open();

        // contact list layout data

        layoutMultiSelect = view.findViewById(R.id.layoutMultiselect);
        layoutSearch = view.findViewById(R.id.layoutSearch);

        recycleviewData = view.findViewById(R.id.recycleviewData);
        svContact = view.findViewById(R.id.svContact);
        btnNewContact = view.findViewById(R.id.imgbtnNewContact);
        btnBack = view.findViewById(R.id.btnBack);
        imgBtnEdit = view.findViewById(R.id.imgBtnEdit);
        imgBtnMultiDelete = view.findViewById(R.id.imgBtnMultiDelete);
        txtNoDataFound = view.findViewById(R.id.txtNoDataFound);

        btnNewContact.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        imgBtnEdit.setOnClickListener(this);
        imgBtnMultiDelete.setOnClickListener(this);

        recycleviewData.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycleviewData.setLayoutManager(layoutManager);

        contactAdapter = new ContactAdapter(ContactFragment.this,getContext(),suggestContactadapterData, txtNoDataFound,new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contacts item) {
                Intent i = new Intent(getContext(), ContactSelectedActivity.class);
                Gson gson = new Gson();
                i.putExtra(Globle.SELECTED_ITEM, gson.toJson(item));
                startActivity(i);
            }

            @Override
            public void OnItemLongClickAdd(Contacts item, int pos) {
                selectContactList.put(pos,item);
                openMultiselectLayout();
            }

            @Override
            public void OnItemLongClickRemove(Contacts item, int pos) {
                selectContactList.remove(pos);

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

        openContactListLayout(false,null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.imgbtnNewContact:
                hideKeyboard(getActivity());
                //svContact.setQuery(null,true);
                editItem = null;
                openNewContactLayout();
                break;

            case R.id.btnBack:
                hideKeyboard(getActivity());
                backAction();
                break;

            case R.id.imgBtnEdit:
                isEdit =true;

                for (Map.Entry<Integer, Contacts> e : selectContactList.entrySet())
                {
                    editItemPos = e.getKey();
                    editItem = e.getValue();
                    break;
                }

                hideKeyboard(getActivity());
                svContact.setQuery(null,true);
                openNewContactLayout();
                break;

            case R.id.imgBtnMultiDelete:
                int count = selectContactList.size();

                if(dbManager.delete_ContactFromIds(selectContactList) >0)
                {
                    for (Map.Entry<Integer, Contacts> e : selectContactList.entrySet())
                    {
                        suggestContactadapterData.remove(e.getValue());
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

    boolean backAction()
    {
        if(!svContact.getQuery().toString().isEmpty())
        {
            svContact.setQuery(null,true);
        }else if(layoutMultiSelect.getVisibility() == View.VISIBLE)
        {
            selectContactList.clear();
            contactAdapter.setSelected(false);
            openSearchLayout();
        }else{
           /* Intent i = new Intent(getContext(), HomeActivity2.class);
            startActivity(i);*/
            return true;
        }
        return  false;
    }

    void openContactListLayout(boolean isComeFromNewData, Contacts contacts)
    {
        openSearchLayout();
        // layoutAddContact.setVisibility(View.GONE);
        svContact.setQuery(null, true);

        if(isComeFromNewData)
        {
            if(isEdit)
            {
                suggestContactAllData.remove(editItemPos);
                suggestContactadapterData.remove(editItemPos);
                suggestContactAllData.add(0,contacts);
                suggestContactadapterData.add(0,contacts);
                contactAdapter.setSelected(false);
                //contactAdapter.itemEditDataChange(editItemPos);

            }else {
                suggestContactAllData.add(0,contacts);
                suggestContactadapterData.add(0,contacts);

                contactAdapter.itemInsertDataChange(0);
            }

        }else {
            new loadContactSuggestionData().execute();
        }

    }

    void openNewContactLayout()
    {
        DialogFragment dialogNewContact = new DialogAddContact(getContext(),editItem, new IDilogCallback() {
            @Override
            public void onActionClick(String data) {
                Gson gson = new Gson();
                Contacts contacts = gson.fromJson(data,Contacts.class);
                if(contacts != null)
                {
                    openContactListLayout(true,contacts);
                    isEdit = false;
                    editItem = null;
                    editItemPos = -1;
                    // closeNewContactLayout();
                }
            }

            @Override
            public void onCancelClick() {

            }
        });

        dialogNewContact.show(getActivity().getSupportFragmentManager(), "tag");
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

    @Override
    public boolean onBackPressed() {

        return backAction();
    }
}