package com.example.dbms_lab_inventory;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LabDetailsActivity extends AppCompatActivity {
    private ImageView back_arrow;
    private LinearLayout edit;
    private ImageView image;
    private TextView name;
    private TextView room;
    private TextView floor;
    private TextView est_date;
    private TextView purpose;
    private Button list_button;
    private DatabaseReference user_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_details);
        init();
        //set_bottom_sheet();
        set_text_view();

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        if(sh.getString("usertype","").equals("admin")) {
            edit.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), EditLabDetails.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("department", getIntent().getStringExtra("department"));
                intent.putExtra("floor", floor.getText().toString());
                intent.putExtra("room_no", room.getText().toString());
                intent.putExtra("purpose", purpose.getText().toString());
                intent.putExtra("est_date", est_date.getText().toString());
                startActivity(intent);
            });
        }
        else{
            edit.setVisibility(View.GONE);
        }

        list_button.setOnClickListener(view -> {
            Intent intent = new Intent(LabDetailsActivity.this,LabEquipmentsClass.class);
            intent.putExtra("room_no",room.getText().toString());
            intent.putExtra("department", getIntent().getStringExtra("department"));
            startActivity(intent);
        });

        back_arrow.setOnClickListener(view -> {
            Intent intent = new Intent(LabDetailsActivity.this,MainActivity.class);
            intent.putExtra("department", getIntent().getStringExtra("department"));
            startActivity(intent);
            finish();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(LabDetailsActivity.this,MainActivity.class);
                intent.putExtra("department", getIntent().getStringExtra("department"));
                startActivity(intent);
                finish();
            }
        };
        LabDetailsActivity.this.getOnBackPressedDispatcher().addCallback(LabDetailsActivity.this,callback);
    }



    private void set_text_view(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String college_name = sh.getString("college_name"," ");
        Intent intent = getIntent();
        String room_num = intent.getStringExtra("Room_no");
        String dep = intent.getStringExtra("department");
        room.setText(room_num);
        if(sh.getString("usertype","").equals("admin")){
            user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child("Admin").child(dep).child("Lab Details").child(room_num);
        }
        else{
            user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child("Admin").child(sh.getString("Dep_name"," ")).child("Lab Details").child(room_num);
        }

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String key = snapshot1.getKey();
                        if(key.equals("Equipments")){
                            continue;
                        }
                        String val = snapshot1.getValue(String.class);
                        Log.d("null",val);
                        switch (key) {
                            case "est_date":
                                est_date.setText(val);
                                break;
                            case "floor":
                                floor.setText(val);
                                break;
                            case "name":
                                name.setText(val);
                                break;
                            case "purpose":
                                purpose.setText(val);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void set_bottom_sheet(){
//        BottomSheetBehavior.from(bottomSheet).setPeekHeight(350);
//        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
//    }

    private void init(){
        back_arrow = findViewById(R.id.back_arrow);
        edit = findViewById(R.id.edit_btn);
        image = findViewById(R.id.lab_photo);
        name = findViewById(R.id.name_text);
        room = findViewById(R.id.room_text);
        floor = findViewById(R.id.floor_text);
        est_date = findViewById(R.id.est_text);
        purpose = findViewById(R.id.purpose_text);
        list_button = findViewById(R.id.list_button);
        //bottomSheet = findViewById(R.id.bottomSheet);
    }
}