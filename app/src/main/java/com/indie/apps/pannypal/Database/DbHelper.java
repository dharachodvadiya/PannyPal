package com.indie.apps.pannypal.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static final String TBL_PAYMENTTYPE = "PaymentType";
    public static final String TBL_CONTACTS = "Contacts";
    public static final String TBL_CONTACTDATA = "ContactData";
    // Database Information
    public static final String DB_NAME = "dbpannypal.DB";

    public static final String ID = "id";

    //Payment_type table
    public static final String P_TYPE = "p_type";

    // Contacts table
    public static final String C_NAME = "c_name";
    public static final String C_PHNO = "c_phno";
    public static final String C_ISLIMIT = "c_isLimit";
    public static final String C_LIMITAMT = "c_limitAmt";
    public static final String C_PROFILEURL = "c_profileURL";
    public static final String C_CREDITAMT = "c_creditAmt";
    public static final String C_DEBITAMT = "c_debitAmt";
    public static final String C_TOTALAMT = "c_totalAmt";
    public static final String C_DATE = "c_date";
    public static final String C_TIME = "c_time";

    //Caontact data table

    public static final String CD_CID = "cd_cId";
    public static final String CD_PID = "cd_pId";
    public static final String CD_TYPE = "cd_type";
    public static final String CD_AMT = "cd_amount";
    public static final String CD_REMARK = "cd_remark";
    public static final String CD_DATE = "cd_date";
    public static final String CD_TIME = "cd_time";

    private static final String CREATE_TABLE_PAYMENTTYPE = "create table " + TBL_PAYMENTTYPE + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            P_TYPE + " TEXT " +
            ");";

    private static final String CREATE_TABLE_CONTACTS = "create table " + TBL_CONTACTS + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            C_NAME + " TEXT , " +
            C_PHNO + " TEXT , " +
            C_ISLIMIT + " TEXT , " +
            C_LIMITAMT + " TEXT , " +
            C_PROFILEURL + " TEXT , " +
            C_TOTALAMT + " TEXT ," +
            C_CREDITAMT + " TEXT ," +
            C_DEBITAMT + " TEXT ," +
            C_DATE + " TEXT ," +
            C_TIME + " TEXT " +
            ");";

    public static final String CREATE_TABLE_CONTACT_DATA = "create table " + TBL_CONTACTDATA + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CD_CID + " TEXT , " +
            CD_PID + " TEXT , " +
            CD_TYPE + " TEXT , " +
            CD_AMT + " TEXT , " +
            CD_REMARK + " TEXT,  " +
            CD_DATE + " TEXT,  " +
            CD_TIME + " TEXT  " +
            ");";

    // database version
    static final int DB_VERSION = 1;
    public DbHelper(@Nullable Context context) {
        super(context,DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_PAYMENTTYPE);
        sqLiteDatabase.execSQL(CREATE_TABLE_CONTACTS);
        sqLiteDatabase.execSQL(CREATE_TABLE_CONTACT_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
