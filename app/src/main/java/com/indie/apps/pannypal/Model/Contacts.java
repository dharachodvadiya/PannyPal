package com.indie.apps.pannypal.Model;

public class Contacts {

    private String id;
    private String name;
    private String phno;
    private String isLimit;
    private String limitAmt;
    private String profileURL;
    private String creditAmt;
    private String debitAmt;
    private String totalAmt;
    private String date;
    private String time;

    public Contacts(String id, String name, String phno, String isLimit, String limitAmt, String profileURL, String creditAmt, String debitAmt, String totalAmt, String date, String time) {
        this.id = id;
        this.name = name;
        this.phno = phno;
        this.isLimit = isLimit;
        this.limitAmt = limitAmt;
        this.profileURL = profileURL;
        this.creditAmt = creditAmt;
        this.debitAmt = debitAmt;
        this.totalAmt = totalAmt;
        this.date = date;
        this.time = time;
    }

    public Contacts(String name, String phno, String isLimit, String limitAmt, String profileURL, String creditAmt, String debitAmt, String totalAmt, String date, String time) {
        this.name = name;
        this.phno = phno;
        this.isLimit = isLimit;
        this.limitAmt = limitAmt;
        this.profileURL = profileURL;
        this.creditAmt = creditAmt;
        this.debitAmt = debitAmt;
        this.totalAmt = totalAmt;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Boolean getIsLimit() {
        return (this.isLimit == "1");
    }

    public void setIsLimit(boolean isLimit) {

        if(isLimit) this.isLimit="1"; else this.isLimit="0";

    }

    public String getLimitAmt() {
        return limitAmt;
    }

    public void setLimitAmt(String limitAmt) {
        this.limitAmt = limitAmt;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public long getCreditAmt() {
        return Long.parseLong(creditAmt);
    }

    public void setCreditAmt(long creditAmt) {
        this.creditAmt = String.valueOf(creditAmt);
    }

    public long getDebitAmt() {
        return Long.parseLong(debitAmt);
    }

    public void setDebitAmt(long debitAmt) {
        this.debitAmt = String.valueOf(debitAmt);
    }

    public long getTotalAmt() {
        return Long.parseLong(totalAmt);
    }

    public void setTotalAmt(long totalAmt) {
        this.totalAmt = String.valueOf(totalAmt);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
