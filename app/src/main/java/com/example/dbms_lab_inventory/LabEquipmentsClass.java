package com.example.dbms_lab_inventory;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class LabEquipmentsClass extends AppCompatActivity implements ItemClickListener2{

    private ImageView back;
    private FloatingActionButton button;
    private RecyclerView recyclerView;
    private Dialog customDialog;
    private TextInputEditText equip_name;
    private TextInputEditText purchase_date;
    private TextInputEditText purchase_price;
    private TextInputEditText qty;
    private TextInputEditText donor;
    private TextInputEditText purpose;
    private Button save;
    private Button cancel;
    private TextView upload_button;
    private CheckBox checkBox;
    private DatabaseReference user_ref;
    private List<EquipmentItem> list;
    private EquipmentAdapter adapter;
    private ActivityResultLauncher<String> pickPdfLauncher;
    private String url;
    FirebaseStorage storage;
    StorageReference storageRef;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_equipments_class);
        init();
        init_dialog();
        add_lab_details();
        fill_list();

        pickPdfLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        // Call the upload logic here
                        uploadFile(uri);
                    }
                });

        button.setOnClickListener(view -> {
            customDialog.show();
            checkBox.setChecked(false);
        });

        cancel.setOnClickListener(view -> customDialog.dismiss());

        back.setOnClickListener(view -> customDialog.dismiss());

        purchase_date.setOnClickListener(view -> date_picker());

        save.setOnClickListener(view -> {
                if(checkBox.isChecked()) {
                    add_to_firebase();
                }
                else {
                    Toast.makeText(this, "Please upload file", Toast.LENGTH_SHORT).show();
                }
        });

        upload_button.setOnClickListener(view -> pickPdfLauncher.launch("application/pdf"));

    }

    private void fill_list(){
        list.clear();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String college_name = sh.getString("college_name"," ");
        Intent intent = getIntent();
        Log.d("college name",college_name);

        user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child(sh.getString("Dep_name"," "));
        user_ref = user_ref.child("Lab Details")
                    .child(intent.getStringExtra("room_no"))
                    .child("Equipments");

        recyclerView.setLayoutManager(new LinearLayoutManager(LabEquipmentsClass.this));

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String name = dataSnapshot.getKey();
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            String key = snapshot1.getKey();
                            String value = snapshot1.getValue(String.class);

                            if(key.equals("qty")){
                                list.add(new EquipmentItem("image",name,value));
                            }
                        }
                    }

                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        adapter = new EquipmentAdapter(LabEquipmentsClass.this,list,LabEquipmentsClass.this);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadFile(Uri uri) {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        StorageReference pdfRef = storageRef.child("pdfs/" + UUID.randomUUID().toString() + ".pdf");

        pdfRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG, "File uploaded successfully");
                    pdfRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                        url = downloadUrl.toString();
                        Log.d(TAG, "Download URL: " + url);
                    });
                    Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    checkBox.setChecked(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error uploading file", e);
                    checkBox.setChecked(false);
                });
    }

    private void date_picker(){
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                LabEquipmentsClass.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                    purchase_date.setText(date,TextView.BufferType.EDITABLE);

                },
                year, month, day);
        datePickerDialog.show();
    }

    private void add_to_firebase(){
        list.clear();
        if(checker()){
            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String college_name = sh.getString("college_name"," ");
            Intent intent = getIntent();
            Log.d("college name",college_name);

            user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child(sh.getString("Dep_name"," "));
            user_ref.child("Lab Details")
                    .child(intent.getStringExtra("room_no"))
                    .child("Equipments").child(edit_to_string(equip_name))
                    .setValue(new EquipmentUtil(edit_to_string(purchase_date),edit_to_string(purchase_price),edit_to_string(purpose),edit_to_string(donor),edit_to_string(qty),url));

            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putInt("Lab_count",list.size());
            myEdit.putBoolean("isFirst",false);
            myEdit.apply();

            customDialog.dismiss();

            Toast.makeText(this, "Equipment added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private String edit_to_string(TextInputEditText edit_text){
        return edit_text.getText().toString();
    }


    private boolean checker(){
        if(equip_name.getText().toString().isEmpty()){
            equip_name.setError("Field cannot be empty");
            equip_name.requestFocus();
            return false;
        }
        if(purchase_price.getText().toString().isEmpty()){
            purchase_price.setError("Field cannot be empty");
            purchase_price.requestFocus();
            return false;
        }
        if(purchase_date.getText().toString().isEmpty()){
            purchase_date.setError("Field cannot be empty");
            purchase_date.requestFocus();
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
        if(donor.getText().toString().isEmpty()){
            donor.setError("Field cannot be empty");
            donor.requestFocus();
            return false;
        }
        return true;
    }

    private void add_lab_details(){
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.baseline_add_24);
        if (drawable != null) {
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        }

       button.setImageDrawable(drawable);
    }

    private void init_dialog(){
        customDialog = new Dialog(LabEquipmentsClass.this);
        customDialog.setContentView(R.layout.equipments_dialog);
        customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.getWindow().setBackgroundDrawableResource(R.drawable.round_back_white);
        customDialog.setCancelable(true);

        equip_name = customDialog.findViewById(R.id.equip_text);
        purchase_date = customDialog.findViewById(R.id.purchase_text);
        purchase_price = customDialog.findViewById(R.id.price_text);
        qty = customDialog.findViewById(R.id.qty_text);
        donor = customDialog.findViewById(R.id.donor_text);
        purpose = customDialog.findViewById(R.id.purpose_edit_text);
        save = customDialog.findViewById(R.id.doneButton);
        cancel = customDialog.findViewById(R.id.cancelButton);
        upload_button = customDialog.findViewById(R.id.upload_pdf);
        checkBox = customDialog.findViewById(R.id.checkbox);
        checkBox.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.lavender)));
        checkBox.setChecked(false);
    }
    private void init(){
        back = findViewById(R.id.back_arrow);
        button = findViewById(R.id.floating_button);
        recyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();
    }

    protected void onResume() {
        super.onResume();
        if (!isTaskRoot() && customDialog != null && !customDialog.isShowing() && alertDialog != null && alertDialog.isShowing()) {
            fill_list();
        }
    }


    @Override
    public void onClick(EquipmentItem item) {
        Intent intent = new Intent(LabEquipmentsClass.this, EquipmentDetails.class);
        Intent intent1 = getIntent();
        intent.putExtra("name",item.name);
        intent.putExtra("room_no", intent1.getStringExtra("room_no"));
        Toast.makeText(this, "room no" + intent1.getStringExtra("room_no"), Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    public void onLongPress(EquipmentItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LabEquipmentsClass.this);
        builder.setMessage("Are you sure you want to delete this Equipment");
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        Intent intent = getIntent();

        builder.setPositiveButton("Yes", (dialog, which) -> {
            list.clear();
            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String college_name = sh.getString("college_name"," ");

            user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name)
                    .child(sh.getString("Dep_name"," ")).child("Lab Details")
                    .child(intent.getStringExtra("room_no"))
                    .child("Equipments").child(edit_to_string(equip_name));

            user_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key = snapshot1.getKey();
                            if(key.equals(item.name)){
                                snapshot1.getRef().removeValue();
                                Toast.makeText(LabEquipmentsClass.this, "Equipment deleted successfully", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.cancel();
        });

        alertDialog = builder.create();
        alertDialog.show();
    }
}