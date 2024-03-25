package com.example.dbms_lab_inventory;

public class RBUtility {
    String dep_id;
    String state;
    String city;
    int PRN;


    public RBUtility( String dep_id, String state, String city, int PRN) {
        this.dep_id = dep_id;
        this.state = state;
        this.city = city;
        this.PRN = PRN;
    }

    public String getDep_id() {
        return dep_id;
    }

    public void setDep_id(String dep_id) {
        this.dep_id = dep_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
