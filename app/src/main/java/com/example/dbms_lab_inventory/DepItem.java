package com.example.dbms_lab_inventory;

public class DepItem {
    String Dep_name;

    public DepItem(String dep_name) {
        Dep_name = dep_name;
    }

    public String getDep_name() {
        return Dep_name;
    }

    public void setDep_name(String dep_name) {
        Dep_name = dep_name;
    }
}
