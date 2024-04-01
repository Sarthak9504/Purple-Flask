package com.example.dbms_lab_inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout dep_id;
    private TextInputLayout password;
    private TextInputEditText editTextDep_id;
    private TextInputEditText editTextPass;
    private Button signInBtn;
    private TextView signUpBtn;
    private FirebaseAuth firebaseAuth;
    private boolean isAllCorrect = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIfEmpty(editTextDep_id)){
                    isAllCorrect = ifEmpty(editTextDep_id);
                }
                if(checkIfEmpty(editTextPass)){
                    isAllCorrect = ifEmpty(editTextPass);
                }
                else if(!checkIfEmpty(editTextDep_id)){
                    isAllCorrect = check_mail_vit();
                }
                else{
                    isAllCorrect=true;
                }
                if(isAllCorrect){
                    LoginActivity.this.onClick(signInBtn);
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.onClick(signUpBtn);
            }
        });


    }

    private void onClick(Object o){
        if(o.equals(signInBtn)){
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(editTextDep_id.getText().toString().trim(), editTextPass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putInt("isLogged",1);
                        //myEdit.putBoolean("isFirst",true);
                        myEdit.apply();
                        if(sharedPreferences.getString("usertype","").equals("student")) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("newLogin", false);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            startActivity(new Intent(LoginActivity.this, AdminDashboard.class));
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "User not registered", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            if(sharedPreferences.getString("usertype","").equals("student")){
                startActivity(new Intent(LoginActivity.this,SignUp.class));
            }
            else{
                startActivity(new Intent(LoginActivity.this,SignUpAdmin.class));
            }
        }
    }



    private void init(){
        dep_id = findViewById(R.id.dep_id);
        password = findViewById(R.id.password);
        editTextDep_id = findViewById(R.id.editTextDep_id);
        editTextPass = findViewById(R.id.editTextPass);
        signInBtn = findViewById(R.id.signInButton);
        signUpBtn = findViewById(R.id.signUpBtn);
    }

    private boolean checkIfEmpty(TextInputEditText editText){
        String temp = editText.getText().toString();
        return temp.isEmpty();
    }

    private boolean ifEmpty(TextInputEditText editText){
        editText.setError("Field cannot be empty");
        editText.requestFocus();
        return false;
    }

    private boolean check_mail_vit(){
        String mail = editTextDep_id.getText().toString().trim();
        if(!mail.substring(mail.length()-8).equals("vit.edu")){
            editTextDep_id.setError("Required Vit mail");
            editTextDep_id.requestFocus();
            return false;
        }
        return true;
    }
}