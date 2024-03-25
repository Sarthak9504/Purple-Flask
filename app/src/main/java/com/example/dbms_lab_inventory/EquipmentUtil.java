package com.example.dbms_lab_inventory;

public class EquipmentUtil {
    String date;
    String price;
    String purpose;
    String donor;
    String qty;
    String url;
    String remark;

    public EquipmentUtil(String date, String price, String purpose, String donor, String qty) {
        this.date = date;
        this.price = price;
        this.purpose = purpose;
        this.donor = donor;
        this.qty = qty;
    }

    public EquipmentUtil(String date, String price, String purpose, String donor, String qty, String remark,String url) {
        this.date = date;
        this.price = price;
        this.purpose = purpose;
        this.donor = donor;
        this.qty = qty;
        this.url = url;
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {this.price = price;}

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
