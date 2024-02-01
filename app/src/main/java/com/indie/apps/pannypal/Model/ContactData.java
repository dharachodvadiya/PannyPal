package com.indie.apps.pannypal.Model;

public class ContactData {
    private String id;
    private String c_id;
    private String p_id;
    private String type; //[c/d]
    private String amount;
    private String remark;
    private String date;
    private String time;

    public ContactData(String c_id, String p_id, String type, String amount, String remark, String date, String time) {
        this.c_id = c_id;
        this.p_id = p_id;
        this.type = type;
        this.amount = amount;
        this.remark = remark;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAmount() {
        return Long.parseLong(amount);
    }

    public void setAmount(long amount) {
        this.amount = String.valueOf(amount);
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
