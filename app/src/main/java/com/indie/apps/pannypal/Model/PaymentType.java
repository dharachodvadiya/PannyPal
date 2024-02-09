package com.indie.apps.pannypal.Model;

public class PaymentType {

    private long id;
    private String type;

    public PaymentType(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public PaymentType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
