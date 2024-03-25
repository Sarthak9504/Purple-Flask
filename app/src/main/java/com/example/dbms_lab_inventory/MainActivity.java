package com.example.dbms_lab_inventory;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClickListener{
    private FloatingActionButton add_lab_button;
    private Dialog customDialog;
    private ImageView menu;
    private ImageView notifis;
    private ImageView back_cross;
    private TextView dep_name;
    private TextView college_name;
    private TextView lab_count;
    private TextView equip_count;
    private TextInputEditText room_num;
    private TextInputEditText lab_name;
    private TextInputEditText floor;
    private TextInputEditText est_date;
    private TextInputEditText purpose;
    private Button cancel;
    private Button done;
    private DatabaseReference user_ref;
    private RecyclerView recyclerView;
    private List<LabItem> list;
    private LabAdapter adapter;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();
        initDialogBox();
        add_lab_details();
        //Toast.makeText(this, "Calling list here", Toast.LENGTH_SHORT).show();
        fill_list();
        set_text_view();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        if(sh.getString("usertype","").equals("admin")) {
            add_lab_button.setOnClickListener(view -> customDialog.show());

            back_cross.setOnClickListener(view -> customDialog.dismiss());

            cancel.setOnClickListener(view -> customDialog.dismiss());

            done.setOnClickListener(view -> add_data_to_firebase());

            est_date.setOnClickListener(view -> date_picker());

            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Intent intent = new Intent(MainActivity.this,AdminDashboard.class);
                    intent.putExtra("department", getIntent().getStringExtra("department"));
                    startActivity(intent);
                    finish();
                }
            };
            MainActivity.this.getOnBackPressedDispatcher().addCallback(MainActivity.this,callback);
        }
        else{
            add_lab_button.setVisibility(View.GONE);
        }
    }

    private void date_picker(){
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                    est_date.setText(date,TextView.BufferType.EDITABLE);

                },
                year, month, day);
        datePickerDialog.show();
    }

    private void set_text_view(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        college_name.setText(sh.getString("college_name"," "));
        if(sh.getString("usertype","").equals("admin")) {
            dep_name.setText(getIntent().getStringExtra("department"));
        }
        else{
            dep_name.setText(sh.getString("Dep_name",""));
        }
    }

    private void fill_list(){
        list.clear();
        Toast.makeText(this, "In fill list", Toast.LENGTH_SHORT).show();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String college_name = sh.getString("college_name"," ");
        DatabaseReference rc_view_ref;

        if(sh.getString("usertype","").equals("admin")){
            rc_view_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child("Admin").child(getIntent().getStringExtra("department")).child("Lab Details");
        }
        else{
            rc_view_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child("Admin").child(sh.getString("Dep_name"," ")).child("Lab Details");
            //Toast.makeText(this, "In students", Toast.LENGTH_SHORT).show();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rc_view_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String room = dataSnapshot.getKey();
                        for(DataSnapshot labSnapshot : dataSnapshot.getChildren())
                        {
                            String key = labSnapshot.getKey();
                            if(key.equals("Equipments")){
                                continue;
                            }
                            String value = labSnapshot.getValue(String.class);

                            assert key != null;
                            if(key.equals("name")){
                                list.add(new LabItem(room,value));
                            }
                        }
                    }
                    if(adapter!=null){
                        Log.d("not null","here");
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        Log.d("null","here");
                        adapter = new LabAdapter(MainActivity.this,list,MainActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void add_data_to_firebase(){
        list.clear();
        String room_no = room_num.getText().toString().trim();
        String floor_no = floor.getText().toString().trim();
        String name = lab_name.getText().toString().trim();
        String date = est_date.getText().toString().trim();
        String purpose_txt = purpose.getText().toString().trim();

        if(checker()){
            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String college_name = sh.getString("college_name"," ");
            Log.d("college name",college_name);

            user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child("Admin").child(getIntent().getStringExtra("department"));
            user_ref.child("Lab Details").child(room_no).setValue(new LabDetailUtility(floor_no,name,date,purpose_txt));

            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putInt("Lab_count",list.size());
            myEdit.apply();
            set_text_view();

            customDialog.dismiss();
        }
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

    private void initDialogBox(){
        customDialog = new Dialog(MainActivity.this);
        customDialog.setContentView(R.layout.custom_dialog_box);
        customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.getWindow().setBackgroundDrawableResource(R.drawable.round_back_white);
        customDialog.setCancelable(true);

        room_num = customDialog.findViewById(R.id.room_num_text);
        lab_name = customDialog.findViewById(R.id.lab_name_text);
        floor = customDialog.findViewById(R.id.floor_text);
        est_date = customDialog.findViewById(R.id.est_date_text);
        purpose = customDialog.findViewById(R.id.purpose_edit_text);
        cancel = customDialog.findViewById(R.id.cancelButton);
        done = customDialog.findViewById(R.id.doneButton);
        back_cross = customDialog.findViewById(R.id.back_cross);
    }

    private void add_lab_details(){
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.baseline_add_24);
        if (drawable != null) {
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        }

        add_lab_button.setImageDrawable(drawable);
    }

    private void init(){
        add_lab_button = findViewById(R.id.floating_button);
        menu = findViewById(R.id.menu);
        college_name = findViewById(R.id.college_name);
        dep_name = findViewById(R.id.dep_name);
        notifis = findViewById(R.id.notifis);
        lab_count = findViewById(R.id.lab_count_text);
        equip_count = findViewById(R.id.equipment_count_text);
        recyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();
    }


    protected void onResume() {
        super.onResume();
        if (!isTaskRoot() && customDialog != null && !customDialog.isShowing() && alertDialog != null && alertDialog.isShowing()) {
            //Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
            fill_list();
        }
    }

    @Override
    public void onClick(LabItem labItem) {
        Intent intent = new Intent(MainActivity.this,LabDetailsActivity.class);
        intent.putExtra("Room_no",labItem.room_num);
        intent.putExtra("department",getIntent().getStringExtra("department"));
        Log.d("room",labItem.room_num);
        startActivity(intent);
    }

    @Override
    public void onLongPress(LabItem labItem) {
        SharedPreferences sh1 = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        if(sh1.getString("usertype","").equals("admin")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure you want to delete this lab");
            builder.setTitle("Alert !");
            builder.setCancelable(false);

            builder.setPositiveButton("Yes", (dialog, which) -> {
                list.clear();
                SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                String college_name = sh.getString("college_name", " ");
                user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child("Admin").child(getIntent().getStringExtra("department")).child("Lab Details");

                user_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                String key = snapshot1.getKey();
                                if (key.equals(labItem.room_num)) {
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
            });

            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.cancel();
            });

            alertDialog = builder.create();
            alertDialog.show();
        }
    }
}