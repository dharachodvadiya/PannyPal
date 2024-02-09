package com.indie.apps.pannypal.Model;

public class UserProfile {

    private long id;
    private String name;
    private String email;
    private String profileURL;
    private Double creditAmt;
    private Double debitAmt;
    private Double totalAmt;

    public UserProfile(long id, String name, String email, String profileURL, Double creditAmt, Double debitAmt, Double totalAmt) {
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
        this.creditAmt = creditAmt;
        this.debitAmt = debitAmt;
        this.totalAmt = totalAmt;
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
}
