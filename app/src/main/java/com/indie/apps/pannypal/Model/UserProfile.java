package com.indie.apps.pannypal.Model;

public class UserProfile {

    private long id;
    private String name;
    private String email;
    private String profileURL;
    private Double creditAmt;
    private Double debitAmt;

    public UserProfile(long id, String name, String email, String profileURL, Double creditAmt, Double debitAmt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileURL = profileURL;
        this.creditAmt = creditAmt;
        this.debitAmt = debitAmt;
    }

    public UserProfile(String name, String email, String profileURL, Double creditAmt, Double debitAmt) {
        this.name = name;
        this.email = email;
        this.profileURL = profileURL;
        this.creditAmt = creditAmt;
        this.debitAmt = debitAmt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}

