package com.example.dbms_lab_inventory;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EquipmentDetails extends AppCompatActivity {

    private ImageView back_arrow;
    private LinearLayout edit;
    private ImageView image;
    private TextView name;
    private TextView date;
    private TextView price;
    private TextView donor;
    private TextView qty;
    private TextView purpose;
    private Button pdf_button;
    private DatabaseReference user_ref;
    private String name_txt;
    private String room_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_details);
        init();
        //set_bottom_sheet();
        set_text_view();

        edit.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),EditEquipments.class);
            intent.putExtra("name",name.getText().toString());
            intent.putExtra("date",date.getText().toString());
            intent.putExtra("price",price.getText().toString());
            intent.putExtra("donor",donor.getText().toString());
            intent.putExtra("qty",qty.getText().toString());
            intent.putExtra("purpose",purpose.getText().toString());
            intent.putExtra("room_no",getIntent().getStringExtra("room_no"));
            startActivity(intent);
        });

        pdf_button.setOnClickListener(view -> {
            view_pdf();
        });

        back_arrow.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),LabEquipmentsClass.class);
            intent.putExtra("name",name_txt);
            intent.putExtra("room_no",room_num);
            startActivity(intent);
            finish();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getApplicationContext(),LabEquipmentsClass.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("room_no",room_num);
                startActivity(intent);
                finish();
            }
        };
        EquipmentDetails.this.getOnBackPressedDispatcher().addCallback(EquipmentDetails.this,callback);

    }

    private void view_pdf(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String college_name = sh.getString("college_name"," ");
        Intent intent = getIntent();

        user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child(sh.getString("Dep_name"," "));
        user_ref = user_ref.child("Lab Details")
                .child(intent.getStringExtra("room_no"))
                .child("Equipments").child(intent.getStringExtra("name"));

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String url = null;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        String val = snapshot1.getValue(String.class);
                        String key = snapshot1.getKey();
                        if(key.equals("url")){
                            url = val;
                            break;
                        }
                    }
                    if(url != null) {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setTitle("File Download");
                        request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_DOWNLOADS, "downloads");

                        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        downloadManager.enqueue(request);

                        Toast.makeText(EquipmentDetails.this, "File downloaded successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void set_text_view(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String college_name = sh.getString("college_name"," ");
        Intent intent = getIntent();
        name_txt = intent.getStringExtra("name");
        room_num = intent.getStringExtra("room_no");
        //Log.d("room_num",room_num);
        name.setText(name_txt);
        user_ref = FirebaseDatabase.getInstance().getReference("College or University")
                .child(college_name).child(sh.getString("Dep_name"," ")).child("Lab Details")
                .child(room_num)
                .child("Equipments").child(name_txt);

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String key = snapshot1.getKey();
                        String val = snapshot1.getValue(String.class);
                        Log.d("null",val);

                        switch (key) {
                            case "date":
                                date.setText(val);
                                break;
                            case "donor":
                                donor.setText(val);
                                break;
                            case "price":
                                price.setText(val);
                                break;
                            case "purpose":
                                purpose.setText(val);
                                break;
                            case "qty":
                                qty.setText(val);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init(){
        back_arrow = findViewById(R.id.back_arrow);
        edit = findViewById(R.id.edit_btn);
        image = findViewById(R.id.lab_photo);
        name = findViewById(R.id.name_text);
        date = findViewById(R.id.purchase_text);
        price = findViewById(R.id.price_text);
        donor = findViewById(R.id.donor_text);
        qty = findViewById(R.id.qty_text);
        purpose = findViewById(R.id.purpose_text);
        pdf_button = findViewById(R.id.pdf_button);
    }
}