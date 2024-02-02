package com.indie.apps.pannypal.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.indie.apps.pannypal.Model.ContactData;
import com.indie.apps.pannypal.Model.Contacts;
import com.indie.apps.pannypal.Model.PaymentType;
import com.indie.apps.pannypal.Model.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class DbManager {

    private DbHelper dbHelper;
    private SQLiteDatabase database;

    private Context context;

    public DbManager(Context context) {
        this.context = context;
    }

    public DbManager open() throws SQLException {
        dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();

        if(Build.VERSION.SDK_INT >= 28)
        {
            database.disableWriteAheadLogging();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long add_UserProfile(UserProfile data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.U_NAME,data.getName());
        contentValue.put(DbHelper.U_EMAIL,data.getEmail());
        contentValue.put(DbHelper.U_PROFILE_URL,data.getProfileURL());
        contentValue.put(DbHelper.U_CREDIT,data.getCreditAmt());
        contentValue.put(DbHelper.U_DEBIT,data.getDebitAmt());
        contentValue.put(DbHelper.U_TOTAL,data.getTotalAmt());

        long id = database.insert(DbHelper.TBL_USERPROFILE, null, contentValue);

        Log.d("DbManager" , "Add UserProfile");

        return  id;
    }

    public long edit_UserProfile(UserProfile data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.U_NAME,data.getName());
        contentValue.put(DbHelper.U_EMAIL,data.getEmail());
        contentValue.put(DbHelper.U_PROFILE_URL,data.getProfileURL());
        contentValue.put(DbHelper.U_CREDIT,data.getCreditAmt());
        contentValue.put(DbHelper.U_DEBIT,data.getDebitAmt());
        contentValue.put(DbHelper.U_TOTAL,data.getTotalAmt());

        long id = database.update(DbHelper.TBL_USERPROFILE, contentValue,DbHelper.ID + " = " + data.getId(),null);

        Log.d("DbManager" , "Edit UserProfile");

        return  id;
    }

    @SuppressLint("Range")
    public UserProfile get_UserProfile() {
        String[] columns = new String[] { DbHelper.ID,
                DbHelper.U_NAME,
                DbHelper.U_EMAIL,
                DbHelper.U_PROFILE_URL,
                DbHelper.U_CREDIT,
                DbHelper.U_DEBIT,
                DbHelper.U_TOTAL
        };
        UserProfile info = null;
        try {
            @SuppressLint("Recycle") Cursor cursor = database.query(DbHelper.TBL_CONTACTDATA, columns, null, null, null, null, null);

            if (cursor != null && cursor.getCount() >0) {
                cursor.moveToFirst();
                info = new UserProfile(cursor.getString(cursor.getColumnIndex(DbHelper.ID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.U_NAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.U_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.U_PROFILE_URL)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.U_CREDIT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.U_DEBIT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.U_TOTAL))
                );

                Log.d("dbManager" , "UserProfile  get ");
            }
        }catch (Exception e)
        {

        }finally {
            return info;
        }
    }

    public long add_PaymentType(PaymentType data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.P_TYPE,data.getType());

        long id = database.insert(DbHelper.TBL_PAYMENTTYPE, null, contentValue);

        Log.d("DbManager" , "Add PaymentType");

        return  id;
    }

    public long edit_PaymentType(PaymentType data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.P_TYPE,data.getType());

        long id = database.update(DbHelper.TBL_PAYMENTTYPE, contentValue,DbHelper.ID + " = " + data.getId(),null);

        Log.d("DbManager" , "Edit PaymentType");

        return  id;
    }

    public List<PaymentType> get_PaymentType() {
        String[] columns = new String[] { DbHelper.ID, DbHelper.P_TYPE };

        @SuppressLint("Recycle") Cursor cursor = database.query(DbHelper.TBL_PAYMENTTYPE, columns, null, null, null, null, null);
        List<PaymentType> dataList = new ArrayList<>();
        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();

            do{
                @SuppressLint("Range") PaymentType info = new PaymentType(cursor.getString(cursor.getColumnIndex(DbHelper.ID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.P_TYPE)));

                dataList.add(info);

            }while (cursor.moveToNext());

            Log.d("dbManager" , "PaymenType Count "+ dataList.size()+"");
        }
        return dataList;
    }

    public long add_Contacts(Contacts data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.C_NAME,data.getName());
        contentValue.put(DbHelper.C_PHNO,data.getPhno());
        contentValue.put(DbHelper.C_ISLIMIT,data.getIsLimit());
        contentValue.put(DbHelper.C_LIMITAMT,data.getLimitAmt());
        contentValue.put(DbHelper.C_PROFILEURL,data.getProfileURL());
        contentValue.put(DbHelper.C_CREDITAMT,data.getCreditAmt());
        contentValue.put(DbHelper.C_DEBITAMT,data.getDebitAmt());
        contentValue.put(DbHelper.C_TOTALAMT,data.getTotalAmt());
        contentValue.put(DbHelper.C_DATE,data.getDate());
        contentValue.put(DbHelper.C_TIME,data.getTime());

        long id = database.insert(DbHelper.TBL_CONTACTS, null, contentValue);

        Log.d("DbManager" , "Add Contacts");

        return  id;
    }

    public long edit_Contacts(Contacts data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.C_NAME,data.getName());
        contentValue.put(DbHelper.C_PHNO,data.getPhno());
        contentValue.put(DbHelper.C_ISLIMIT,data.getIsLimit());
        contentValue.put(DbHelper.C_LIMITAMT,data.getLimitAmt());
        contentValue.put(DbHelper.C_PROFILEURL,data.getProfileURL());
        contentValue.put(DbHelper.C_CREDITAMT,data.getCreditAmt());
        contentValue.put(DbHelper.C_DEBITAMT,data.getDebitAmt());
        contentValue.put(DbHelper.C_TOTALAMT,data.getTotalAmt());
        contentValue.put(DbHelper.C_DATE,data.getDate());
        contentValue.put(DbHelper.C_TIME,data.getTime());

        long id = database.update(DbHelper.TBL_CONTACTS, contentValue,DbHelper.ID + " = " + data.getId(),null);

        Log.d("DbManager" , "Edit Contacts");

        return  id;
    }

    public List<Contacts> get_Contacts() {
        String[] columns = new String[] { DbHelper.ID,
                DbHelper.C_NAME,
                DbHelper.C_PHNO,
                DbHelper.C_ISLIMIT,
                DbHelper.C_LIMITAMT,
                DbHelper.C_PROFILEURL,
                DbHelper.C_CREDITAMT,
                DbHelper.C_DEBITAMT,
                DbHelper.C_TOTALAMT,
                DbHelper.C_DATE,
                DbHelper.C_TIME
        };

        @SuppressLint("Recycle") Cursor cursor = database.query(DbHelper.TBL_CONTACTS, columns, null, null, null, null, null);
        List<Contacts> dataList = new ArrayList<>();
        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();

            do{
                @SuppressLint("Range") Contacts info = new Contacts(cursor.getString(cursor.getColumnIndex(DbHelper.ID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_NAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_PHNO)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_ISLIMIT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_LIMITAMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_PROFILEURL)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_CREDITAMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_DEBITAMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_TOTALAMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_DATE)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_TIME))
                        );

                dataList.add(info);

            }while (cursor.moveToNext());

            Log.d("dbManager" , "Contacts Count "+ dataList.size()+"");
        }
        return dataList;
    }

    public long add_ContactData(ContactData data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.CD_CID,data.getC_id());
        contentValue.put(DbHelper.CD_PID,data.getP_id());
        contentValue.put(DbHelper.CD_TYPE,data.getType());
        contentValue.put(DbHelper.CD_AMT,data.getAmount());
        contentValue.put(DbHelper.CD_REMARK,data.getRemark());
        contentValue.put(DbHelper.CD_DATE,data.getDate());
        contentValue.put(DbHelper.CD_TIME,data.getTime());


        long id = database.insert(DbHelper.TBL_CONTACTDATA, null, contentValue);

        Log.d("DbManager" , "Add ContactData");

        return  id;
    }

    public long edit_ContactData(ContactData data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.CD_CID,data.getC_id());
        contentValue.put(DbHelper.CD_PID,data.getP_id());
        contentValue.put(DbHelper.CD_TYPE,data.getType());
        contentValue.put(DbHelper.CD_AMT,data.getAmount());
        contentValue.put(DbHelper.CD_REMARK,data.getRemark());
        contentValue.put(DbHelper.CD_DATE,data.getDate());
        contentValue.put(DbHelper.CD_TIME,data.getTime());

        long id = database.update(DbHelper.TBL_CONTACTDATA, contentValue,DbHelper.ID + " = " + data.getId(),null);

        Log.d("DbManager" , "Edit ContactData");

        return  id;
    }

    public List<ContactData> get_ContactData() {
        String[] columns = new String[] { DbHelper.ID,
                DbHelper.CD_CID,
                DbHelper.CD_PID,
                DbHelper.CD_TYPE,
                DbHelper.CD_AMT,
                DbHelper.CD_REMARK,
                DbHelper.CD_DATE,
                DbHelper.CD_TIME,
        };

        @SuppressLint("Recycle") Cursor cursor = database.query(DbHelper.TBL_CONTACTDATA, columns, null, null, null, null, null);
        List<ContactData> dataList = new ArrayList<>();
        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();

            do{
                @SuppressLint("Range") ContactData info = new ContactData(cursor.getString(cursor.getColumnIndex(DbHelper.ID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_CID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_PID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_TYPE)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_AMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_REMARK)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_DATE)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_TIME))
                        );

                dataList.add(info);

            }while (cursor.moveToNext());

            Log.d("dbManager" , "ContactData Count "+ dataList.size()+"");
        }
        return dataList;
    }

}
