package com.indie.apps.pannypal.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.Double4;

import androidx.annotation.NonNull;

public class Contacts implements Parcelable {

    private long id;
    private String name;
    private String phno;
    private int isLimit; // 0= false, 1= true
    private Double limitAmt;
    private String profileURL;
    private Double creditAmt;
    private Double debitAmt;
    private long dateTime;

    public Contacts(long id, String name, String phno, int isLimit, Double limitAmt, String profileURL, Double creditAmt, Double debitAmt, long dateTime) {
        this.id = id;
        this.name = name;
        this.phno = phno;
        this.isLimit = isLimit;
        this.limitAmt = limitAmt;
        this.profileURL = profileURL;
        this.creditAmt = creditAmt;
        this.debitAmt = debitAmt;
        this.dateTime = dateTime;
    }

    public Contacts(String name, String phno, int isLimit, Double limitAmt, String profileURL, Double creditAmt, Double debitAmt, long dateTime) {
        this.name = name;
        this.phno = phno;
        this.isLimit = isLimit;
        this.limitAmt = limitAmt;
        this.profileURL = profileURL;
        this.creditAmt = creditAmt;
        this.debitAmt = debitAmt;
        this.dateTime = dateTime;
    }

    protected Contacts(Parcel in) {
        id = in.readLong();
        name = in.readString();
        phno = in.readString();
        isLimit = in.readInt();
        if (in.readByte() == 0) {
            limitAmt = null;
        } else {
            limitAmt = in.readDouble();
        }
        profileURL = in.readString();
        if (in.readByte() == 0) {
            creditAmt = null;
        } else {
            creditAmt = in.readDouble();
        }
        if (in.readByte() == 0) {
            debitAmt = null;
        } else {
            debitAmt = in.readDouble();
        }
        dateTime = in.readLong();
    }

    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel in) {
            return new Contacts(in);
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public int getIsLimit() {
        return (this.isLimit);
    }

    public void setIsLimit(int isLimit) {

        this.isLimit= isLimit;

    }

    public Double getLimitAmt() {
        return limitAmt;
    }

    public void setLimitAmt(Double limitAmt) {
        this.limitAmt = limitAmt;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public Double getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(Double creditAmt) {
        this.creditAmt = creditAmt;
    }

    public void addCreditAmt(Double creditAmt) {
        this.creditAmt += creditAmt;
    }
    public Double getDebitAmt() {
        return debitAmt;
    }

    public void setDebitAmt(Double debitAmt) {
        this.debitAmt = debitAmt;
    }

    public void addDebitAmt(Double debitAmt) {
        this.debitAmt += debitAmt;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeValue(this);
    }
}
