package com.example.dbms_lab_inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

public class GetStarted extends AppCompatActivity {
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_get_started);

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(view -> {
            Intent i = new Intent(GetStarted.this, LoginActivity.class);
            startActivity(i);
            finish();
        });
    }
}