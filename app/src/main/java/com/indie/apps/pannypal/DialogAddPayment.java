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

public class DialogAddPayment extends DialogFragment  implements View.OnClickListener {


    DbManager dbManager;
    Context c;

    // new Payment
    ImageButton btnNewPaymentClose,btnSaveNewPayment;
    EditText etPaymentName;

    IDilogCallback dilogCallback;

    public DialogAddPayment(@NonNull Context context,IDilogCallback callback) {
        this.c = context;
        this.dilogCallback = callback;
    }
  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_entry);
        init();
        selectAmountType(currAmtType);
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_payment, container, false);
        init(view);
        return view;
    }

    void init(View v)
    {
        dbManager= new DbManager(c);
        dbManager.open();

        getActivity().getOnBackPressedDispatcher().addCallback(this,callback);


        //new payment
        btnNewPaymentClose = v.findViewById(R.id.btnNewPaymentClose);
        btnSaveNewPayment = v.findViewById(R.id.imgbtnSavePayment);
        etPaymentName = v.findViewById(R.id.etContactType);

        btnNewPaymentClose.setOnClickListener(this);
        btnSaveNewPayment.setOnClickListener(this);
        openNewPaymentLayout();
    }

    void openNewPaymentLayout()
    {
        etPaymentName.setText("");
    }

   /* void closeNewPaymentLayout(boolean isNewData)
    {

        dismiss();

    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.btnNewPaymentClose:
                dilogCallback.onCancelClick();
                dismiss();
                //closeNewPaymentLayout(false);
                break;

            case R.id.imgbtnSavePayment:
                if(saveNewPayment())
                {
                    //closeNewPaymentLayout(true);
                    dilogCallback.onActionClick(null);
                    dismiss();
                }
                break;

        }
    }


    boolean  saveNewPayment()
    {
        if(etPaymentName.getText().toString().trim().length() <=0)
        {
            Toast.makeText(c,"Please enter Payment Type",Toast.LENGTH_LONG).show();
            return  false;
        }else if(dbManager.get_PaymentFromType(etPaymentName.getText().toString().trim()) != -1)
        {
            Toast.makeText(c,"Payment type  already exist",Toast.LENGTH_LONG).show();
            return  false;
        }else {
            String name = etPaymentName.getText().toString().trim();
            PaymentType data = new PaymentType(
                    name);

            dbManager.add_PaymentType(data);

            Toast.makeText(c,"Add new Payment Successfully",Toast.LENGTH_LONG).show();
            return  true;
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
        //closeNewPaymentLayout(false);
        //dilogCallback.onCancelClick();
        //dismiss();
    }

}