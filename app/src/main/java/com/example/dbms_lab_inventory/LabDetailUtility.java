package com.example.dbms_lab_inventory;

public class LabDetailUtility {
    String floor;
    String name;
    String est_date;
    String purpose;

    public LabDetailUtility(String floor, String name, String est_date, String purpose) {
        this.floor = floor;
        this.name = name;
        this.est_date = est_date;
        this.purpose = purpose;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEst_date() {
        return est_date;
    }

    public void setEst_date(String est_date) {
        this.est_date = est_date;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
