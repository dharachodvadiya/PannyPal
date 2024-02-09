package com.indie.apps.pannypal.Model;

import android.renderscript.Double4;

public class Contacts {

    private long id;
    private String name;
    private String phno;
    private int isLimit; // 0= false, 1= true
    private Double limitAmt;
    private String profileURL;
    private Double creditAmt;
    private Double debitAmt;
    private Double totalAmt;
    private long dateTime;

    public Contacts(long id, String name, String phno, int isLimit, Double limitAmt, String profileURL, Double creditAmt, Double debitAmt, Double totalAmt, long dateTime) {
        this.id = id;
        this.name = name;
        this.phno = phno;
        this.isLimit = isLimit;
        this.limitAmt = limitAmt;
        this.profileURL = profileURL;
        this.creditAmt = creditAmt;
        this.debitAmt = debitAmt;
        this.totalAmt = totalAmt;
        this.dateTime = dateTime;
    }

    public Contacts(String name, String phno, int isLimit, Double limitAmt, String profileURL, Double creditAmt, Double debitAmt, Double totalAmt, long dateTime) {
        this.name = name;
        this.phno = phno;
        this.isLimit = isLimit;
        this.limitAmt = limitAmt;
        this.profileURL = profileURL;
        this.creditAmt = creditAmt;
        this.debitAmt = debitAmt;
        this.totalAmt = totalAmt;
        this.dateTime = dateTime;
    }

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

    public Double getDebitAmt() {
        return debitAmt;
    }

    public void setDebitAmt(Double debitAmt) {
        this.debitAmt = debitAmt;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
