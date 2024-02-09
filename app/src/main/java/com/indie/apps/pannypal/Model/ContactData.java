package com.indie.apps.pannypal.Model;

public class ContactData {
    private long id;
    private long c_id;
    private long p_id;
    private int type; // credit = 1 debit = -1
    private Double amount;
    private String remark;
    private long dateTime;

    public ContactData(long id, long c_id, long p_id, int type, Double amount, String remark, long dateTime) {
        this.id = id;
        this.c_id = c_id;
        this.p_id = p_id;
        this.type = type;
        this.amount = amount;
        this.remark = remark;
        this.dateTime = dateTime;
    }

    public ContactData(long c_id, long p_id, int type, Double amount, String remark, long dateTime) {
        this.c_id = c_id;
        this.p_id = p_id;
        this.type = type;
        this.amount = amount;
        this.remark = remark;
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getC_id() {
        return c_id;
    }

    public void setC_id(long c_id) {
        this.c_id = c_id;
    }

    public long getP_id() {
        return p_id;
    }

    public void setP_id(long p_id) {
        this.p_id = p_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
