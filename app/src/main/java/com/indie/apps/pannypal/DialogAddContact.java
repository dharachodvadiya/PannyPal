package com.indie.apps.pannypal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class DialogAddContact extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    // new Contact
    DbManager dbManager;
    Context c;
   // RelativeLayout layoutAddContact;
    TextView txtContactDilogHeading;
    ImageButton btnNewContactClose,btnSaveNewContact;
    RelativeLayout layoutLimit,layoutLimitAnim;
    EditText etContactName, etPhno , etLimitAmt;
    CountryCodePicker codePicker;
    Switch switchLimit;

    private IDilogCallback dilogCallback;

    Contacts currContact = null;


    public DialogAddContact(@NonNull Context context, Contacts currContact, IDilogCallback callback) {
        this.c = context;
        this.currContact = currContact;
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
        View view = inflater.inflate(R.layout.dialog_add_contact, container, false);
        init(view);
        return view;
    }


    void init(View v)
    {
        dbManager= new DbManager(c);
        dbManager.open();

        getActivity().getOnBackPressedDispatcher().addCallback(this,callback);


        //new contact

       // layoutAddContact = v.findViewById(R.id.layoutAddContact);
        btnNewContactClose = v.findViewById(R.id.btnNewContactClose);
        btnSaveNewContact = v.findViewById(R.id.imgbtnSaveContact);
        txtContactDilogHeading = v.findViewById(R.id.txtContactDilogHeading);

        etContactName = v.findViewById(R.id.etContactName);
        etPhno = v.findViewById(R.id.etContactNumber);
        switchLimit = v.findViewById(R.id.switchLimit);
        etLimitAmt = v.findViewById(R.id.etLimitAmt);
        layoutLimit = v.findViewById(R.id.layoutLimit);
        layoutLimitAnim = v.findViewById(R.id.layoutLimitAnim);
        codePicker= v.findViewById(R.id.country_code);

        btnNewContactClose.setOnClickListener(this);
        btnSaveNewContact.setOnClickListener(this);
        switchLimit.setOnCheckedChangeListener(this);
        openNewContactLayout();
    }

    void openNewContactLayout()
    {
        //layoutAddContact.setVisibility(View.VISIBLE);

        if(currContact == null)
        {
            txtContactDilogHeading.setText("Add New Contact");
            etContactName.setText("");
            etPhno.setText("");
            switchLimit.setChecked(false);
        }else {
            txtContactDilogHeading.setText("Edit Contact");
            etContactName.setText(currContact.getName());
            if(!currContact.getPhno().isEmpty())
            {
                String[] phno = etPhno.getText().toString().split(" ");

                codePicker.setCountryForPhoneCode(Integer.parseInt(phno[0]));
                etPhno.setText(phno[2]);
            }

            if(currContact.getIsLimit() == 1)
            {
                switchLimit.setChecked(true);
                etLimitAmt.setText(currContact.getLimitAmt()+"");
            }else {
                switchLimit.setChecked(false);
            }

        }


    }

  /*  void closeNewContactLayout()
    {
        dismiss();
        //layoutAddContact.setVisibility(View.GONE);
    }*/


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.btnNewContactClose:
                //closeNewContactLayout();

                hideKeyboard(getActivity());
                dilogCallback.onCancelClick();
                dismiss();
                break;

            case R.id.imgbtnSaveContact:
                hideKeyboard(getActivity());
                Contacts contacts = saveNewContact();
                if(contacts != null)
                {
                    Gson gson = new Gson();
                    //closeNewContactLayout();
                    dilogCallback.onActionClick(gson.toJson(contacts));
                    dismiss();
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


    Contacts saveNewContact()
    {
        if(etContactName.getText().toString().trim().length() <=0)
        {
            Toast.makeText(c,"Please enter Name",Toast.LENGTH_LONG).show();
            return  null;
        }else if(!etPhno.getText().toString().isEmpty() && !Globle.isValidPhoneNumber(codePicker.getSelectedCountryNameCode(),etPhno.getText().toString()))
        {
            Toast.makeText(c,"Please enter Valid Contact Number",Toast.LENGTH_LONG).show();
            return null;
        }else if(switchLimit.isChecked() && etLimitAmt.getText().toString().trim().length() <=0)
        {
            Toast.makeText(c,"Please enter Limit Amount",Toast.LENGTH_LONG).show();
            return  null;
        }
        else
        {
            String name = etContactName.getText().toString().trim();
            if((currContact == null && dbManager.get_ContactsFromName(name) == -1) ||
                    (currContact != null && dbManager.get_ContactsFromName(name) <=1))
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

                if(currContact == null)
                {
                    contactData.setId(dbManager.add_Contacts(contactData));
                    Toast.makeText(c,"Add new Contact Successfully",Toast.LENGTH_LONG).show();
                }else {

                    contactData.setId(currContact.getId());
                    contactData.setCreditAmt(currContact.getCreditAmt());
                    contactData.setDebitAmt(currContact.getDebitAmt());
                    contactData.setDateTime(Calendar.getInstance().getTimeInMillis());
                    dbManager.edit_Contacts(contactData);

                    Toast.makeText(c,"Contact Edit Successfully",Toast.LENGTH_LONG).show();
                }
                return  contactData;
            }else {
                Toast.makeText(c,"Contact Name Already Exist",Toast.LENGTH_LONG).show();
                return  null;
            }

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
        //closeNewContactLayout();
      //  dilogCallback.onCancelClick();
       // dismiss();
    }


}