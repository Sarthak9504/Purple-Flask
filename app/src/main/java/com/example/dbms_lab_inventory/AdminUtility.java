package com.example.dbms_lab_inventory;

public class AdminUtility {
    String name;
    String mail;
    String state;
    String city;
    String department_name;
    String PRN;

    public AdminUtility(String name, String mail, String state, String city,String PRN) {
        this.name = name;
        this.mail = mail;
        this.state = state;
        this.city = city;
        this.PRN = PRN;
    }

    public AdminUtility(String Dep_Name){
        department_name = Dep_Name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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
