package com.example.dbms_lab_inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class ChoiceActivity extends AppCompatActivity {

    private Button studentBtn;
    private Button adminBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        init();

        studentBtn.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("usertype","student");
            myEdit.apply();

            startActivity(new Intent(ChoiceActivity.this,LoginActivity.class));
        });

        adminBtn.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("usertype","admin");
            myEdit.apply();

            startActivity(new Intent(ChoiceActivity.this,LoginActivity.class));
        });
    }

    private void init(){
        studentBtn = findViewById(R.id.studentButton);
        adminBtn = findViewById(R.id.adminButton);
    }

}