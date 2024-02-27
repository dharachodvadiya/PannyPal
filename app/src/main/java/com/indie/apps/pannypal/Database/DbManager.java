package com.indie.apps.pannypal.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.indie.apps.pannypal.Adapter.SearchContactFromNewEntryAdapter;
import com.indie.apps.pannypal.Globle;
import com.indie.apps.pannypal.Model.ContactData;
import com.indie.apps.pannypal.Model.Contacts;
import com.indie.apps.pannypal.Model.PaymentType;
import com.indie.apps.pannypal.Model.UserProfile;
import com.indie.apps.pannypal.Model.suggestContactData;

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

    public boolean isTableExists(String tableName) {


        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public long add_UserProfile(UserProfile data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.U_NAME,data.getName());
        contentValue.put(DbHelper.U_EMAIL,data.getEmail());
        contentValue.put(DbHelper.U_PROFILE_URL,data.getProfileURL());
        contentValue.put(DbHelper.U_CREDIT,data.getCreditAmt());
        contentValue.put(DbHelper.U_DEBIT,data.getDebitAmt());

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
                DbHelper.U_DEBIT
        };
        UserProfile info = null;
        try {
            @SuppressLint("Recycle") Cursor cursor = database.query(DbHelper.TBL_USERPROFILE, columns, null, null, null, null, null);

            if (cursor != null && cursor.getCount() >0) {
                cursor.moveToFirst();
                info = new UserProfile(cursor.getLong(cursor.getColumnIndex(DbHelper.ID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.U_NAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.U_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.U_PROFILE_URL)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.U_CREDIT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.U_DEBIT))
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
                @SuppressLint("Range") PaymentType info = new PaymentType(cursor.getLong(cursor.getColumnIndex(DbHelper.ID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.P_TYPE)));

                dataList.add(info);

            }while (cursor.moveToNext());

            Log.d("dbManager" , "PaymenType Count "+ dataList.size()+"");
        }
        return dataList;
    }

    public long get_PaymentFromType(String name) {

        String query ="select " + DbHelper.ID + " from " + DbHelper.TBL_PAYMENTTYPE + " where " + DbHelper.P_TYPE + " = '" + name + "' COLLATE NOCASE" ;
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();

            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(DbHelper.ID));
            Log.d("dbManager" , "Payment Count from name Id-  "+id +"");
            return id;
        }
        return -1;
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
        contentValue.put(DbHelper.C_DATE,data.getDateTime());

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
        contentValue.put(DbHelper.C_DATE,data.getDateTime());

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
                DbHelper.C_DATE
        };

        @SuppressLint("Recycle") Cursor cursor = database.query(DbHelper.TBL_CONTACTS, columns, null, null, null, null, DbHelper.C_DATE+" DESC");
        List<Contacts> dataList = new ArrayList<>();
        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();

            do{
                @SuppressLint("Range") Contacts info = new Contacts(cursor.getLong(cursor.getColumnIndex(DbHelper.ID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_NAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_PHNO)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.C_ISLIMIT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.C_LIMITAMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_PROFILEURL)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.C_CREDITAMT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.C_DEBITAMT)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.C_DATE))
                        );

                dataList.add(info);

            }while (cursor.moveToNext());

            Log.d("dbManager" , "Contacts Count "+ dataList.size()+"");
        }
        return dataList;
    }

    @SuppressLint("Range")
    public Contacts get_ContactFromId(long id) {
        String[] columns = new String[] { DbHelper.ID,
                DbHelper.C_NAME,
                DbHelper.C_PHNO,
                DbHelper.C_ISLIMIT,
                DbHelper.C_LIMITAMT,
                DbHelper.C_PROFILEURL,
                DbHelper.C_CREDITAMT,
                DbHelper.C_DEBITAMT,
                DbHelper.C_DATE
        };

        @SuppressLint("Recycle") Cursor cursor = database.query(DbHelper.TBL_CONTACTS, columns, DbHelper.ID +"=?",new String[]{String.valueOf(id)}, null, null, null);
        Contacts data = null;
        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();

            do{
                data = new Contacts(cursor.getLong(cursor.getColumnIndex(DbHelper.ID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_NAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_PHNO)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.C_ISLIMIT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.C_LIMITAMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_PROFILEURL)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.C_CREDITAMT)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.C_DEBITAMT)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.C_DATE))
                );

            }while (cursor.moveToNext());

            Log.d("dbManager" , "Contact from Id "+ data.getId()+"");
        }
        return data;
    }

    public int delete_ContactFromIds(List<Contacts> contacts) {

        int count = contacts.size();
        double creditAmt=0, debitAmt = 0;
        String[] arrayId = new String[count];
        for(int j = 0 ; j< count; j++)
        {
            Contacts tmp = contacts.get(j);
            arrayId[j] = tmp.getId()+"";
            creditAmt += tmp.getCreditAmt();
            debitAmt += tmp.getDebitAmt();

        }
       // List<CrDrInfo> tmpInfo = fetchCrDrFromId(id);
        int deleteCount = database.delete(DbHelper.TBL_CONTACTS, DbHelper.ID + " IN (" + TextUtils.join(",", arrayId) + ")", null);
        Log.d("dbManager" , "delete_ContactFromIds "+ deleteCount);

        if(deleteCount >0)
        {
            delete_ContactDataFromIds(arrayId);

            if(creditAmt >0)
            {
                Globle.MyProfile.addCreditAmt(-creditAmt);

            }
            if(debitAmt >0){
                Globle.MyProfile.addDebitAmt(-debitAmt);

            }

            edit_UserProfile(Globle.MyProfile);
        }

        return deleteCount;


    }

    public List<suggestContactData> get_ContactsNameList() {
        String[] columns = new String[] { DbHelper.ID,
                DbHelper.C_NAME,
                DbHelper.C_PHNO,
                DbHelper.C_ISLIMIT,
                DbHelper.C_LIMITAMT,
                DbHelper.C_PROFILEURL,
                DbHelper.C_CREDITAMT,
                DbHelper.C_DEBITAMT,
                DbHelper.C_DATE
        };

        @SuppressLint("Recycle") Cursor cursor = database.query(DbHelper.TBL_CONTACTS, columns, null, null, null, null, DbHelper.C_DATE+" DESC");
        List<suggestContactData> dataList = new ArrayList<>();
        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();

            do{
                @SuppressLint("Range") suggestContactData info = new suggestContactData(cursor.getLong(cursor.getColumnIndex(DbHelper.ID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.C_NAME))
                );

                dataList.add(info);

            }while (cursor.moveToNext());

            Log.d("dbManager" , "Contacts Count "+ dataList.size()+"");
        }
        return dataList;
    }

    public long get_ContactsFromName(String name) {
        String query ="select " + DbHelper.ID + " from " + DbHelper.TBL_CONTACTS + " where " + DbHelper.C_NAME + " = '" + name + "' COLLATE NOCASE" ;
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery(query, null);
        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();

            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(DbHelper.ID));
            Log.d("dbManager" , "Contacts Count from name Id-  "+id +"");
            return id;
        }
        return -1;
    }

    public Cursor get_Contacts_suggestion(String name) {
        String[] columns = new String[] { DbHelper.ID,
                DbHelper.C_NAME
        };

        String query ="select " + DbHelper.ID + " as _id," + DbHelper.C_NAME + " from " + DbHelper.TBL_CONTACTS + " where " + DbHelper.C_NAME + " Like '" + name + "%'" ;
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery(query, null);
        Log.d("dbManager" , "Suggested Contacts Count "+ cursor.getCount()+"");
        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();
return cursor;
        }
        return null;
    }

    public long add_ContactData(ContactData data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.CD_CID,data.getC_id());
        contentValue.put(DbHelper.CD_PID,data.getP_id());
        contentValue.put(DbHelper.CD_C_NAME,data.getC_name());
        contentValue.put(DbHelper.CD_P_NAME,data.getP_name());
        contentValue.put(DbHelper.CD_TYPE,data.getType());
        contentValue.put(DbHelper.CD_AMT,data.getAmount());
        contentValue.put(DbHelper.CD_REMARK,data.getRemark());
        contentValue.put(DbHelper.CD_DATE,data.getDateTime());


        long id = database.insert(DbHelper.TBL_CONTACTDATA, null, contentValue);

        Log.d("DbManager" , "Add ContactData");

        Contacts contacts = get_ContactFromId(data.getC_id());

        if(data.getType() == 1)
        {
            Globle.MyProfile.addCreditAmt(data.getAmount());

            contacts.addCreditAmt(data.getAmount());
        }else {
            Globle.MyProfile.addDebitAmt(data.getAmount());

            contacts.addDebitAmt(data.getAmount());
        }

        contacts.setDateTime(data.getDateTime());
        edit_UserProfile(Globle.MyProfile);
        edit_Contacts(contacts);
        return  id;
    }

    public long edit_ContactData(ContactData data) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DbHelper.CD_CID,data.getC_id());
        contentValue.put(DbHelper.CD_PID,data.getP_id());
        contentValue.put(DbHelper.CD_C_NAME,data.getC_name());
        contentValue.put(DbHelper.CD_P_NAME,data.getP_name());
        contentValue.put(DbHelper.CD_TYPE,data.getType());
        contentValue.put(DbHelper.CD_AMT,data.getAmount());
        contentValue.put(DbHelper.CD_REMARK,data.getRemark());
        contentValue.put(DbHelper.CD_DATE,data.getDateTime());

        long id = database.update(DbHelper.TBL_CONTACTDATA, contentValue,DbHelper.ID + " = " + data.getId(),null);

        Log.d("DbManager" , "Edit ContactData");

        return  id;
    }

    public List<ContactData> get_ContactData() {
        String[] columns = new String[] { DbHelper.ID,
                DbHelper.CD_CID,
                DbHelper.CD_PID,
                DbHelper.CD_C_NAME,
                DbHelper.CD_P_NAME,
                DbHelper.CD_TYPE,
                DbHelper.CD_AMT,
                DbHelper.CD_REMARK,
                DbHelper.CD_DATE
        };

        @SuppressLint("Recycle") Cursor cursor = database.query(DbHelper.TBL_CONTACTDATA, columns, null, null, null, null, null);
        List<ContactData> dataList = new ArrayList<>();
        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();

            do{
                @SuppressLint("Range") ContactData info = new ContactData(cursor.getLong(cursor.getColumnIndex(DbHelper.ID)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.CD_CID)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.CD_PID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_C_NAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_P_NAME)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.CD_TYPE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CD_AMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_REMARK)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.CD_DATE))
                        );

                dataList.add(info);

            }while (cursor.moveToNext());

            Log.d("dbManager" , "ContactData Count "+ dataList.size()+"");
        }
        return dataList;
    }

    public List<ContactData> get_ContactData_List_DESC() {
        String[] columns = new String[] { DbHelper.ID,
                DbHelper.CD_CID,
                DbHelper.CD_PID,
                DbHelper.CD_C_NAME,
                DbHelper.CD_P_NAME,
                DbHelper.CD_TYPE,
                DbHelper.CD_AMT,
                DbHelper.CD_REMARK,
                DbHelper.CD_DATE
        };

        @SuppressLint("Recycle") Cursor cursor = database.query(DbHelper.TBL_CONTACTDATA, columns, null, null, null, null, DbHelper.CD_DATE+" DESC");
        List<ContactData> dataList = new ArrayList<>();
        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();

            do{
                @SuppressLint("Range") ContactData info = new ContactData(cursor.getLong(cursor.getColumnIndex(DbHelper.ID)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.CD_CID)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.CD_PID)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_C_NAME)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_P_NAME)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.CD_TYPE)),
                        cursor.getDouble(cursor.getColumnIndex(DbHelper.CD_AMT)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.CD_REMARK)),
                        cursor.getLong(cursor.getColumnIndex(DbHelper.CD_DATE))
                );

                dataList.add(info);

            }while (cursor.moveToNext());

            Log.d("dbManager" , "ContactData Count "+ dataList.size()+"");
        }
        return dataList;
    }

    public int delete_ContactDataFromIds(String[] ids) {

        // List<CrDrInfo> tmpInfo = fetchCrDrFromId(id);
        int deleteCount = database.delete(DbHelper.TBL_CONTACTDATA, DbHelper.CD_CID + " IN (" + TextUtils.join(",", ids) + ")", null);
        Log.d("dbManager" , "delete_ContactDataFromIds "+ deleteCount+"");
        return deleteCount;


    }

}
