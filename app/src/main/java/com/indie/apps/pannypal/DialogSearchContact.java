package com.indie.apps.pannypal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.indie.apps.pannypal.Adapter.SearchContactFromNewEntryAdapter;
import com.indie.apps.pannypal.Database.DbManager;
import com.indie.apps.pannypal.Model.ContactData;
import com.indie.apps.pannypal.Model.Contacts;
import com.indie.apps.pannypal.Model.PaymentType;
import com.indie.apps.pannypal.Model.suggestContactData;
import com.indie.apps.pannypal.Thread.AsyncTaskExecutorService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DialogSearchContact extends DialogFragment  implements View.OnClickListener{


    DbManager dbManager;
    Context c;
    // search layout data
    RelativeLayout layoutContactSearch;
    RecyclerView rvSuggestContact;
    SearchContactFromNewEntryAdapter searchContactFromNewEntryAdapter;

    SearchView svContact ;
    ImageButton btnContactClose, btnNewContact;
    List<suggestContactData> suggestContactAllData = new ArrayList<>();
    List<suggestContactData> suggestContactadapterData = new ArrayList<>();

    private IDilogCallback dilogCallback;

    public DialogSearchContact(@NonNull Context context,IDilogCallback callback) {
        this.c = context;
        this.dilogCallback = callback;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_contact_search, container, false);
        init(view);
        return view;
    }



    void init(View v)
    {
        dbManager= new DbManager(c);
        dbManager.open();

        getActivity().getOnBackPressedDispatcher().addCallback(this,callback);


        // search layout data
        layoutContactSearch = v.findViewById(R.id.layoutContactSearch);
        svContact = v.findViewById(R.id.svContact);
        rvSuggestContact = v.findViewById(R.id.rvContact);
        btnContactClose = v.findViewById(R.id.btnContactClose);
        btnNewContact = v.findViewById(R.id.imgbtnNewContact);

        btnContactClose.setOnClickListener(this);
        btnNewContact.setOnClickListener(this);


        rvSuggestContact.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false);
        rvSuggestContact.setLayoutManager(layoutManager);

        searchContactFromNewEntryAdapter = new SearchContactFromNewEntryAdapter(suggestContactadapterData, new SearchContactFromNewEntryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(suggestContactData item) {


                Gson gson = new Gson();
                dilogCallback.onActionClick(gson.toJson(item));
                dismiss();
                //closeContactSuggestionLayout();
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


        openContactSuggestionLayout();
    }

    void openContactSuggestionLayout()
    {
        layoutContactSearch.setVisibility(View.VISIBLE);
        svContact.setQuery(null, true);
        new loadContactSuggestionData().execute();
    }

   /* void closeContactSuggestionLayout()
    {
        dismiss();
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnContactClose:
                //closeContactSuggestionLayout();
                dilogCallback.onCancelClick();
                dismiss();
                break;

            case R.id.imgbtnNewContact:
                hideKeyboard(getActivity());
                //openNewContactLayout();

               dismiss();
                DialogFragment dialogNewContact = new DialogAddContact(c, null, dilogCallback);

                dialogNewContact.show(getActivity().getSupportFragmentManager(), "tag");
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

    OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            // Handle the back button event
            backAction();
        }
    };

    void backAction()
    {
        //closeContactSuggestionLayout();

        if(!svContact.getQuery().toString().isEmpty())
        {
            svContact.setQuery(null,true);
        }else {
            dilogCallback.onCancelClick();
            dismiss();
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

}