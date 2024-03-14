package com.example.dbms_lab_inventory;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditEquipments extends AppCompatActivity {

    private TextInputEditText name;
    private TextInputEditText date;
    private TextInputEditText price;
    private TextInputEditText qty;
    private TextInputEditText donor;
    private TextInputEditText purpose;
    private Button discard;
    private Button save;
    private Button discard_dialog;
    private Button cancel_dialog;
    private ImageView back_cross;
    private ImageView back_dialog;
    private Dialog customDialog;
    private String name_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_equipments);
        init();
        init_dialog();
        set_edit_text();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date_picker();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker()){
                    if(name_txt.equals(name.getText().toString())){
                        update_child();
                    }
                    else{
                        change_room();
                    }
                    Intent intent = new Intent(EditEquipments.this,EquipmentDetails.class);
                    intent.putExtra("name",name.getText().toString());
                    intent.putExtra("room_no",getIntent().getStringExtra("room_no"));
                    startActivity(intent);
                    finish();
                }
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditEquipments.this,EquipmentDetails.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("room_no",getIntent().getStringExtra("room_no"));
                startActivity(intent);
                finish();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                customDialog.show();

                cancel_dialog.setOnClickListener(view -> customDialog.dismiss());

                discard_dialog.setOnClickListener(view -> {
                    Intent intent = new Intent(EditEquipments.this,EquipmentDetails.class);
                    intent.putExtra("room_no",getIntent().getStringExtra("room_no"));
                    intent.putExtra("name",name.getText().toString());
                    startActivity(intent);
                    finish();
                });

                back_dialog.setOnClickListener(view -> customDialog.dismiss());

            }
        };
        EditEquipments.this.getOnBackPressedDispatcher().addCallback(EditEquipments.this,callback);

        back_cross.setOnClickListener(view -> {
            Intent intent = new Intent(EditEquipments.this,EquipmentDetails.class);
            intent.putExtra("room_no",getIntent().getStringExtra("room_no"));
            intent.putExtra("name",name.getText().toString());
            startActivity(intent);
            finish();
        });
    }

    private void change_room(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String college_name = sh.getString("college_name"," ");
        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("College or University")
                .child(college_name).child(sh.getString("Dep_name"," ")).child("Lab Details")
                .child(getIntent().getStringExtra("room_no")).child("Equipments");

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String key = snapshot1.getKey();
                        if(key.equals(name_txt)){
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

        EquipmentUtil details = new EquipmentUtil(edit_to_string(date),edit_to_string(price),edit_to_string(purpose),edit_to_string(donor),edit_to_string(qty));
        user_ref.child(name.getText().toString()).setValue(details);
    }

    private String edit_to_string(TextInputEditText edit_text){
        return edit_text.getText().toString();
    }

    private void update_child(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String college_name = sh.getString("college_name"," ");
        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("College or University")
                .child(college_name).child(sh.getString("Dep_name"," ")).child("Lab Details")
                .child(getIntent().getStringExtra("room_no")).child("Equipments").child(name_txt);
        //LabDetailUtility details = new LabDetailUtility(floor.getText().toString(),lab_name.getText().toString(),est_date.getText().toString(),purpose.getText().toString());
        user_ref.child("date").setValue(date.getText().toString());
        user_ref.child("donor").setValue(donor.getText().toString());
        user_ref.child("price").setValue(price.getText().toString());
        user_ref.child("purpose").setValue(purpose.getText().toString());
        user_ref.child("qty").setValue(qty.getText().toString());
    }

    private boolean checker(){
        if(name.getText().toString().isEmpty()){
            name.setError("Field cannot be empty");
            name.requestFocus();
            return false;
        }
        if(price.getText().toString().isEmpty()){
            price.setError("Field cannot be empty");
            price.requestFocus();
            return false;
        }
        if(date.getText().toString().isEmpty()){
            date.setError("Field cannot be empty");
            date.requestFocus();
            return false;
        }
        if(donor.getText().toString().isEmpty()){
            donor.setError("Field cannot be empty");
            donor.requestFocus();
            return false;
        }
        if(qty.getText().toString().isEmpty()){
            qty.setError("Field cannot be empty");
            qty.requestFocus();
            return false;
        }
        if(purpose.getText().toString().isEmpty()){
            purpose.setError("Field cannot be empty");
            purpose.requestFocus();
            return false;
        }
        return true;
    }

    private void set_edit_text(){
        Intent intent = getIntent();
        name_txt = intent.getStringExtra("name");
        //Log.d("intent",intent.getStringExtra("room_no") + intent.getStringExtra("floor") + intent.getStringExtra("name") + intent.getStringExtra("purpose") + intent.getStringExtra("est_date"));
        name.setText(intent.getStringExtra("name"), TextView.BufferType.EDITABLE);
        date.setText(intent.getStringExtra("date"),TextView.BufferType.EDITABLE);
        price.setText(intent.getStringExtra("price"),TextView.BufferType.EDITABLE);
        purpose.setText(intent.getStringExtra("purpose"),TextView.BufferType.EDITABLE);
        donor.setText(intent.getStringExtra("donor"),TextView.BufferType.EDITABLE);
        qty.setText(intent.getStringExtra("qty"),TextView.BufferType.EDITABLE);
    }

    private void date_picker(){
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EditEquipments.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String date_txt = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                    date.setText(date_txt, TextView.BufferType.EDITABLE);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void init_dialog(){
        customDialog = new Dialog(EditEquipments.this);
        customDialog.setContentView(R.layout.edit_dialog_box);
        customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.getWindow().setBackgroundDrawableResource(R.drawable.round_back_white);
        customDialog.setCancelable(true);

        cancel_dialog = customDialog.findViewById(R.id.dialog_cancel);
        discard_dialog = customDialog.findViewById(R.id.dialog_discard);
        back_dialog = customDialog.findViewById(R.id.dialog_back);
    }


    private void init(){
        name = findViewById(R.id.name_text);
        date = findViewById(R.id.date_text);
        price = findViewById(R.id.price_text);
        qty = findViewById(R.id.qty_text);
        donor = findViewById(R.id.donor_text);
        purpose = findViewById(R.id.purpose_edit_text);
        discard = findViewById(R.id.discardButton);
        save = findViewById(R.id.saveButton);
        back_cross = findViewById(R.id.back_arrow);
    }
}