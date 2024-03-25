package com.example.dbms_lab_inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.List;

public class AdminDashboard extends AppCompatActivity implements ItemClickListener3{
    TextView name_text;
    TextView college_text;
    TextView email_text;
    TextView city_text;
    TextView state_text;
    TextView prn_text;
    RecyclerView recyclerView;
    FloatingActionButton button;
    Dialog customDialog;
    TextInputEditText dep_name;
    ImageView back;
    Button add;
    List<DepItem> list;
    DepAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference user_ref;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        init();
        init_dialog_box();
        set_text_view();
        add_lab_details();
        fillList();

        button.setOnClickListener(view -> customDialog.show());

        add.setOnClickListener(view -> add_data());

        back.setOnClickListener(view -> customDialog.dismiss());


    }

    private void fillList(){
        list.clear();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        user_ref = firebaseDatabase.getReference("College or University");
        user_ref = user_ref.child(sh.getString("college_name"," ")).child("Admin");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String key = snapshot1.getKey();
                        if (!key.equals("city") && !key.equals("name") && !key.equals("mail") && !key.equals("state")) {
                            list.add(new DepItem(key));
                        }
                    }

                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        adapter = new DepAdapter(AdminDashboard.this,list,AdminDashboard.this);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void add_data(){
        if(dep_name.getText().toString().isEmpty()){
            dep_name.requestFocus();
            dep_name.setError("Field cannot be empty");
        }
        else{
            list.clear();
            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            firebaseDatabase = FirebaseDatabase.getInstance();
            user_ref = firebaseDatabase.getReference("College or University");
            user_ref.child(sh.getString("college_name"," ")).child("Admin").child(dep_name.getText().toString()).setValue(true);
            customDialog.dismiss();
        }
    }

    private void add_lab_details(){
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.baseline_add_24);
        if (drawable != null) {
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        }

        button.setImageDrawable(drawable);
    }

    private void set_text_view(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        college_text.setText(sh.getString("college_name"," "));
        name_text.setText(sh.getString("admin_name"," "));
        email_text.setText(sh.getString("admin_email"," "));
        city_text.setText(sh.getString("admin_state"," "));
        state_text.setText(sh.getString("admin_city"," "));
        prn_text.setText(sh.getString("admin_prn"," "));
    }


    private void init_dialog_box(){
        customDialog = new Dialog(AdminDashboard.this);
        customDialog.setContentView(R.layout.dep_dialog);
        customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.getWindow().setBackgroundDrawableResource(R.drawable.round_back_white);
        customDialog.setCancelable(true);

        dep_name = customDialog.findViewById(R.id.editTextDep_name);
        back = customDialog.findViewById(R.id.dialog_back);
        add = customDialog.findViewById(R.id.addButton);
    }

    private void init(){
        name_text = findViewById(R.id.name_text);
        college_text = findViewById(R.id.college_text);
        email_text = findViewById(R.id.email_text);
        city_text = findViewById(R.id.city_text);
        state_text = findViewById(R.id.state_text);
        prn_text = findViewById(R.id.prn_text);
        recyclerView = findViewById(R.id.recycler_view);
        button = findViewById(R.id.floating_button);
        list = new ArrayList<>();
    }

    protected void onResume() {
        super.onResume();
        if (!isTaskRoot() && customDialog != null && !customDialog.isShowing() && alertDialog != null && alertDialog.isShowing()) {
            //Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
            fillList();
        }
    }

    @Override
    public void onClick(DepItem labItem) {
        Intent intent = new Intent(AdminDashboard.this,MainActivity.class);
        intent.putExtra("department",labItem.getDep_name());
        Log.d("department",labItem.getDep_name());
        startActivity(intent);
    }

    @Override
    public void onLongPress(DepItem labItem) {
        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AdminDashboard.this);
        builder.setMessage("Are you sure you want to delete this Department");
        builder.setTitle("Alert !");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            list.clear();
            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String college_name = sh.getString("college_name"," ");
            user_ref = FirebaseDatabase.getInstance().getReference("College or University").child(college_name).child("Admin");

            user_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key = snapshot1.getKey();
                            if(key.equals(labItem.getDep_name())){
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