package com.example.dbms_lab_inventory;

public class LabItem {
    String room_num;
    String lab_type;

    public LabItem(){

    }

    public LabItem(String room_num, String lab_type) {
        this.room_num = room_num;
        this.lab_type = lab_type;
    }

    public String getRoom_num() {
        return room_num;
    }

    public void setRoom_num(String room_num) {
        this.room_num = room_num;
    }

    public String getLab_type() {
        return lab_type;
    }

    public void setLab_type(String lab_type) {
        this.lab_type = lab_type;
    }
}
