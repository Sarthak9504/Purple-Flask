package com.example.dbms_lab_inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();
        initDialogBox();
        add_lab_details();
        fill_list();
        set_text_view();

        add_lab_button.setOnClickListener(view -> customDialog.show());

        back_cross.setOnClickListener(view -> customDialog.dismiss());

        cancel.setOnClickListener(view -> customDialog.dismiss());

        done.setOnClickListener(view -> add_data_to_firebase());
    }

    private void set_text_view(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String depName = "Department of " + sh.getString("Dep_name"," ");
        String labCount = sh.getInt("Lab_count",0) + "";
        String equipCount = sh.getInt("Equipment_count",0) + "";
        lab_count.setText(labCount);
        equip_count.setText(equipCount);
        college_name.setText(sh.getString("college_name"," "));
        dep_name.setText(depName);
    }

    private void fill_list(){
        list.clear();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        Log.d("boolean",sh.getBoolean("isFirst",false)+"");
        if(!sh.getBoolean("isFirst",false)){
            Log.d("boolean","haaaaaaaaaaaaaaaaaaaaaaaaad");
            return;
        }
        String college_name = sh.getString("college_name"," ");
        DatabaseReference rc_view_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child(sh.getString("Dep_name"," ")).child("Lab Details");

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
                        adapter = new LabAdapter(MainActivity.this,list);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putBoolean("isFirst",false);
        myEdit.apply();
        Log.d("boolean",sh.getBoolean("isFirst",false)+"");
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

            user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child(sh.getString("Dep_name"," "));
            user_ref.child("Lab Details").child(room_no).setValue(new LabDetailUtility(floor_no,name,date,purpose_txt));

            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putInt("Lab_count",sh.getInt("Lab_count",0)+1);
            myEdit.putBoolean("isFirst",false);
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
        if (!isTaskRoot() && customDialog != null && !customDialog.isShowing()) {
            fill_list();
        }
    }
}