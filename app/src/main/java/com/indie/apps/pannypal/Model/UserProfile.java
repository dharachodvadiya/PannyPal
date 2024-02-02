package com.indie.apps.pannypal.Model;

public class UserProfile {

    private String id;
    private String name;
    private String email;
    private String profileURL;
    private String creditAmt;
    private String debitAmt;
    private String totalAmt;

    public UserProfile(String id, String name, String email, String profileURL, String creditAmt, String debitAmt, String totalAmt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileURL = profileURL;
        this.creditAmt = creditAmt;
        this.debitAmt = debitAmt;
        this.totalAmt = totalAmt;
    }

    public UserProfile(String name, String email, String profileURL, Double creditAmt, Double debitAmt, Double totalAmt) {
        this.name = name;
        this.email = email;
        this.profileURL = profileURL;
        this.creditAmt = String.valueOf(creditAmt);
        this.debitAmt = String.valueOf(debitAmt);
        this.totalAmt = String.valueOf(totalAmt);
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

    public Double getCreditAmt() {
        return Double.parseDouble(creditAmt);
    }

    public void setCreditAmt(Double creditAmt) {
        this.creditAmt = String.valueOf(creditAmt);
    }

    public Double getDebitAmt() {
        return Double.parseDouble(debitAmt);
    }

    public void setDebitAmt(Double debitAmt) {
        this.debitAmt = String.valueOf(debitAmt);
    }

    public Double getTotalAmt() {
        return Double.parseDouble(totalAmt);
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = String.valueOf(totalAmt);
    }
}
