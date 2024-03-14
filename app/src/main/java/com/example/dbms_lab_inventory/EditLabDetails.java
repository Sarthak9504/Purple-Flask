package com.example.dbms_lab_inventory;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditLabDetails extends AppCompatActivity {

    private TextInputEditText room_num;
    private TextInputEditText lab_name;
    private TextInputEditText floor;
    private TextInputEditText est_date;
    private TextInputEditText purpose;
    private Button discard;
    private Button save;
    private Button discard_dialog;
    private Button cancel_dialog;
    private ImageView back_cross;
    private ImageView back_dialog;
    private Dialog customDialog;
    private String room_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lab_details);
        init();
        set_edit_text();
        init_dialog();

        discard.setOnClickListener(view -> {
            Intent intent = new Intent(EditLabDetails.this,LabDetailsActivity.class);
            intent.putExtra("Room_no",room_no);
            startActivity(intent);
            finish();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                customDialog.show();

                cancel_dialog.setOnClickListener(view -> customDialog.dismiss());

                discard_dialog.setOnClickListener(view -> {
                    Intent intent = new Intent(EditLabDetails.this,LabDetailsActivity.class);
                    intent.putExtra("Room_no",room_no);
                    startActivity(intent);
                    finish();
                });

                back_dialog.setOnClickListener(view -> customDialog.dismiss());

            }
        };
        EditLabDetails.this.getOnBackPressedDispatcher().addCallback(EditLabDetails.this,callback);


        save.setOnClickListener(view -> {
            if(checker()){
                if(room_no.equals(room_num.getText().toString())){
                    update_child();
                }
                else{
                    change_room();
                }
                Intent intent = new Intent(EditLabDetails.this,LabDetailsActivity.class);
                intent.putExtra("Room_no",room_num.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        back_cross.setOnClickListener(view -> {
            Intent intent = new Intent(EditLabDetails.this,LabDetailsActivity.class);
            intent.putExtra("Room_no",room_num.getText().toString());
            startActivity(intent);
            finish();
        });

        est_date.setOnClickListener(view -> date_picker());

    }

    private void date_picker(){
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EditLabDetails.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                    est_date.setText(date,TextView.BufferType.EDITABLE);

                },
                year, month, day);
        datePickerDialog.show();
    }

    private boolean checker(){
        if(room_num.getText().toString().isEmpty()){
            room_num.setError("Field cannot be empty");
            room_num.requestFocus();
            return false;
        }
        if(floor.getText().toString().isEmpty()){
            floor.setError("Field cannot be empty");
            floor.requestFocus();
            return false;
        }
        if(lab_name.getText().toString().isEmpty()){
            lab_name.setError("Field cannot be empty");
            lab_name.requestFocus();
            return false;
        }
        if(est_date.getText().toString().isEmpty()){
            est_date.setError("Field cannot be empty");
            est_date.requestFocus();
            return false;
        }
        return true;
    }

    private void update_child(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String college_name = sh.getString("college_name"," ");
        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child(sh.getString("Dep_name"," ")).child("Lab Details").child(room_num.getText().toString());
        //LabDetailUtility details = new LabDetailUtility(floor.getText().toString(),lab_name.getText().toString(),est_date.getText().toString(),purpose.getText().toString());
        user_ref.child("floor").setValue(floor.getText().toString());
        user_ref.child("name").setValue(lab_name.getText().toString());
        user_ref.child("purpose").setValue(purpose.getText().toString());
        user_ref.child("est_date").setValue(est_date.getText().toString());
    }

    private void change_room(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String college_name = sh.getString("college_name"," ");
        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child(sh.getString("Dep_name"," ")).child("Lab Details");

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String key = snapshot1.getKey();
                        if(key.equals(room_no)){
                            snapshot1.getRef().removeValue();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LabDetailUtility details = new LabDetailUtility(floor.getText().toString(),lab_name.getText().toString(),est_date.getText().toString(),purpose.getText().toString());
        user_ref.child(room_num.getText().toString()).setValue(details);
    }

    private void set_edit_text(){
        Intent intent = getIntent();
        room_no = intent.getStringExtra("room_no");
        //Log.d("intent",intent.getStringExtra("room_no") + intent.getStringExtra("floor") + intent.getStringExtra("name") + intent.getStringExtra("purpose") + intent.getStringExtra("est_date"));
        room_num.setText(intent.getStringExtra("room_no"), TextView.BufferType.EDITABLE);
        floor.setText(intent.getStringExtra("floor"),TextView.BufferType.EDITABLE);
        lab_name.setText(intent.getStringExtra("name"),TextView.BufferType.EDITABLE);
        purpose.setText(intent.getStringExtra("purpose"),TextView.BufferType.EDITABLE);
        est_date.setText(intent.getStringExtra("est_date"),TextView.BufferType.EDITABLE);
    }


    private void init_dialog(){
        customDialog = new Dialog(EditLabDetails.this);
        customDialog.setContentView(R.layout.edit_dialog_box);
        customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.getWindow().setBackgroundDrawableResource(R.drawable.round_back_white);
        customDialog.setCancelable(true);

        cancel_dialog = customDialog.findViewById(R.id.dialog_cancel);
        discard_dialog = customDialog.findViewById(R.id.dialog_discard);
        back_dialog = customDialog.findViewById(R.id.dialog_back);
    }

    private void init(){
        room_num = findViewById(R.id.room_num_text);
        lab_name = findViewById(R.id.lab_name_text);
        floor = findViewById(R.id.floor_text);
        est_date = findViewById(R.id.est_date_text);
        purpose = findViewById(R.id.purpose_edit_text);
        discard = findViewById(R.id.discardButton);
        save = findViewById(R.id.saveButton);
        back_cross = findViewById(R.id.back_arrow);
    }
}